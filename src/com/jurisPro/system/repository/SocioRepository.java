package com.jurisPro.system.repository;

import com.jurisPro.system.config.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SocioRepository {

    // =========================================================
    // INSERTAR SOCIO Y DEVOLVER SU ID
    // =========================================================
    public String insertarSocio(String nombre, String apellido, String telefono) {

        String idSocio = UUID.randomUUID().toString();

        String sql = """
            INSERT INTO Socio
            (Id_socio, name, lastname, telephone)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = Conexion.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idSocio);
            stmt.setString(2, nombre);
            stmt.setString(3, apellido);
            stmt.setString(4, telefono);

            stmt.executeUpdate();

            return idSocio;

        } catch (SQLException e) {

            System.err.println(
                    "Error al insertar socio: " + e.getMessage()
            );

            return null;
        }
    }

    // =========================================================
    // LISTAR SOCIOS
    // =========================================================
    public List<Map<String, Object>> listarSocios() {

        List<Map<String, Object>> socios = new ArrayList<>();

        String sql = "{CALL sp_select_socio()}";

        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Map<String, Object> socio = new HashMap<>();

                socio.put(
                        "Id_socio",
                        rs.getString("Id_socio")
                );

                socio.put(
                        "name",
                        rs.getString("name")
                );

                socio.put(
                        "lastname",
                        rs.getString("lastname")
                );

                socio.put(
                        "telephone",
                        rs.getString("telephone")
                );

                socios.add(socio);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar socios: " + e.getMessage()
            );
        }

        return socios;
    }
}