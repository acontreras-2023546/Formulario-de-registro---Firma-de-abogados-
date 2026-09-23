package com.jurisPro.system.repository;

import com.jurisPro.system.config.Conexion;
import com.jurisPro.system.model.Cliente;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    public boolean insertar(Cliente cliente) {
        return guardar(cliente);
    }

    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "{CALL sp_select_clientes()}";

        try (Connection conn = Conexion.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setDpi(rs.getString("DPI"));
                cliente.setNit(rs.getString("NIT"));
                cliente.setNombre(rs.getString("name"));
                cliente.setApellido(rs.getString("lastname"));
                cliente.setTelefono(rs.getString("telephone"));
                cliente.setDireccion(rs.getString("adress"));
                cliente.setIdUsuario(rs.getString("id_usuario"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return clientes;
    }

    public List<Cliente> listarTodos() {
        return listar();
    }

    public List<Cliente> obtenerTodos() {
        return listar();
    }

    public boolean autenticarCliente(String usuario, String password) {
        for (Cliente cliente : listar()) {
            boolean coincideUsuario = usuario != null &&
                    (usuario.equalsIgnoreCase(cliente.getDpi()) ||
                     (cliente.getNit() != null && usuario.equalsIgnoreCase(cliente.getNit())));
            if (coincideUsuario && password != null && password.equals(cliente.getPassword())) {
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al autenticar cliente: " + e.getMessage());
        }
        return false;
    }

    public boolean guardar(Cliente cliente) {
        // Ahora envía 8 parámetros incluyendo la contraseña
        String sql = "{CALL sp_insert_cliente(?, ?, ?, ?, ?, ?, ?, ?)}";

        if (cliente == null || cliente.getDpi() == null || cliente.getDpi().trim().isEmpty()) {
            System.err.println("Error: El DPI del cliente no puede estar vacío.");
            return false;
        }

        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cliente.getDpi());
            stmt.setString(2, cliente.getNit());
            stmt.setString(3, cliente.getNombre());
            stmt.setString(4, cliente.getApellido());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getDireccion());

            stmt.setString(7, cliente.getPassword());

            String idAbogado = (cliente.getAbogado() != null) ? cliente.getAbogado().getIdAbogado() : null;
            stmt.setString(8, idAbogado);

            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Cliente cliente) {
        String sql = "{CALL sp_update_cliente(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cliente.getDpi());
            stmt.setString(2, cliente.getNit());
            stmt.setString(3, cliente.getNombre());
            stmt.setString(4, cliente.getApellido());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getDireccion());

            stmt.setString(7, cliente.getPassword());

            String idAbogado = (cliente.getAbogado() != null) ? cliente.getAbogado().getIdAbogado() : null;
            stmt.setString(8, idAbogado);

            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String dpi) {
        String sql = "{CALL sp_delete_cliente(?)}";

        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, dpi);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}
