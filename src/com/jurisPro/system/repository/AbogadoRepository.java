package com.jurisPro.system.repository;

import com.jurisPro.system.config.Conexion;

import com.jurispro.system.model.Abogado;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
    
public class AbogadoRepository {


    public List<Abogado> obtenerTodos() {
        List<Abogado> abogados = new ArrayList<>();
        String sql = "{CALL sp_select_abogados()}";

        try (Connection conn = Conexion.getConnection(); 
             CallableStatement stmt = conn.prepareCall(sql); 
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Abogado abogado = new Abogado();
                abogado.setIdAbogado(rs.getString("Id_abogado"));
                abogado.setName(rs.getString("name"));
                abogado.setLastname(rs.getString("lastname"));
                abogado.setTelephone(rs.getString("telephone"));
                abogado.setPassword(rs.getString("password"));
                abogado.setIdSocio(rs.getString("Id_socio"));
                abogado.setIdUsuario(rs.getString("id_usuario"));

                abogados.add(abogado);
            } 
        } catch (SQLException e) {
            System.err.println("Error al listar abogados: " + e.getMessage());
        }

        return abogados;
    }

    public List<Abogado> listarTodos() {
        return obtenerTodos();
    }
}



    public String obtenerPrimerIdSocio() {
        String sql = "SELECT Id_socio FROM Socio WHERE Id_socio IS NOT NULL AND Id_socio != '' LIMIT 1";

        try (Connection conn = Conexion.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("Id_socio");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el primer id_socio: " + e.getMessage());
        }

        return null;
    }

    public boolean autenticarAbogado(String usuario, String password) {
        String sql = "{CALL sp_autenticar_usuario(?, ?)}";

        try (Connection conn = Conexion.getConnection(); 
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id_usuario") != null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar abogado: " + e.getMessage());
        }

        return false;
    }

    public boolean guardar(Abogado abogado) {

        String sql = "{CALL sp_insert_abogado(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = Conexion.getConnection(); 
             CallableStatement stmt = conn.prepareCall(sql)) {

            if (abogado.getIdAbogado() == null || abogado.getIdAbogado().trim().isEmpty()) {
                System.err.println("Error: El ID del abogado no puede estar vacío.");
                return false;
            }

            stmt.setString(1, abogado.getIdAbogado());
            stmt.setString(2, abogado.getName());
            stmt.setString(3, abogado.getLastname());
            stmt.setString(4, abogado.getTelephone());
            stmt.setString(5, abogado.getPassword());
            stmt.setString(6, abogado.getIdSocio());

            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar abogado: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Abogado abogado) {
        String sql = "{CALL sp_update_abogado(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = Conexion.getConnection(); 
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, abogado.getIdAbogado());
            stmt.setString(2, abogado.getName());
            stmt.setString(3, abogado.getLastname());
            stmt.setString(4, abogado.getTelephone());
            stmt.setString(5, abogado.getPassword());
            stmt.setString(6, abogado.getIdSocio());

            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar abogado: " + e.getMessage());
            return false;
        }
    }
}

    public boolean eliminar(String idAbogado) {
        String sql = "{CALL sp_delete_abogado(?)}";

        try (Connection conn = Conexion.getConnection(); 
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, idAbogado);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar abogado: " + e.getMessage());
            return false;
        }
    }
}