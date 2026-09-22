package com.jurisPro.system.service;

import com.jurisPro.system.model.Cliente;
import com.jurisPro.system.repository.ClienteRepository;

import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService() {
        clienteRepository = new ClienteRepository();
    }

    public boolean crearCliente(Cliente cliente) {
        return clienteRepository.insertar(cliente);
    }

    public List<Cliente> obtenerClientes() {
        return clienteRepository.listar();
    }

    public boolean actualizarCliente(Cliente cliente) {
        return clienteRepository.actualizar(cliente);
    }

    public boolean eliminarCliente(String dpi) {
        return clienteRepository.eliminar(dpi);
    }
}