package com.jurispro.system.repository;

import com.jurisPro.system.config.Conexion;
import com.jurispro.system.model.Abogado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbogadoRepository {

    /**
     * Autentica al usuario verificando su nombre de usuario y contraseña en la tabla 'Usuarios'.
     */
    public boolean autenticarAbogado(String username, String password) {
        String sql = "SELECT * FROM Usuarios WHERE username = ? AND password = ? AND rol = 'ABOGADO'";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar abogado en Usuarios: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el primer Id_socio disponible en la base de datos.
     */
    public String obtenerPrimerIdSocio() {
        String sql = "SELECT Id_socio FROM Socio LIMIT 1";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("Id_socio");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de socio: " + e.getMessage());
        }
        return null;
    }

    /**
     * Inserta un abogado en la tabla Abogados según las columnas definidas en el script SQL.
     */
    public boolean guardar(Abogado abogado) {
        String sql = "INSERT INTO Abogados (Id_abogado, name, lastname, especiality, telephone, Id_socio) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, abogado.getIdAbogado());
            stmt.setString(2, abogado.getName());
            stmt.setString(3, abogado.getLastname());
            stmt.setString(4, "General"); // Valor por defecto para la columna especiality
            stmt.setString(5, abogado.getTelephone());
            stmt.setString(6, abogado.getIdSocio());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar abogado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Consulta y retorna todos los abogados registrados.
     */
    public List<Abogado> obtenerTodos() {
        List<Abogado> lista = new ArrayList<>();
        String sql = "SELECT * FROM Abogados";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Abogado a = new Abogado();
                a.setIdAbogado(rs.getString("Id_abogado"));
                a.setName(rs.getString("name"));
                a.setLastname(rs.getString("lastname"));
                a.setTelephone(rs.getString("telephone"));
                a.setIdSocio(rs.getString("Id_socio"));
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar abogados: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza los datos de un abogado existente.
     */
    public boolean actualizar(Abogado abogado) {
        String sql = "UPDATE Abogados SET name = ?, lastname = ?, telephone = ? WHERE Id_abogado = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, abogado.getName());
            stmt.setString(2, abogado.getLastname());
            stmt.setString(3, abogado.getTelephone());
            stmt.setString(4, abogado.getIdAbogado());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar abogado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un abogado por su identificador.
     */
    public boolean eliminar(String idAbogado) {
        String sql = "DELETE FROM Abogados WHERE Id_abogado = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idAbogado);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar abogado: " + e.getMessage());
            return false;
        }
    }
}