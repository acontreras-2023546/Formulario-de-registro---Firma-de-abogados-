package com.jurisPro.system.controller;

import com.jurisPro.system.model.Rol;
import com.jurisPro.system.repository.UsuarioRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * @author informatica
 */
public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<Rol> cmbRol;

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    @FXML
    public void initialize() {
        cmbRol.setItems(FXCollections.observableArrayList(Rol.values()));
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();
        Rol rolSeleccionado = cmbRol.getValue();

        if (usuario.isEmpty() || password.isEmpty() || rolSeleccionado == null) {
            mostrarAlerta("Campos Incompletos", "Atención", "Por favor ingrese usuario, contraseña y seleccione su rol.", Alert.AlertType.WARNING);
            return;
        }

        // Validación en BD usando el Stored Procedure sp_autenticar_usuario
        Rol rolBD = usuarioRepository.autenticar(usuario, password);

        if (rolBD == null) {
            mostrarAlerta("Acceso Denegado", "Credenciales Incorrectas", "El usuario o la contraseña son incorrectos.", Alert.AlertType.ERROR);
            return;
        }

        // Verifica si el rol elegido en el ComboBox coincide con el asignado en la BD
        if (rolBD != rolSeleccionado) {
            mostrarAlerta("Error de Rol", "Rol No Autorizado", "El rol seleccionado no coincide con el rol de este usuario en la BD.", Alert.AlertType.WARNING);
            return;
        }

        // Confirmación de inicio de sesión exitoso
        mostrarAlerta("Éxito", "Sesión Iniciada", "Se inició sesión con éxito", Alert.AlertType.INFORMATION);
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}