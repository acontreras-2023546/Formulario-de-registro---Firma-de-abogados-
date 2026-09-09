package com.jurisPro.system.repository;

import com.jurisPro.system.config.Conexion;
import com.jurisPro.system.model.Rol;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRepository {

    /**
     * Autentica a un usuario llamando al Stored Procedure sp_autenticar_usuario.
     * 
     * @param username Nombre de usuario ingresado en la vista
     * @param password Contraseña ingresada
     * @return El Rol correspondiente si las credenciales son válidas, o null si son incorrectas.
     */
    public Rol autenticar(String username, String password) {
        String sql = "{CALL sp_autenticar_usuario(?, ?)}";

        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String rolBD = rs.getString("rol");
                    
                    // Mapeo directo de la cadena obtenida en BD al Enum Rol
                    return Rol.valueOf(rolBD.toUpperCase());
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al intentar autenticar usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Si no hay coincidencias o se produce un error de conexión
    }
}