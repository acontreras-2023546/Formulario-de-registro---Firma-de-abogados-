package com.jurisPro.system.repository;

import com.jurisPro.system.config.Conexion;
import com.jurisPro.system.model.Abogado;
import com.jurisPro.system.model.Cliente;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    // =========================================================
    // INSERTAR
    // =========================================================

    public boolean insertar(Cliente cliente) {
        return guardar(cliente);
    }

    // =========================================================
    // LISTAR TODOS LOS CLIENTES
    // =========================================================

    public List<Cliente> listar() {

        List<Cliente> clientes = new ArrayList<>();

        String sql = "{CALL sp_select_clientes()}";

        try (Connection conn = Conexion.getConnection();
             CallableStatement cs = conn.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {

                Cliente cliente = new Cliente();

                cliente.setDpi(
                        rs.getString("DPI")
                );

                cliente.setNit(
                        rs.getString("NIT")
                );

                cliente.setNombre(
                        rs.getString("name")
                );

                cliente.setApellido(
                        rs.getString("lastname")
                );

                cliente.setTelefono(
                        rs.getString("telephone")
                );

                cliente.setDireccion(
                        rs.getString("adress")
                );

                // ---------------------------------------------
                // ID DEL ABOGADO
                // ---------------------------------------------

                String idAbogado =
                        rs.getString("Id_abogado");

                if (idAbogado != null
                        && !idAbogado.trim().isEmpty()) {

                    Abogado abogado = new Abogado();

                    abogado.setIdAbogado(idAbogado);

                    cliente.setAbogado(abogado);
                }

                // Se mantiene por compatibilidad
                cliente.setIdUsuario(
                        rs.getString("id_usuario")
                );

                clientes.add(cliente);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar clientes: "
                    + e.getMessage()
            );
        }

        return clientes;
    }

    // =========================================================
    // LISTAR CLIENTES POR ABOGADO
    // =========================================================

    public List<Cliente> listarPorAbogado(String idAbogado) {

        List<Cliente> clientes = new ArrayList<>();

        if (idAbogado == null
                || idAbogado.trim().isEmpty()) {

            return clientes;
        }

        String sql = """
            SELECT
                DPI,
                NIT,
                name,
                lastname,
                telephone,
                adress,
                Id_abogado,
                id_usuario
            FROM Clientes
            WHERE Id_abogado = ?
            """;

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idAbogado);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Cliente cliente = new Cliente();

                    cliente.setDpi(
                            rs.getString("DPI")
                    );

                    cliente.setNit(
                            rs.getString("NIT")
                    );

                    cliente.setNombre(
                            rs.getString("name")
                    );

                    cliente.setApellido(
                            rs.getString("lastname")
                    );

                    cliente.setTelefono(
                            rs.getString("telephone")
                    );

                    cliente.setDireccion(
                            rs.getString("adress")
                    );

                    // -----------------------------------------
                    // ABOGADO
                    // -----------------------------------------

                    String idAbogadoCliente =
                            rs.getString("Id_abogado");

                    if (idAbogadoCliente != null) {

                        Abogado abogado = new Abogado();

                        abogado.setIdAbogado(
                                idAbogadoCliente
                        );

                        cliente.setAbogado(abogado);
                    }

                    cliente.setIdUsuario(
                            rs.getString("id_usuario")
                    );

                    clientes.add(cliente);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar clientes del abogado: "
                    + e.getMessage()
            );
        }

        return clientes;
    }

    // =========================================================
    // MÉTODOS COMPATIBLES CON EL PROYECTO
    // =========================================================

    public List<Cliente> listarTodos() {
        return listar();
    }

    public List<Cliente> obtenerTodos() {
        return listar();
    }

    // =========================================================
    // AUTENTICAR CLIENTE
    // =========================================================

    public boolean autenticarCliente(
            String usuario,
            String password) {

        for (Cliente cliente : listar()) {

            boolean coincideUsuario =
                    usuario != null
                    && (
                        usuario.equalsIgnoreCase(
                                cliente.getDpi()
                        )
                        ||
                        (
                            cliente.getNit() != null
                            &&
                            usuario.equalsIgnoreCase(
                                    cliente.getNit()
                            )
                        )
                    );

            if (coincideUsuario
                    && password != null
                    && password.equals(
                            cliente.getPassword()
                    )) {

                return true;
            }
        }

        return false;
    }

    // =========================================================
    // GUARDAR CLIENTE
    // =========================================================

    public boolean guardar(Cliente cliente) {

        /*
         * sp_insert_cliente tiene 7 parámetros:
         *
         * 1 DPI
         * 2 NIT
         * 3 name
         * 4 lastname
         * 5 telephone
         * 6 adress
         * 7 Id_abogado
         */

        String sql =
                "{CALL sp_insert_cliente(?, ?, ?, ?, ?, ?, ?)}";

        if (cliente == null
                || cliente.getDpi() == null
                || cliente.getDpi().trim().isEmpty()) {

            System.err.println(
                    "Error: El DPI del cliente no puede estar vacío."
            );

            return false;
        }

        if (cliente.getAbogado() == null
                || cliente.getAbogado().getIdAbogado() == null
                || cliente.getAbogado().getIdAbogado()
                        .trim().isEmpty()) {

            System.err.println(
                    "Error: El cliente debe tener un abogado asignado."
            );

            return false;
        }

        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt =
                     conn.prepareCall(sql)) {

            stmt.setString(
                    1,
                    cliente.getDpi()
            );

            stmt.setString(
                    2,
                    cliente.getNit()
            );

            stmt.setString(
                    3,
                    cliente.getNombre()
            );

            stmt.setString(
                    4,
                    cliente.getApellido()
            );

            stmt.setString(
                    5,
                    cliente.getTelefono()
            );

            stmt.setString(
                    6,
                    cliente.getDireccion()
            );

            stmt.setString(
                    7,
                    cliente.getAbogado().getIdAbogado()
            );

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {
    System.err.println("========== ERROR AL CREAR CLIENTE ==========");
    e.printStackTrace();
    return false;
}
    }

    // =========================================================
    // ACTUALIZAR CLIENTE
    // =========================================================

    public boolean actualizar(Cliente cliente) {

        /*
         * sp_update_cliente tiene 7 parámetros.
         */

        String sql =
                "{CALL sp_update_cliente(?, ?, ?, ?, ?, ?, ?)}";

        if (cliente == null
                || cliente.getDpi() == null
                || cliente.getDpi().trim().isEmpty()) {

            System.err.println(
                    "Error: El DPI del cliente es obligatorio."
            );

            return false;
        }

        if (cliente.getAbogado() == null
                || cliente.getAbogado().getIdAbogado() == null
                || cliente.getAbogado().getIdAbogado()
                        .trim().isEmpty()) {

            System.err.println(
                    "Error: El cliente debe tener un abogado."
            );

            return false;
        }

        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt =
                     conn.prepareCall(sql)) {

            stmt.setString(
                    1,
                    cliente.getDpi()
            );

            stmt.setString(
                    2,
                    cliente.getNit()
            );

            stmt.setString(
                    3,
                    cliente.getNombre()
            );

            stmt.setString(
                    4,
                    cliente.getApellido()
            );

            stmt.setString(
                    5,
                    cliente.getTelefono()
            );

            stmt.setString(
                    6,
                    cliente.getDireccion()
            );

            stmt.setString(
                    7,
                    cliente.getAbogado().getIdAbogado()
            );

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar cliente: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // ELIMINAR CLIENTE
    // =========================================================

    public boolean eliminar(String dpi) {

        String sql =
                "{CALL sp_delete_cliente(?)}";

        try (Connection conn = Conexion.getConnection();
             CallableStatement stmt =
                     conn.prepareCall(sql)) {

            stmt.setString(1, dpi);

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "Error al eliminar cliente: "
                    + e.getMessage()
            );

            return false;
        }
    }
}