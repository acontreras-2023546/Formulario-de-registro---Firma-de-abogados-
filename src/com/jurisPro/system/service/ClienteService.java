package com.jurisPro.system.service;

import com.jurisPro.system.model.Cliente;
import com.jurisPro.system.repository.ClienteRepository;

import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService() {
        clienteRepository = new ClienteRepository();
    }

    // =========================================================
    // CREAR CLIENTE
    // =========================================================

    public boolean crearCliente(Cliente cliente) {
        return clienteRepository.insertar(cliente);
    }

    // =========================================================
    // OBTENER TODOS LOS CLIENTES
    // =========================================================

    public List<Cliente> obtenerClientes() {
        return clienteRepository.listar();
    }

    // =========================================================
    // OBTENER CLIENTES DE UN ABOGADO
    // =========================================================

    public List<Cliente> obtenerClientesPorAbogado(
            String idAbogado) {

        return clienteRepository.listarPorAbogado(idAbogado);
    }

    // =========================================================
    // ACTUALIZAR CLIENTE
    // =========================================================

    public boolean actualizarCliente(Cliente cliente) {
        return clienteRepository.actualizar(cliente);
    }

    // =========================================================
    // ELIMINAR CLIENTE
    // =========================================================

    public boolean eliminarCliente(String dpi) {
        return clienteRepository.eliminar(dpi);
    }
}