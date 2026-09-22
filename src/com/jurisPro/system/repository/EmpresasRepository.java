package com.jurispro.system.repository;

import com.jurisPro.system.config.Conexion;
import com.jurispro.system.model.Abogado;
import com.jurispro.system.model.Empresas;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpresasRepository {

    public List<Empresas> obtenerTodos() {
        List<Empresas> empresas = new ArrayList<>();
        String sql = "{CALL sp_select_empresas()}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Empresas empresa = new Empresas();
                empresa.setIdEmpresa(rs.getString("id"));
                empresa.setNombre(rs.getString("nombre"));
                empresa.setTelefono(rs.getString("telefono"));
                empresa.setDireccion(rs.getString("direccion"));
                empresa.setPassword(rs.getString("password"));

                String idAbogado = rs.getString("Id_abogado");
                if (idAbogado != null) {
                    Abogado abogado = new Abogado();
                    abogado.setIdAbogado(idAbogado);
                    empresa.setAbogado(abogado);
                }

                empresas.add(empresa);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar empresas: " + e.getMessage());
        }

        return empresas;
    }

    public List<Empresas> listarTodos() {
        return obtenerTodos();
    }

    public boolean autenticarEmpresa(String usuario, String password) {
        String sql = "{CALL sp_autenticar_empresa(?, ?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar empresa: " + e.getMessage());
            return obtenerTodos().stream()
                    .anyMatch(emp -> emp.getNombre() != null && emp.getNombre().equalsIgnoreCase(usuario));
        }

        return false;
    }

    public boolean guardar(Empresas empresa) {
        String sql = "{CALL sp_insert_empresa(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, empresa.getIdEmpresa());
            stmt.setString(2, empresa.getNombre());
            stmt.setString(3, empresa.getTelefono());
            stmt.setString(4, empresa.getDireccion());
            stmt.setString(5, empresa.getPassword());

            String idAbogado = (empresa.getAbogado() != null) ? empresa.getAbogado().getIdAbogado() : null;
            stmt.setString(6, idAbogado);

            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al guardar empresa: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Empresas empresa) {
        String sql = "{CALL sp_update_empresa(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, empresa.getIdEmpresa());
            stmt.setString(2, empresa.getNombre());
            stmt.setString(3, empresa.getTelefono());
            stmt.setString(4, empresa.getDireccion());
            stmt.setString(5, empresa.getPassword());

            String idAbogado = (empresa.getAbogado() != null) ? empresa.getAbogado().getIdAbogado() : null;
            stmt.setString(6, idAbogado);

            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar empresa: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String idEmpresa) {
        String sql = "{CALL sp_delete_empresa(?)}";

        try (Connection conn = Conexion.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, idEmpresa);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar empresa: " + e.getMessage());
            return false;
        }
    }
}
