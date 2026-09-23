package com.jurisPro.system.controller;

import com.jurisPro.system.model.Rol;
import com.jurisPro.system.repository.AbogadoRepository;
import com.jurisPro.system.repository.ClienteRepository;
import com.jurisPro.system.repository.EmpresasRepository;
import com.jurisPro.system.repository.UsuarioRepository;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<Rol> cmbRol;

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final ClienteRepository clienteRepository = new ClienteRepository();
    private final AbogadoRepository abogadoRepository = new AbogadoRepository();
    private final EmpresasRepository empresaRepository = new EmpresasRepository();

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
            mostrarAlerta("Campos Incompletos", "Atención",
                    "Ingrese usuario, contraseña y seleccione su rol.", Alert.AlertType.WARNING);
            return;
        }

        Rol rolAutenticado = autenticarUsuarioSegunRol(usuario, password, rolSeleccionado);
        if (rolAutenticado == null) {
            mostrarAlerta("Acceso Denegado", "Credenciales Incorrectas",
                    "El usuario, contraseña o rol seleccionado son incorrectos.", Alert.AlertType.ERROR);
            return;
        }

        abrirDashboard(event, rolAutenticado);
    }

    private Rol autenticarUsuarioSegunRol(String usuario, String password, Rol rolEsperado) {

   
    if (rolEsperado == Rol.ADMINISTRADOR) {
        Rol rolBD = usuarioRepository.autenticar(usuario, password);

        if (rolBD == Rol.ADMINISTRADOR) {
            return Rol.ADMINISTRADOR;
        }

        return null;
    }

  
    if (rolEsperado == Rol.ABOGADO) {
        if (abogadoRepository.autenticarAbogado(usuario, password)) {
            return Rol.ABOGADO;
        }

        return null;
    }

   
    if (rolEsperado == Rol.CLIENTE) {
        if (clienteRepository.autenticarCliente(usuario, password)
                || empresaRepository.autenticarEmpresa(usuario, password)) {
            return Rol.CLIENTE;
        }

        return null;
    }

    return null;
}

    private void abrirDashboard(ActionEvent event, Rol rol) {
        String archivoFXML;
        switch (rol) {
            case ADMINISTRADOR -> archivoFXML = "/com/jurisPro/system/view/AdminDashboard.fxml";
            case ABOGADO -> archivoFXML = "/com/jurisPro/system/view/AbogadoDashboard.fxml";
            case CLIENTE -> {
                mostrarAlerta("Acceso correcto", "Cliente",
                        "El acceso de clientes está autenticado, pero su vista todavía no está disponible.",
                        Alert.AlertType.INFORMATION);
                return;
            }
            default -> { return; }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el Dashboard",
                    e.getMessage() == null ? "Error desconocido." : e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
