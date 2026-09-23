package com.jurisPro.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ClienteController {

@FXML
private Label lblNombreCliente;

@FXML
private Label lblNombreAbogado;

@FXML
private Label lblEstadoCaso;

@FXML
private Label lblDescripcionCaso;

@FXML
private Button btnLogout;

// Usuario que inició sesión
private String usuario;

/**
 * Recibe el usuario que inició sesión.
 */
public void setUsuario(String usuario) {
    this.usuario = usuario;

    // Por ahora mostramos el usuario.
    // Posteriormente aquí consultaremos la base de datos.
    lblNombreCliente.setText(usuario);

    lblNombreAbogado.setText("Abogado asignado");
    lblEstadoCaso.setText("En proceso");
    lblDescripcionCaso.setText(
            "Información del estado actual del caso."
    );
}


}