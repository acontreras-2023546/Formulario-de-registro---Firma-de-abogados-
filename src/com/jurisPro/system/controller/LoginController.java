package com.jurisPro.system.controller;



import com.jurisPro.system.model.Abogado;
import com.jurisPro.system.model.Cliente;
import com.jurisPro.system.model.Rol;
import static com.jurisPro.system.model.Rol.ABOGADO;
import static com.jurisPro.system.model.Rol.ADMINISTRADOR;
import static com.jurisPro.system.model.Rol.CLIENTE;
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

        if (usuario.isEmpty() || password.isEmpty() || rolSeleccionado == null) {
            mostrarAlerta(
                    "Campos Incompletos",
                    "Atención",
                    "Ingrese usuario, contraseña y seleccione su rol.",
                    Alert.AlertType.WARNING
            );
            return;
        }

        // -----------------------------------------
        // LOGIN DE ABOGADO
        // -----------------------------------------
        if (rolSeleccionado == Rol.ABOGADO) {

            Abogado abogado = abogadoRepository.autenticarAbogado(usuario, password);

            if (abogado == null) {
                mostrarAlerta(
                        "Acceso Denegado",
                        "Credenciales Incorrectas",
                        "El usuario o contraseña del abogado son incorrectos.",
                        Alert.AlertType.ERROR
                );
                return;
            }

            abrirDashboardAbogado(event, abogado);
            return;
        }

        // -----------------------------------------
        // LOGIN DE ADMINISTRADOR
        // -----------------------------------------
        if (rolSeleccionado == Rol.ADMINISTRADOR) {

            Rol rolBD = usuarioRepository.autenticar(usuario, password);

            if (rolBD == Rol.ADMINISTRADOR) {
                abrirDashboard(event, Rol.ADMINISTRADOR);
            } else {
                mostrarAlerta(
                        "Acceso Denegado",
                        "Credenciales Incorrectas",
                        "El usuario o contraseña del administrador son incorrectos.",
                        Alert.AlertType.ERROR
                );
            }

            return;
        }

        // -----------------------------------------
        // LOGIN DE CLIENTE
        // -----------------------------------------
        if (rolSeleccionado == Rol.CLIENTE) {

            Cliente cliente =
                    clienteRepository.autenticarCliente(
                            usuario,
                            password
                    );

            if (cliente != null) {
                abrirDashboardCliente(event, cliente);
                return;
            }

            // Las empresas utilizan el mismo portal/vista que los clientes.
            com.jurisPro.system.model.Empresas empresa =
                    empresaRepository.autenticarEmpresa(
                            usuario,
                            password
                    );

            if (empresa != null) {
                abrirDashboardEmpresa(event, empresa);
                return;
            }

            mostrarAlerta(
                    "Acceso Denegado",
                    "Credenciales Incorrectas",
                    "El usuario o contraseña son incorrectos.",
                    Alert.AlertType.ERROR
            );

            return;
        }
    }

    /**
     * Abre el dashboard del abogado y le pasa
     * el abogado que acaba de iniciar sesión.
     */
    private void abrirDashboardAbogado(ActionEvent event, Abogado abogado) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/jurisPro/system/view/AbogadoDashboard.fxml"
                    )
            );

            Parent root = loader.load();

            // Obtener el controlador del Dashboard
            AbogadoDashboardController controller =
                    loader.getController();

            // Pasar el abogado que inició sesión
            controller.setAbogadoLogueado(abogado);

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException | RuntimeException e) {

            e.printStackTrace();

            mostrarAlerta(
                    "Error",
                    "No se pudo abrir el Dashboard",
                    e.getMessage() == null
                            ? "Error desconocido."
                            : e.getMessage(),
                    Alert.AlertType.ERROR
            );
        }
    }

    /**
     * Abre el dashboard del cliente y le pasa
     * el cliente que acaba de iniciar sesión.
     */
    private void abrirDashboardCliente(
            ActionEvent event,
            Cliente cliente) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/jurisPro/system/view/ClienteView.fxml"
                    )
            );

            Parent root = loader.load();

            ClienteController controller =
                    loader.getController();

            controller.setCliente(cliente);

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException | RuntimeException e) {

            e.printStackTrace();

            mostrarAlerta(
                    "Error",
                    "No se pudo abrir el Dashboard del cliente",
                    e.getMessage() == null
                            ? "Error desconocido."
                            : e.getMessage(),
                    Alert.AlertType.ERROR
            );
        }
    }


    /**
     * Abre la misma vista ClienteView para una empresa.
     */
    private void abrirDashboardEmpresa(
            ActionEvent event,
            com.jurisPro.system.model.Empresas empresa) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/jurisPro/system/view/ClienteView.fxml"
                    )
            );

            Parent root = loader.load();

            ClienteController controller = loader.getController();
            controller.setEmpresa(empresa);

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();

            mostrarAlerta(
                    "Error",
                    "No se pudo abrir el portal",
                    e.getMessage() == null
                            ? "Error desconocido."
                            : e.getMessage(),
                    Alert.AlertType.ERROR
            );
        }
    }

    /**
     * Abre dashboards generales.
     */
    private void abrirDashboard(ActionEvent event, Rol rol) {

    String archivoFXML;

    switch (rol) {

        case ADMINISTRADOR:
            archivoFXML =
                    "/com/jurisPro/system/view/AdminDashboard.fxml";
            break;

        case ABOGADO:
            archivoFXML =
                    "/com/jurisPro/system/view/AbogadoDashboard.fxml";
            break;

        case CLIENTE:
            mostrarAlerta(
                    "Acceso correcto",
                    "Cliente",
                    "El acceso de clientes está autenticado, pero su vista todavía no está disponible.",
                    Alert.AlertType.INFORMATION
            );
            return;

        default:
            return;
    }

    try {

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(archivoFXML));

        Parent root = loader.load();

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();

    } catch (IOException | RuntimeException e) {

        e.printStackTrace();

        mostrarAlerta(
                "Error",
                "No se pudo abrir el Dashboard",
                e.getMessage() == null
                        ? "Error desconocido."
                        : e.getMessage(),
                Alert.AlertType.ERROR
        );
    }
}

    private void mostrarAlerta(
            String titulo,
            String cabecera,
            String contenido,
            Alert.AlertType tipo) {

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}