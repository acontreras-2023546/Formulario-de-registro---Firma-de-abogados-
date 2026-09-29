package com.jurisPro.system.repository;

import com.jurisPro.system.config.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CasoRepository {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Asegura que las columnas de relación existan incluso si la base de
     * datos fue creada con una versión anterior del proyecto.
     */
    private void asegurarColumnasRelacionCaso(Connection conn) {
        String[] sentencias = {
            "ALTER TABLE Casos ADD COLUMN IF NOT EXISTS DPI_cliente VARCHAR(13) NULL",
            "ALTER TABLE Casos ADD COLUMN IF NOT EXISTS Id_empresa VARCHAR(36) NULL"
        };

        for (String sql : sentencias) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                // Si la columna ya existe o el servidor no permite DDL aquí,
                // las consultas normales seguirán intentando trabajar con el esquema actual.
            }
        }
    }

    /**
     * Obtiene el caso asociado directamente al DPI del cliente.
     */
    public Map<String, String> obtenerCasoPorCliente(String dpi) {
        String sql = """
            SELECT Id_caso, description, status, informe, date, Id_abogado
            FROM Casos
            WHERE DPI_cliente = ?
            ORDER BY date DESC, Id_caso DESC
            LIMIT 1
            """;
        return obtenerCaso(sql, dpi);
    }

    /**
     * Obtiene el caso del cliente. Primero busca la relación DPI_cliente.
     * Como compatibilidad con casos creados con la versión anterior del
     * sistema, si no existe esa relación busca un caso sin DPI asociado
     * al mismo abogado.
     */
    public Map<String, String> obtenerCasoPorCliente(
            String dpi, String idAbogado) {

        Map<String, String> caso = obtenerCasoPorCliente(dpi);

        if (caso != null) {
            return caso;
        }

        if (idAbogado == null || idAbogado.isBlank()) {
            return null;
        }

        String sql = """
            SELECT Id_caso, description, status, informe, date, Id_abogado
            FROM Casos
            WHERE Id_abogado = ?
              AND (DPI_cliente IS NULL OR DPI_cliente = '')
              AND (Id_empresa IS NULL OR Id_empresa = '')
            ORDER BY date DESC, Id_caso DESC
            LIMIT 1
            """;

        return obtenerCaso(sql, idAbogado);
    }

    /** Obtiene el caso asociado a una empresa. */
    public Map<String, String> obtenerCasoPorEmpresa(String idEmpresa) {
        String sql = """
            SELECT Id_caso, description, status, informe, date, Id_abogado
            FROM Casos
            WHERE Id_empresa = ?
            ORDER BY date DESC, Id_caso DESC
            LIMIT 1
            """;
        return obtenerCaso(sql, idEmpresa);
    }

    private Map<String, String> obtenerCaso(String sql, String identificador) {
        if (identificador == null || identificador.isBlank()) {
            return null;
        }

        try (Connection conn = Conexion.getConnection()) {

            asegurarColumnasRelacionCaso(conn);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, identificador);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Map<String, String> caso = new HashMap<>();
                        caso.put("idCaso", rs.getString("Id_caso"));
                        caso.put("description", rs.getString("description"));
                        caso.put("status", rs.getString("status"));
                        caso.put("informe", rs.getString("informe"));
                        caso.put("date", rs.getString("date"));
                        caso.put("idAbogado", rs.getString("Id_abogado"));
                        return caso;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener caso: " + e.getMessage());
        }

        return null;
    }

    /** Guarda el estado y el último informe del caso del cliente. */
    public boolean guardarActualizacionCliente(
            String dpi,
            String idAbogado,
            String estado,
            String nuevoInforme) {

        if (dpi == null || dpi.isBlank()
                || idAbogado == null || idAbogado.isBlank()
                || estado == null || estado.isBlank()) {
            return false;
        }

        return guardarActualizacion(
                "DPI_cliente",
                dpi,
                idAbogado,
                estado,
                nuevoInforme
        );
    }

    /** Guarda el estado y el último informe del caso de una empresa. */
    public boolean guardarActualizacionEmpresa(
            String idEmpresa,
            String idAbogado,
            String estado,
            String nuevoInforme) {

        if (idEmpresa == null || idEmpresa.isBlank()
                || idAbogado == null || idAbogado.isBlank()
                || estado == null || estado.isBlank()) {
            return false;
        }

        return guardarActualizacion(
                "Id_empresa",
                idEmpresa,
                idAbogado,
                estado,
                nuevoInforme
        );
    }

    private boolean guardarActualizacion(
            String columnaRelacion,
            String identificador,
            String idAbogado,
            String estado,
            String nuevoInforme) {

        String sqlBuscar = """
            SELECT Id_caso, description
            FROM Casos
            WHERE %s = ?
            ORDER BY date DESC, Id_caso DESC
            LIMIT 1
            """.formatted(columnaRelacion);

        String ahora = LocalDateTime.now().format(FORMATO_FECHA);
        String informe = nuevoInforme == null ? "" : nuevoInforme.trim();

        try (Connection conn = Conexion.getConnection()) {

            asegurarColumnasRelacionCaso(conn);

            String idCaso = null;
            String descripcion = "Proceso legal";

            // Primero buscamos el caso que YA está vinculado al cliente/empresa.
            try (PreparedStatement buscar = conn.prepareStatement(sqlBuscar)) {
                buscar.setString(1, identificador);

                try (ResultSet rs = buscar.executeQuery()) {
                    if (rs.next()) {
                        idCaso = rs.getString("Id_caso");
                        String desc = rs.getString("description");
                        if (desc != null && !desc.isBlank()) {
                            descripcion = desc;
                        }
                    }
                }
            }

            // Si no existe un caso vinculado, creamos uno YA vinculado.
            // Esto es lo que permite que el portal del cliente encuentre
            // exactamente el estado que acaba de guardar el abogado.
            if (idCaso == null) {

                String sqlInsert = """
                    INSERT INTO Casos
                        (Id_caso, description, status, informe, date, Id_abogado, %s)
                    VALUES
                        (UUID(), ?, ?, ?, ?, ?, ?)
                    """.formatted(columnaRelacion);

                try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
                    stmt.setString(1, descripcion);
                    stmt.setString(2, estado.trim());
                    stmt.setString(3, informe);
                    stmt.setString(4, ahora);
                    stmt.setString(5, idAbogado);
                    stmt.setString(6, identificador);
                    return stmt.executeUpdate() > 0;
                }
            }

            // Si ya existe, actualizamos EL MISMO caso.
            String sqlUpdate = """
                UPDATE Casos
                SET status = ?,
                    informe = ?,
                    date = ?,
                    Id_abogado = ?
                WHERE Id_caso = ?
                """;

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
                stmt.setString(1, estado.trim());
                stmt.setString(2, informe);
                stmt.setString(3, ahora);
                stmt.setString(4, idAbogado);
                stmt.setString(5, idCaso);
                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al guardar actualización del caso: "
                    + e.getMessage()
            );
            return false;
        }
    }

    // Métodos antiguos conservados para compatibilidad.
    public String insertarCaso(String descripcion,
                               String estado,
                               String informe,
                               String fecha,
                               String idAbogado) {

        String sql = "{CALL sp_insert_caso(?, ?, ?, ?, ?)}";

        try (Connection conn = Conexion.getConnection();
             java.sql.CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, descripcion);
            cs.setString(2, estado);
            cs.setString(3, informe);
            cs.setString(4, fecha);
            cs.setString(5, idAbogado);
            cs.executeUpdate();

            String sqlBuscar = "SELECT Id_caso FROM Casos WHERE description = ? AND date = ? ORDER BY Id_caso DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sqlBuscar)) {
                ps.setString(1, descripcion);
                ps.setString(2, fecha);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("Id_caso");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al insertar caso: " + e.getMessage());
        }

        return null;
    }

    public boolean eliminarCaso(String idCaso) {

        String sql = "{CALL sp_delete_caso(?)}";

        try (Connection conn = Conexion.getConnection();
             java.sql.CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, idCaso);
            cs.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al eliminar caso: " + e.getMessage());
            return false;
        }
    }
}
