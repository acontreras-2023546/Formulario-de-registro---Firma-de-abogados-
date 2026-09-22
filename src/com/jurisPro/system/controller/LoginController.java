package com.jurispro.system.controller;

import com.jurisPro.system.model.Rol;
import com.jurispro.system.repository.AbogadoRepository;
import com.jurispro.system.repository.ClienteRepository;
import com.jurispro.system.repository.EmpresasRepository;
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

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<Rol> cmbRol;

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

        // 1. Validar campos vacíos
        if (usuario.isEmpty() || password.isEmpty() || rolSeleccionado == null) {
            mostrarAlerta("Campos Incompletos", "Atención", "Por favor ingrese usuario, contraseña y seleccione su rol.", Alert.AlertType.WARNING);
            return;
        }

        // 2. Validación de credenciales unificada en BD según el Rol
        Rol rolBD = autenticarUsuarioSegunRol(usuario, password, rolSeleccionado);

        if (rolBD == null) {
            mostrarAlerta("Acceso Denegado", "Credenciales Incorrectas", "El usuario, contraseña o rol seleccionado son incorrectos.", Alert.AlertType.ERROR);
            return;
        }

        // 3. Redirección según el rol autenticado
        if (rolBD == Rol.ADMINISTRADOR || rolBD.name().equalsIgnoreCase("ADMIN")) {
            abrirAdminDashboard(event);
        } else {
            mostrarAlerta("Éxito", "Sesión Iniciada", "Bienvenido/a al sistema.", Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Búsqueda flexible según el rol seleccionado por el usuario.
     */
    private Rol autenticarUsuarioSegunRol(String usuario, String password, Rol rolEsperado) {
        // Validación para Administradores o Usuarios generales
        Rol rolAdmin = usuarioRepository.autenticar(usuario, password);
        if (rolAdmin != null && rolAdmin == rolEsperado) {
            return rolAdmin;
        }

        // Validación para Clientes (Busca por DPI o NIT)
        if (rolEsperado == Rol.CLIENTE) {
            boolean existeCliente = clienteRepository.autenticarCliente(usuario, password);
            if (existeCliente) {
                return Rol.CLIENTE;
            }

            boolean existeEmpresa = empresaRepository.autenticarEmpresa(usuario, password);
            if (existeEmpresa) {
                return Rol.CLIENTE;
            }
        }

        // Validación para Abogados (Busca por Nombre / ID)
        if (rolEsperado == Rol.ABOGADO) {
            boolean existeAbogado = abogadoRepository.autenticarAbogado(usuario, password);
            if (existeAbogado) {
                return Rol.ABOGADO;
            }
        }

        return null;
    }

    private void abrirAdminDashboard(ActionEvent event) {
        try {
            String ruta = "/com/jurispro/system/view/AdminDashboard.fxml";
            java.net.URL fxmlLocation = getClass().getResource(ruta);

            if (fxmlLocation == null) {
                System.err.println("=== ERROR DE RUTA ===");
                System.err.println("NO SE ENCONTRÓ EN: " + ruta);

                java.net.URL viewFolder = getClass().getResource("/com/jurispro/system/view/");
                if (viewFolder != null) {
                    System.err.println("La carpeta '/com/jurispro/system/view/' SÍ existe.");
                    java.io.File carpetaView = new java.io.File(viewFolder.toURI());
                    System.err.println("Archivos reales en la carpeta view: " + java.util.Arrays.toString(carpetaView.list()));
                }

                mostrarAlerta("Error", "Ruta no encontrada", "Revisa la consola de NetBeans para ver los archivos encontrados.", Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("JurisPro - Panel de Administración");
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al cargar vista", e.getMessage(), Alert.AlertType.ERROR);
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