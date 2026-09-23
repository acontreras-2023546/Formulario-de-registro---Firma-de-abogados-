package com.jurisPro.system.repository;

import com.jurisPro.system.config.Conexion;
import com.jurisPro.system.model.Abogado;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbogadoRepository {

    private final SocioRepository socioRepo = new SocioRepository();

    // =========================================================
    // OBTENER TODOS LOS ABOGADOS
    // =========================================================
    public List<Abogado> obtenerTodos() {

        List<Abogado> abogados = new ArrayList<>();

        String sql = "{CALL sp_select_abogados()}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Abogado abogado = new Abogado();

                abogado.setIdAbogado(
                        rs.getString("Id_abogado")
                );

                abogado.setName(
                        rs.getString("name")
                );

                abogado.setLastname(
                        rs.getString("lastname")
                );

                abogado.setEspeciality(
                        rs.getString("especiality")
                );

                abogado.setTelephone(
                        rs.getString("telephone")
                );

                abogado.setIdSocio(
                        rs.getString("Id_socio")
                );

                abogado.setUsername(
                        rs.getString("username")
                );

                abogado.setPassword(
                        rs.getString("password")
                );

                abogado.setIdUsuario(
                        rs.getString("id_usuario")
                );

                abogados.add(abogado);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar abogados: "
                    + e.getMessage()
            );
        }

        return abogados;
    }

    // =========================================================
    // LISTAR TODOS
    // =========================================================
    public List<Abogado> listarTodos() {
        return obtenerTodos();
    }

    // =========================================================
    // OBTENER UN SOCIO EXISTENTE
    // =========================================================
    public String obtenerPrimerIdSocio() {

        String sql = """
            SELECT Id_socio
            FROM Socio
            WHERE Id_socio IS NOT NULL
            AND Id_socio != ''
            LIMIT 1
            """;

        try (Connection conn = Conexion.getConnection(); java.sql.PreparedStatement stmt
                = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("Id_socio");
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener el primer id_socio: "
                    + e.getMessage()
            );
        }

        return null;
    }

    // =========================================================
    // AUTENTICAR ABOGADO
    // =========================================================
    public Abogado autenticarAbogado(
            String usuario,
            String password) {

        String sql = "{CALL sp_autenticar_abogado(?, ?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Abogado abogado = new Abogado();

                    abogado.setIdAbogado(
                            rs.getString("Id_abogado")
                    );

                    abogado.setName(
                            rs.getString("name")
                    );

                    abogado.setLastname(
                            rs.getString("lastname")
                    );

                    abogado.setEspeciality(
                            rs.getString("especiality")
                    );

                    abogado.setTelephone(
                            rs.getString("telephone")
                    );

                    abogado.setIdSocio(
                            rs.getString("Id_socio")
                    );

                    abogado.setUsername(
                            rs.getString("username")
                    );

                    abogado.setPassword(
                            rs.getString("password")
                    );

                    return abogado;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al autenticar abogado: "
                    + e.getMessage()
            );
        }

        return null;
    }

    // =========================================================
    // OBTENER ABOGADO POR ID
    // =========================================================
    public Abogado obtenerPorId(String idAbogado) {

        if (idAbogado == null || idAbogado.isBlank()) {
            return null;
        }

        String sql = """
            SELECT Id_abogado, name, lastname, especiality, telephone,
                   Id_socio, username, password, id_usuario
            FROM Abogados
            WHERE Id_abogado = ?
            LIMIT 1
            """;

        try (Connection conn = Conexion.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idAbogado);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Abogado abogado = new Abogado();
                    abogado.setIdAbogado(rs.getString("Id_abogado"));
                    abogado.setName(rs.getString("name"));
                    abogado.setLastname(rs.getString("lastname"));
                    abogado.setEspeciality(rs.getString("especiality"));
                    abogado.setTelephone(rs.getString("telephone"));
                    abogado.setIdSocio(rs.getString("Id_socio"));
                    abogado.setUsername(rs.getString("username"));
                    abogado.setPassword(rs.getString("password"));
                    abogado.setIdUsuario(rs.getString("id_usuario"));
                    return abogado;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener abogado por ID: " + e.getMessage());
        }

        return null;
    }

    // =========================================================
    // GUARDAR ABOGADO
    // =========================================================
    public boolean guardar(Abogado abogado) {

        String sql
                = "{CALL sp_insert_abogado(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            // -------------------------------------------------
            // ID DEL ABOGADO
            // -------------------------------------------------
            String idAbogado = abogado.getIdAbogado();

            if (idAbogado == null
                    || idAbogado.trim().isEmpty()) {

                idAbogado
                        = java.util.UUID.randomUUID().toString();
            }

            // -------------------------------------------------
            // ID DEL SOCIO
            // -------------------------------------------------
            String idSocio = abogado.getIdSocio();

            if (idSocio == null
                    || idSocio.trim().isEmpty()) {

                idSocio = socioRepo.insertarSocio(
                        abogado.getName(),
                        abogado.getLastname(),
                        abogado.getTelephone()
                );

                if (idSocio == null) {

                    System.err.println(
                            "No se pudo crear el socio."
                    );

                    return false;
                }
            }

            // -------------------------------------------------
            // PARÁMETROS DEL PROCEDIMIENTO
            // -------------------------------------------------
            stmt.setString(1, idAbogado);
            stmt.setString(2, abogado.getName());
            stmt.setString(3, abogado.getLastname());
            stmt.setString(
                    4,
                    abogado.getEspeciality() == null
                    ? ""
                    : abogado.getEspeciality()
            );
            stmt.setString(5, abogado.getTelephone());
            stmt.setString(6, abogado.getUsername());
            stmt.setString(7, abogado.getPassword());
            stmt.setString(8, idSocio);

            // El abogado actualmente no tiene un registro
            // asociado en la tabla Usuarios.
            stmt.setString(9, null);

            stmt.execute();

            // Guardamos los IDs generados
            abogado.setIdAbogado(idAbogado);
            abogado.setIdSocio(idSocio);

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "Error al insertar abogado: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // ACTUALIZAR ABOGADO
    // =========================================================
    public boolean actualizar(Abogado abogado) {

        String sql
                = "{CALL sp_update_abogado(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, abogado.getIdAbogado());
            stmt.setString(2, abogado.getName());
            stmt.setString(3, abogado.getLastname());
            stmt.setString(4, abogado.getEspeciality());
            stmt.setString(5, abogado.getTelephone());
            stmt.setString(6, abogado.getUsername());
            stmt.setString(7, abogado.getPassword());
            stmt.setString(8, abogado.getIdSocio());

            stmt.execute();

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar abogado: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // ELIMINAR ABOGADO
    // =========================================================
    public boolean eliminar(String idAbogado) {

        String sql = "{CALL sp_delete_abogado(?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, idAbogado);

            stmt.execute();

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "Error al eliminar abogado: "
                    + e.getMessage()
            );

            return false;
        }
    }
}
