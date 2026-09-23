
package com.jurisPro.system.repository;

import com.jurisPro.system.config.Conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class CasoRepository {

    public String insertarCaso(String descripcion,
                               String estado,
                               String informe,
                               String fecha,
                               String idAbogado) {

        String sql = "{CALL sp_insert_caso(?, ?, ?, ?, ?)}";

        try (Connection conn = Conexion.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, descripcion);
            cs.setString(2, estado);
            cs.setString(3, informe);
            cs.setString(4, fecha);
            cs.setString(5, idAbogado);

            cs.executeUpdate();

            String sqlBuscar =
                    "SELECT Id_caso FROM Casos " +
                    "WHERE description = ? " +
                    "AND date = ? " +
                    "ORDER BY Id_caso DESC LIMIT 1";

            try (var ps = conn.prepareStatement(sqlBuscar)) {

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
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, idCaso);
            cs.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("Error al eliminar caso: " + e.getMessage());
            return false;
        }
    }
}


































//package com.jurisPro.system.repository;
//
//import com.jurisPro.system.config.Conexion;
//import java.sql.CallableStatement;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CasoRepository {
//
//    // CREAR
//    public boolean insertar(String description, String status, String informe,
//                            String date, String idAbogado) {
//
//        String sql = "{CALL sp_insert_caso(?, ?, ?, ?, ?)}";
//
//        try (Connection conn = Conexion.getConnection();
//             CallableStatement stmt = conn.prepareCall(sql)) {
//
//            stmt.setString(1, description);
//            stmt.setString(2, status);
//            stmt.setString(3, informe);
//            stmt.setString(4, date);
//            stmt.setString(5, idAbogado);
//
//            stmt.execute();
//            return true;
//
//        } catch (SQLException e) {
//            System.err.println("Error al insertar caso: " + e.getMessage());
//            return false;
//        }
//    }
//
//    // LEER
//    public List<String[]> listar() {
//
//        List<String[]> casos = new ArrayList<>();
//
//        String sql = "{CALL sp_select_casos()}";
//
//        try (Connection conn = Conexion.getConnection();
//             CallableStatement stmt = conn.prepareCall(sql);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//
//                String[] caso = {
//                    rs.getString("Id_caso"),
//                    rs.getString("description"),
//                    rs.getString("status"),
//                    rs.getString("informe"),
//                    rs.getString("date"),
//                    rs.getString("Id_abogado")
//                };
//
//                casos.add(caso);
//            }
//
//        } catch (SQLException e) {
//            System.err.println("Error al listar casos: " + e.getMessage());
//        }
//
//        return casos;
//    }
//
//    // EDITAR
//    public boolean actualizar(String idCaso, String description, String status,
//                              String informe, String date, String idAbogado) {
//
//        String sql = "{CALL sp_update_caso(?, ?, ?, ?, ?, ?)}";
//
//        try (Connection conn = Conexion.getConnection();
//             CallableStatement stmt = conn.prepareCall(sql)) {
//
//            stmt.setString(1, idCaso);
//            stmt.setString(2, description);
//            stmt.setString(3, status);
//            stmt.setString(4, informe);
//            stmt.setString(5, date);
//            stmt.setString(6, idAbogado);
//
//            stmt.execute();
//            return true;
//
//        } catch (SQLException e) {
//            System.err.println("Error al actualizar caso: " + e.getMessage());
//            return false;
//        }
//    }
//
//    // ELIMINAR
//    public boolean eliminar(String idCaso) {
//
//        String sql = "{CALL sp_delete_caso(?)}";
//
//        try (Connection conn = Conexion.getConnection();
//             CallableStatement stmt = conn.prepareCall(sql)) {
//
//            stmt.setString(1, idCaso);
//
//            stmt.execute();
//            return true;
//
//        } catch (SQLException e) {
//            System.err.println("Error al eliminar caso: " + e.getMessage());
//            return false;
//        }
//    }
//}