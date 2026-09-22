package com.jurispro.system.repository;

import com.jurisPro.system.config.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocioRepository {

    public boolean insertarSocio(String nombre, String apellido, String telefono) {
        String sql = "{CALL sp_insert_socio(?, ?, ?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, telefono);

            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar socio: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> listarSocios() {
        List<Map<String, Object>> socios = new ArrayList<>();
        String sql = "{CALL sp_select_socio()}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> socio = new HashMap<>();
                socio.put("Id_socio", rs.getString("Id_socio"));
                socio.put("name", rs.getString("name"));
                socio.put("lastname", rs.getString("lastname"));
                socio.put("telephone", rs.getString("telephone"));
                socios.add(socio);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar socios: " + e.getMessage());
        }

        return socios;
    }
}
