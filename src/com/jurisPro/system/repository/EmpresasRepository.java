package com.jurisPro.system.repository;

import com.jurisPro.system.config.Conexion;
import com.jurisPro.system.model.Abogado;
import com.jurisPro.system.model.Empresas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpresasRepository {

    public List<Empresas> listarTodos() {
        List<Empresas> empresas = new ArrayList<>();

        String sql = """
            SELECT
                Id_empresa,
                name,
                telephone,
                adress,
                Id_abogado,
                id_usuario
            FROM Empresas
            """;

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Empresas empresa = new Empresas();
                empresa.setIdEmpresa(rs.getString("Id_empresa"));
                empresa.setNombre(rs.getString("name"));
                empresa.setTelefono(rs.getString("telephone"));
                empresa.setDireccion(rs.getString("adress"));
                empresa.setIdUsuario(rs.getString("id_usuario"));

                String idAbogado = rs.getString("Id_abogado");
                if (idAbogado != null && !idAbogado.isBlank()) {
                    Abogado abogado = new Abogado();
                    abogado.setIdAbogado(idAbogado);
                    empresa.setAbogado(abogado);
                }

                empresas.add(empresa);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar empresas: " + e.getMessage());
        }

        return empresas;
    }

    public List<Empresas> obtenerTodos() {
        return listarTodos();
    }

    /**
     * Autentica una empresa usando la misma tabla Usuarios que utilizan
     * los clientes. Ambas cuentas usan el rol CLIENTE porque comparten
     * el mismo portal/vista.
     */
    public Empresas autenticarEmpresa(String usuario, String password) {

        String sql = """
            SELECT
                e.Id_empresa,
                e.name,
                e.telephone,
                e.adress,
                e.Id_abogado,
                e.id_usuario,
                u.username
            FROM Empresas e
            INNER JOIN Usuarios u
                ON e.id_usuario = u.id_usuario
            WHERE u.username = ?
              AND u.password = ?
              AND u.rol = 'CLIENTE'
            LIMIT 1
            """;

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Empresas empresa = new Empresas();
                    empresa.setIdEmpresa(rs.getString("Id_empresa"));
                    empresa.setNombre(rs.getString("name"));
                    empresa.setTelefono(rs.getString("telephone"));
                    empresa.setDireccion(rs.getString("adress"));
                    empresa.setIdUsuario(rs.getString("id_usuario"));
                    empresa.setUsername(rs.getString("username"));

                    String idAbogado = rs.getString("Id_abogado");
                    if (idAbogado != null && !idAbogado.isBlank()) {
                        Abogado abogado = new Abogado();
                        abogado.setIdAbogado(idAbogado);
                        empresa.setAbogado(abogado);
                    }

                    return empresa;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar empresa: " + e.getMessage());
        }

        return null;
    }

    /**
     * Crea empresa + cuenta de acceso en una sola transacción.
     */
    public boolean guardarConCredenciales(
            Empresas empresa,
            String username,
            String password) {

        if (empresa == null
                || username == null || username.isBlank()
                || password == null || password.isEmpty()) {
            return false;
        }

        String idUsuario = java.util.UUID.randomUUID().toString();
        String idEmpresa = empresa.getIdEmpresa();

        if (idEmpresa == null || idEmpresa.isBlank()) {
            idEmpresa = java.util.UUID.randomUUID().toString();
        }

        String sqlUsuario = """
            INSERT INTO Usuarios (id_usuario, username, password, rol)
            VALUES (?, ?, ?, 'CLIENTE')
            """;

        String sqlEmpresa = """
            INSERT INTO Empresas
                (Id_empresa, name, telephone, adress, Id_abogado, id_usuario)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = Conexion.getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                    stmt.setString(1, idUsuario);
                    stmt.setString(2, username.trim());
                    stmt.setString(3, password);
                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = conn.prepareStatement(sqlEmpresa)) {
                    stmt.setString(1, idEmpresa);
                    stmt.setString(2, empresa.getNombre());
                    stmt.setString(3, empresa.getTelefono());
                    stmt.setString(4, empresa.getDireccion());
                    stmt.setString(5,
                            empresa.getAbogado() != null
                                    ? empresa.getAbogado().getIdAbogado()
                                    : null);
                    stmt.setString(6, idUsuario);
                    stmt.executeUpdate();
                }

                conn.commit();
                empresa.setIdEmpresa(idEmpresa);
                empresa.setIdUsuario(idUsuario);
                empresa.setUsername(username.trim());
                return true;

            } catch (SQLException e) {
                try { conn.rollback(); } catch (SQLException ignored) { }
                System.err.println("Error al crear empresa y usuario: " + e.getMessage());
                return false;
            } finally {
                try { conn.setAutoCommit(true); } catch (SQLException ignored) { }
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión al crear empresa: " + e.getMessage());
            return false;
        }
    }

    /** Mantiene compatibilidad con llamadas antiguas. */
    public boolean guardar(Empresas empresa) {
        if (empresa == null) return false;
        String username = empresa.getUsername();
        String password = empresa.getPassword();
        return guardarConCredenciales(empresa, username, password);
    }

    public boolean actualizar(Empresas empresa) {
        String sql = """
            UPDATE Empresas
            SET name = ?, telephone = ?, adress = ?, Id_abogado = ?
            WHERE Id_empresa = ?
            """;

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empresa.getNombre());
            stmt.setString(2, empresa.getTelefono());
            stmt.setString(3, empresa.getDireccion());
            stmt.setString(4,
                    empresa.getAbogado() != null
                            ? empresa.getAbogado().getIdAbogado()
                            : null);
            stmt.setString(5, empresa.getIdEmpresa());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar empresa: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String idEmpresa) {
        String sql = "DELETE FROM Empresas WHERE Id_empresa = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idEmpresa);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar empresa: " + e.getMessage());
            return false;
        }
    }
}
