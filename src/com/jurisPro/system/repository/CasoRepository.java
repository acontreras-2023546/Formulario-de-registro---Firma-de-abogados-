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

    private static final DateTimeFormatter FORMATO_FECHA
            = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Obtiene el caso asociado a un cliente. El resultado contiene:
     * description, status, informe, date e id_abogado.
     */
// ============================================================
// OBTENER CASO DEL CLIENTE
// ============================================================

public Map<String, String> obtenerCasoPorCliente(String dpi) {

    String sql = """
        SELECT
            Id_caso,
            description,
            status,
            informe,
            date,
            Id_abogado
        FROM Casos
        WHERE DPI_cliente = ?
        ORDER BY date DESC, Id_caso DESC
        LIMIT 1
        """;

    return obtenerCaso(sql, dpi);
}


// ============================================================
// OBTENER CASO DE LA EMPRESA
// ============================================================

public Map<String, String> obtenerCasoPorEmpresa(String idEmpresa) {

    String sql = """
        SELECT
            Id_caso,
            description,
            status,
            informe,
            date,
            Id_abogado
        FROM Casos
        WHERE Id_empresa = ?
        ORDER BY date DESC, Id_caso DESC
        LIMIT 1
        """;

    return obtenerCaso(sql, idEmpresa);
}


// ============================================================
// MÉTODO GENERAL PARA OBTENER CASO
// ============================================================

private Map<String, String> obtenerCaso(
        String sql,
        String identificador) {

    if (identificador == null || identificador.isBlank()) {
        return null;
    }

    try (
        Connection conn = Conexion.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)
    ) {

        stmt.setString(1, identificador);

        try (ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {

                Map<String, String> caso =
                        new HashMap<>();

                caso.put(
                        "idCaso",
                        rs.getString("Id_caso")
                );

                caso.put(
                        "description",
                        rs.getString("description")
                );

                caso.put(
                        "status",
                        rs.getString("status")
                );

                caso.put(
                        "informe",
                        rs.getString("informe")
                );

                caso.put(
                        "date",
                        rs.getString("date")
                );

                caso.put(
                        "idAbogado",
                        rs.getString("Id_abogado")
                );

                return caso;
            }
        }

    } catch (SQLException e) {

        System.err.println(
                "Error al obtener caso: "
                + e.getMessage()
        );
    }

    return null;
}


// ============================================================
// GUARDAR ACTUALIZACIÓN DEL CLIENTE
// ============================================================

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


// ============================================================
// GUARDAR ACTUALIZACIÓN DE EMPRESA
// ============================================================

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


// ============================================================
// ACTUALIZAR CASO
// ============================================================

private boolean guardarActualizacion(
        String columnaRelacion,
        String identificador,
        String idAbogado,
        String estado,
        String nuevoInforme) {

    /*
     * IMPORTANTE:
     *
     * Buscamos el caso únicamente por el cliente/empresa.
     *
     * NO hacemos:
     *
     * WHERE DPI_cliente = ? AND Id_abogado = ?
     *
     * porque eso podría crear un caso nuevo cuando
     * cambia el abogado.
     */

    String sqlBuscar = """
        SELECT
            Id_caso,
            description,
            informe
        FROM Casos
        WHERE %s = ?
        ORDER BY date DESC, Id_caso DESC
        LIMIT 1
        """.formatted(columnaRelacion);


    String ahora =
            LocalDateTime
                    .now()
                    .format(FORMATO_FECHA);


    String informeLimpio =
            nuevoInforme == null
                    ? ""
                    : nuevoInforme.trim();


    try (
        Connection conn = Conexion.getConnection()
    ) {

        String idCaso = null;

        String descripcion =
                "Proceso legal";

        String informeAnterior = null;


        // --------------------------------------------------------
        // BUSCAR CASO EXISTENTE
        // --------------------------------------------------------

        try (
            PreparedStatement buscar =
                    conn.prepareStatement(sqlBuscar)
        ) {

            buscar.setString(
                    1,
                    identificador
            );

            try (
                ResultSet rs =
                        buscar.executeQuery()
            ) {

                if (rs.next()) {

                    idCaso =
                            rs.getString("Id_caso");

                    descripcion =
                            rs.getString("description");

                    informeAnterior =
                            rs.getString("informe");
                }
            }
        }


        // --------------------------------------------------------
        // CONSERVAR INFORMES ANTERIORES
        // --------------------------------------------------------

        String informeFinal =
                informeAnterior;


        if (!informeLimpio.isEmpty()) {

            String nuevoRegistro =
                    "[" + ahora + "]\n"
                    + informeLimpio;


            if (informeFinal == null
                    || informeFinal.isBlank()) {

                informeFinal =
                        nuevoRegistro;

            } else {

                informeFinal =
                        informeFinal
                        + "\n\n"
                        + nuevoRegistro;
            }
        }


        // --------------------------------------------------------
        // SI NO EXISTE CASO → CREAR
        // --------------------------------------------------------

        if (idCaso == null) {

            String sqlInsert = """
                INSERT INTO Casos
                    (
                        Id_caso,
                        description,
                        status,
                        informe,
                        date,
                        Id_abogado,
                        %s
                    )
                VALUES
                    (
                        UUID(),
                        ?,
                        ?,
                        ?,
                        ?,
                        ?,
                        ?
                    )
                """.formatted(columnaRelacion);


            try (
                PreparedStatement stmt =
                        conn.prepareStatement(sqlInsert)
            ) {

                stmt.setString(
                        1,
                        descripcion
                );

                stmt.setString(
                        2,
                        estado
                );

                stmt.setString(
                        3,
                        informeFinal
                );

                stmt.setString(
                        4,
                        ahora
                );

                stmt.setString(
                        5,
                        idAbogado
                );

                stmt.setString(
                        6,
                        identificador
                );

                stmt.executeUpdate();

                return true;
            }
        }


        // --------------------------------------------------------
        // SI EXISTE → ACTUALIZAR
        // --------------------------------------------------------

        String sqlUpdate = """
            UPDATE Casos
            SET
                status = ?,
                informe = ?,
                date = ?
            WHERE Id_caso = ?
            """;


        try (
            PreparedStatement stmt =
                    conn.prepareStatement(sqlUpdate)
        ) {

            stmt.setString(
                    1,
                    estado
            );

            stmt.setString(
                    2,
                    informeFinal
            );

            stmt.setString(
                    3,
                    ahora
            );

            stmt.setString(
                    4,
                    idCaso
            );

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

        try (Connection conn = Conexion.getConnection(); java.sql.CallableStatement cs = conn.prepareCall(sql)) {

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

        try (Connection conn = Conexion.getConnection(); java.sql.CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, idCaso);
            cs.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error al eliminar caso: " + e.getMessage());
            return false;
        }
    }
}
