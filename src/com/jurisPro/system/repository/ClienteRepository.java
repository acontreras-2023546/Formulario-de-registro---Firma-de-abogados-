package com.jurispro.system.repository;

import com.jurisPro.system.config.Conexion;
import com.jurispro.system.model.Abogado;
import com.jurispro.system.model.Cliente;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

<<<<<<< HEAD
    public List<Cliente> obtenerTodos() {
=======
    public List<Cliente> listarTodos() {
>>>>>>> 2d1c65d (fix: arreglo base de datos)
        List<Cliente> clientes = new ArrayList<>();
        String sql = "{CALL sp_select_clientes()}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();

<<<<<<< HEAD
=======
                // 1. Corregido: La tabla usa 'DPI', 'name', 'lastname', etc.
>>>>>>> 2d1c65d (fix: arreglo base de datos)
                cliente.setDpi(rs.getString("DPI"));
                cliente.setNit(rs.getString("NIT"));
                cliente.setNombre(rs.getString("name"));
                cliente.setApellido(rs.getString("lastname"));
                cliente.setTelefono(rs.getString("telephone"));
                cliente.setDireccion(rs.getString("adress"));

<<<<<<< HEAD
=======
                // 2. Mapeo del abogado asociado
>>>>>>> 2d1c65d (fix: arreglo base de datos)
                String idAbogado = rs.getString("Id_abogado");
                if (idAbogado != null) {
                    Abogado abogado = new Abogado();
                    abogado.setIdAbogado(idAbogado);
                    cliente.setAbogado(abogado);
                }

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }

        return clientes;
    }

<<<<<<< HEAD
    public List<Cliente> listarTodos() {
        return obtenerTodos();
    }

    public boolean autenticarCliente(String usuario, String password) {
        String sql = "{CALL sp_autenticar_cliente(?, ?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar cliente: " + e.getMessage());
            return obtenerTodos().stream()
                    .anyMatch(c -> c.getNombre() != null && c.getNombre().equalsIgnoreCase(usuario));
        }

        return false;
    }

    public boolean guardar(Cliente cliente) {
=======
    public boolean guardar(Cliente cliente) {
        // Orden de parámetros según sp_insert_cliente:
        // (p_dpi, p_nit, p_name, p_lastname, p_telephone, p_adress, p_id_abogado)
>>>>>>> 2d1c65d (fix: arreglo base de datos)
        String sql = "{CALL sp_insert_cliente(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

<<<<<<< HEAD
=======
            // Validar que el DPI no sea nulo ni esté vacío
>>>>>>> 2d1c65d (fix: arreglo base de datos)
            if (cliente.getDpi() == null || cliente.getDpi().trim().isEmpty()) {
                System.err.println("Error: El DPI del cliente no puede estar vacío.");
                return false;
            }

            stmt.setString(1, cliente.getDpi());
            stmt.setString(2, cliente.getNit());
            stmt.setString(3, cliente.getNombre());
            stmt.setString(4, cliente.getApellido());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getDireccion());

<<<<<<< HEAD
=======
            // 7. ID del abogado asignado (puede ser null)
>>>>>>> 2d1c65d (fix: arreglo base de datos)
            String idAbogado = (cliente.getAbogado() != null) ? cliente.getAbogado().getIdAbogado() : null;
            stmt.setString(7, idAbogado);

            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Cliente cliente) {
<<<<<<< HEAD
=======
        // Orden de parámetros según sp_update_cliente:
        // (p_dpi, p_nit, p_name, p_lastname, p_telephone, p_adress, p_id_abogado)
>>>>>>> 2d1c65d (fix: arreglo base de datos)
        String sql = "{CALL sp_update_cliente(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cliente.getDpi());
            stmt.setString(2, cliente.getNit());
            stmt.setString(3, cliente.getNombre());
            stmt.setString(4, cliente.getApellido());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getDireccion());

            String idAbogado = (cliente.getAbogado() != null) ? cliente.getAbogado().getIdAbogado() : null;
            stmt.setString(7, idAbogado);

            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String dpi) {
<<<<<<< HEAD
=======
        // p_dpi VARCHAR(13)
>>>>>>> 2d1c65d (fix: arreglo base de datos)
        String sql = "{CALL sp_delete_cliente(?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, dpi);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}
