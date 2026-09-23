
package com.jurisPro.system.controller;

import com.jurisPro.system.model.Abogado;
import com.jurisPro.system.model.Cliente;
import com.jurisPro.system.model.Empresas;
import com.jurisPro.system.repository.AbogadoRepository;
import com.jurisPro.system.repository.ClienteRepository;
import com.jurisPro.system.repository.EmpresasRepository;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AdminDashboardController implements Initializable {

    // --- ELEMENTOS DE LA VISTA (FXML) ---

    @FXML
    private ComboBox<String> cmbTipoUsuario;

    @FXML
    private TableView<Object> tblGeneral;

    @FXML
    private TableColumn<Object, String> colId;

    @FXML
    private TableColumn<Object, String> colNombre;

    @FXML
    private TableColumn<Object, String> colTipo;

    @FXML
    private TableColumn<Object, String> colAbogado;

    @FXML
    private ComboBox<String> cmbFormTipo;

    @FXML
    private TextField txtDpi;

    @FXML
    private TextField txtNit;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtDireccion;

    @FXML
    private ComboBox<Abogado> cmbAsignarAbogado;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private TextField txtUsuario;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnLogout;

    // --- REPOSITORIOS ---

    private final AbogadoRepository abogadoRepo = new AbogadoRepository();
    private final ClienteRepository clienteRepo = new ClienteRepository();
    private final EmpresasRepository empresaRepo = new EmpresasRepository();

    // --- LISTAS Y ESTADO ---

    private final ObservableList<Object> listaUsuarios =
            FXCollections.observableArrayList();

    private final ObservableList<Abogado> listaAbogados =
            FXCollections.observableArrayList();

    private Object itemSeleccionado = null;

    @Override
public void initialize(URL location, ResourceBundle resources) {

    configurarTabla();
    configurarComboAbogados();
    cargarCombos();
    actualizarTodo();

    if (cmbTipoUsuario != null) {
        cmbTipoUsuario.setOnAction(e -> cargarDatosTabla());
    }

    if (cmbFormTipo != null) {

        cmbFormTipo.valueProperty().addListener(
                (obs, oldVal, newVal) -> actualizarCamposPorRol(newVal)
        );

        // Aplicar las restricciones desde el inicio
        actualizarCamposPorRol(cmbFormTipo.getValue());
    }

    if (tblGeneral != null) {
        tblGeneral.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {

                    if (newSel != null) {
                        itemSeleccionado = newSel;
                        cargarFormularioDesdeTabla(newSel);
                    } else {
                        handleLimpiar(null);
                    }
                });
    }
}

    // =========================================================
    // CONFIGURAR TABLA
    // =========================================================

    private void configurarTabla() {

        if (colId != null) {

            colId.setCellValueFactory(data -> {

                Object item = data.getValue();

                if (item instanceof Abogado) {
                    return new SimpleStringProperty(
                            ((Abogado) item).getIdAbogado()
                    );
                }

                if (item instanceof Cliente) {
                    return new SimpleStringProperty(
                            ((Cliente) item).getDpi()
                    );
                }

                if (item instanceof Empresas) {
                    return new SimpleStringProperty(
                            ((Empresas) item).getIdEmpresa()
                    );
                }

                return new SimpleStringProperty("");
            });
        }

        if (colNombre != null) {

            colNombre.setCellValueFactory(data -> {

                Object item = data.getValue();

                if (item instanceof Abogado) {

                    Abogado a = (Abogado) item;

                    return new SimpleStringProperty(
                            (a.getName() != null ? a.getName() : "")
                            + " "
                            + (a.getLastname() != null ? a.getLastname() : "")
                    );
                }

                if (item instanceof Cliente) {

                    Cliente c = (Cliente) item;

                    return new SimpleStringProperty(
                            (c.getNombre() != null ? c.getNombre() : "")
                            + " "
                            + (c.getApellido() != null ? c.getApellido() : "")
                    );
                }

                if (item instanceof Empresas) {
                    return new SimpleStringProperty(
                            ((Empresas) item).getNombre()
                    );
                }

                return new SimpleStringProperty("");
            });
        }

        if (colTipo != null) {

            colTipo.setCellValueFactory(data -> {

                Object item = data.getValue();

                if (item instanceof Abogado) {
                    return new SimpleStringProperty("Abogado");
                }

                if (item instanceof Cliente) {
                    return new SimpleStringProperty("Cliente");
                }

                if (item instanceof Empresas) {
                    return new SimpleStringProperty("Empresa");
                }

                return new SimpleStringProperty("");
            });
        }

        if (colAbogado != null) {

            colAbogado.setCellValueFactory(data -> {

                Object item = data.getValue();

                if (item instanceof Cliente) {

                    Cliente c = (Cliente) item;

                    if (c.getAbogado() != null) {

                        String nombreCompleto =
                                (c.getAbogado().getName() != null
                                        ? c.getAbogado().getName()
                                        : "")
                                + " "
                                + (c.getAbogado().getLastname() != null
                                        ? c.getAbogado().getLastname()
                                        : "");

                        return new SimpleStringProperty(
                                nombreCompleto.trim().isEmpty()
                                        ? "Sin Nombre"
                                        : nombreCompleto
                        );
                    }

                    return new SimpleStringProperty("Sin Asignar");
                }

                if (item instanceof Empresas) {

                    Empresas e = (Empresas) item;

                    if (e.getAbogado() != null) {

                        String nombreCompleto =
                                (e.getAbogado().getName() != null
                                        ? e.getAbogado().getName()
                                        : "")
                                + " "
                                + (e.getAbogado().getLastname() != null
                                        ? e.getAbogado().getLastname()
                                        : "");

                        return new SimpleStringProperty(
                                nombreCompleto.trim().isEmpty()
                                        ? "Sin Nombre"
                                        : nombreCompleto
                        );
                    }

                    return new SimpleStringProperty("Sin Asignar");
                }

                return new SimpleStringProperty("N/A");
            });
        }
    }

    // =========================================================
    // CAMPOS SEGÚN TIPO
    // =========================================================

    private void actualizarCamposPorRol(String tipo) {

        if (tipo == null) {
            return;
        }

        boolean esAbogado = "Abogado".equalsIgnoreCase(tipo);
        boolean esCliente = "Cliente".equalsIgnoreCase(tipo);
        boolean esEmpresa = "Empresa".equalsIgnoreCase(tipo);

        if (txtDpi != null) {

            txtDpi.setDisable(!esCliente);

            if (!esCliente) {
                txtDpi.clear();
            }
        }

        if (txtApellido != null) {

            txtApellido.setDisable(esEmpresa);

            if (esEmpresa) {
                txtApellido.clear();
            }
        }

        if (txtNit != null) {

            txtNit.setDisable(esAbogado);

            if (esAbogado) {
                txtNit.clear();
            }
        }

        if (txtDireccion != null) {

            txtDireccion.setDisable(esAbogado);

            if (esAbogado) {
                txtDireccion.clear();
            }
        }

        if (cmbAsignarAbogado != null) {

            cmbAsignarAbogado.setDisable(esAbogado);

            if (esAbogado) {
                cmbAsignarAbogado.setValue(null);
            }
        }

        if (txtUsuario != null) {

    txtUsuario.setDisable(false);
}

        if (txtPassword != null) {

            txtPassword.setDisable(false);

            if (esCliente) {
                txtPassword.clear();
            }
        }

        if (txtConfirmPassword != null) {

            txtConfirmPassword.setDisable(false);

            if (esCliente) {
                txtConfirmPassword.clear();
            }
        }
    }

    // =========================================================
    // COMBOBOX DE ABOGADOS
    // =========================================================

    private void configurarComboAbogados() {

        if (cmbAsignarAbogado != null) {

            cmbAsignarAbogado.setConverter(new StringConverter<Abogado>() {

                @Override
                public String toString(Abogado abogado) {

                    if (abogado == null) {
                        return "";
                    }

                    String nombre =
                            abogado.getName() != null
                                    ? abogado.getName()
                                    : "";

                    String apellido =
                            abogado.getLastname() != null
                                    ? abogado.getLastname()
                                    : "";

                    return (nombre + " " + apellido).trim();
                }

                @Override
                public Abogado fromString(String string) {
                    return null;
                }
            });
        }
    }

    // =========================================================
    // COMBOS
    // =========================================================

    private void cargarCombos() {

        ObservableList<String> tiposFiltro =
                FXCollections.observableArrayList(
                        "Todos",
                        "Abogados",
                        "Clientes",
                        "Empresas"
                );

        ObservableList<String> tiposForm =
                FXCollections.observableArrayList(
                        "Abogado",
                        "Cliente",
                        "Empresa"
                );

        if (cmbTipoUsuario != null) {

            cmbTipoUsuario.setItems(tiposFiltro);
            cmbTipoUsuario.setValue("Todos");
        }

        if (cmbFormTipo != null) {

            cmbFormTipo.setItems(tiposForm);
            cmbFormTipo.setValue("Abogado");
        }
    }

    // =========================================================
    // ACTUALIZAR TODO
    // =========================================================

    private void actualizarTodo() {

        cargarComboAbogados();
        cargarDatosTabla();
        handleLimpiar(null);
    }

    private void cargarComboAbogados() {

        if (cmbAsignarAbogado != null) {

            listaAbogados.setAll(
                    abogadoRepo.obtenerTodos()
            );

            cmbAsignarAbogado.setItems(listaAbogados);
        }
    }

    // =========================================================
    // CARGAR TABLA
    // =========================================================

    private void cargarDatosTabla() {

        listaUsuarios.clear();

        String filtro =
                cmbTipoUsuario != null
                        ? cmbTipoUsuario.getValue()
                        : "Todos";

        List<Abogado> listaAbogadosBD =
                abogadoRepo.obtenerTodos();

        if ("Todos".equals(filtro)
                || "Abogados".equals(filtro)) {

            listaUsuarios.addAll(listaAbogadosBD);
        }

        if ("Todos".equals(filtro)
                || "Clientes".equals(filtro)) {

            List<Cliente> clientes =
                    clienteRepo.obtenerTodos();

            for (Cliente c : clientes) {

                if (c.getAbogado() != null
                        && c.getAbogado().getIdAbogado() != null) {

                    String idBuscado =
                            c.getAbogado().getIdAbogado();

                    listaAbogadosBD.stream()
                            .filter(a ->
                                    idBuscado.equals(
                                            a.getIdAbogado()
                                    )
                            )
                            .findFirst()
                            .ifPresent(c::setAbogado);
                }
            }

            listaUsuarios.addAll(clientes);
        }

        if ("Todos".equals(filtro)
                || "Empresas".equals(filtro)) {

            List<Empresas> empresas =
                    empresaRepo.obtenerTodos();

            for (Empresas e : empresas) {

                if (e.getAbogado() != null
                        && e.getAbogado().getIdAbogado() != null) {

                    String idBuscado =
                            e.getAbogado().getIdAbogado();

                    listaAbogadosBD.stream()
                            .filter(a ->
                                    idBuscado.equals(
                                            a.getIdAbogado()
                                    )
                            )
                            .findFirst()
                            .ifPresent(e::setAbogado);
                }
            }

            listaUsuarios.addAll(empresas);
        }

        if (tblGeneral != null) {

            tblGeneral.setItems(listaUsuarios);
            tblGeneral.refresh();
        }
    }

    // =========================================================
    // CARGAR FORMULARIO DESDE TABLA
    // =========================================================

    private void cargarFormularioDesdeTabla(Object item) {

        limpiarCamposSinResetSeleccion();

        itemSeleccionado = item;

        if (item instanceof Abogado) {

            Abogado a = (Abogado) item;

            cmbFormTipo.setValue("Abogado");

            txtDpi.setText(a.getIdAbogado());
            txtNombre.setText(a.getName());
            txtApellido.setText(a.getLastname());
            txtTelefono.setText(a.getTelephone());

            txtUsuario.setText(a.getUsername());
            txtPassword.setText(a.getPassword());
            txtConfirmPassword.setText(a.getPassword());

        } else if (item instanceof Cliente) {

            Cliente c = (Cliente) item;

            cmbFormTipo.setValue("Cliente");

            txtDpi.setText(c.getDpi());
            txtNombre.setText(c.getNombre());
            txtApellido.setText(c.getApellido());
            txtTelefono.setText(c.getTelefono());
            txtDireccion.setText(c.getDireccion());
            txtNit.setText(c.getNit());

            cmbAsignarAbogado.setValue(c.getAbogado());

        } else if (item instanceof Empresas) {

            Empresas e = (Empresas) item;

            cmbFormTipo.setValue("Empresa");

            txtDpi.setText(e.getIdEmpresa());
            txtNombre.setText(e.getNombre());
            txtTelefono.setText(e.getTelefono());
            txtDireccion.setText(e.getDireccion());

            txtPassword.setText(e.getPassword());
            txtConfirmPassword.setText(e.getPassword());

            cmbAsignarAbogado.setValue(e.getAbogado());
        }
    }

    // =========================================================
    // CREAR
    // =========================================================

    @FXML
    public void handleCrear(ActionEvent event) {

        String tipo =
                cmbFormTipo != null
                        ? cmbFormTipo.getValue()
                        : null;

        if (tipo == null) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Validación",
                    "Seleccione un tipo de registro."
            );

            return;
        }

        String id =
                txtDpi != null
                        ? txtDpi.getText().trim()
                        : "";

        String nombre =
                txtNombre != null
                        ? txtNombre.getText().trim()
                        : "";

        String apellido =
                txtApellido != null
                        ? txtApellido.getText().trim()
                        : "";

        String telefono =
                txtTelefono != null
                        ? txtTelefono.getText().trim()
                        : "";

        String pass =
                txtPassword != null
                        ? txtPassword.getText()
                        : "";

        String confirmPass =
                txtConfirmPassword != null
                        ? txtConfirmPassword.getText()
                        : "";

        if (nombre.isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Validación",
                    "El campo nombre es obligatorio."
            );

            return;
        }

        if (!pass.isEmpty()
                && !pass.equals(confirmPass)) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Validación",
                    "Las contraseñas no coinciden."
            );

            return;
        }

        Abogado abogadoAsignado =
                cmbAsignarAbogado != null
                        ? cmbAsignarAbogado.getValue()
                        : null;

        boolean exito = false;

        // =====================================================
        // CREAR ABOGADO
        // =====================================================

        if ("Abogado".equalsIgnoreCase(tipo)) {

            String usuario =
                    txtUsuario != null
                            ? txtUsuario.getText().trim()
                            : "";

            if (usuario.isEmpty()) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Validación",
                        "El nombre de usuario es obligatorio para el abogado."
                );

                return;
            }
            
            if (pass.isEmpty()) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Validación",
                        "La contraseña es obligatoria para el abogado."
                );

                return;
            }

            if (id.isEmpty()) {

                id = java.util.UUID.randomUUID().toString();
            }

            Abogado nuevo = new Abogado();

            nuevo.setIdAbogado(id);
            nuevo.setName(nombre);
            nuevo.setLastname(apellido);
            nuevo.setTelephone(telefono);
            nuevo.setUsername(usuario);
            nuevo.setPassword(pass);

            /*
             * IMPORTANTE:
             * No generamos manualmente un Id_socio.
             * AbogadoRepository.guardar() crea el Socio
             * automáticamente cuando IdSocio está vacío.
             */
            nuevo.setIdSocio(null);

            exito = abogadoRepo.guardar(nuevo);

        // =====================================================
        // CREAR CLIENTE
        // =====================================================

        } else if ("Cliente".equalsIgnoreCase(tipo)) {

            String direccion =
                    txtDireccion != null
                            ? txtDireccion.getText()
                            : "";

            String nit =
                    txtNit != null
                            ? txtNit.getText()
                            : "";

            String passwordFinal =
                    !pass.isEmpty()
                            ? pass
                            : "N/A";

            if (id.isEmpty()) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Validación",
                        "El DPI del cliente es obligatorio."
                );

                return;
            }

            if (abogadoAsignado == null) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Validación",
                        "Debe asignar un abogado al cliente."
                );

                return;
            }

            Cliente nuevoCliente = new Cliente();

            nuevoCliente.setDpi(id);
            nuevoCliente.setNombre(nombre);
            nuevoCliente.setApellido(apellido);
            nuevoCliente.setTelefono(telefono);
            nuevoCliente.setDireccion(direccion);
            nuevoCliente.setNit(nit);
            nuevoCliente.setPassword(passwordFinal);
            nuevoCliente.setAbogado(abogadoAsignado);

            exito = clienteRepo.guardar(nuevoCliente);

        // =====================================================
        // CREAR EMPRESA
        // =====================================================

        } else if ("Empresa".equalsIgnoreCase(tipo)) {

            Empresas nueva = new Empresas();

            nueva.setIdEmpresa(id);
            nueva.setNombre(nombre);
            nueva.setTelefono(telefono);

            nueva.setDireccion(
                    txtDireccion != null
                            ? txtDireccion.getText()
                            : ""
            );

            nueva.setPassword(
                    !pass.isEmpty()
                            ? pass
                            : "N/A"
            );

            nueva.setAbogado(abogadoAsignado);

            exito = empresaRepo.guardar(nueva);
        }

        if (exito) {

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Registro guardado correctamente."
            );

            actualizarTodo();

            if (tblGeneral != null) {
                tblGeneral.refresh();
            }

            handleLimpiar(null);

        } else {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo guardar el registro."
            );
        }
    }

    // =========================================================
    // EDITAR
    // =========================================================

    @FXML
    public void handleEditar(ActionEvent event) {

        if (itemSeleccionado == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Atención",
                    "Seleccione un registro para actualizar."
            );

            return;
        }

        boolean exito = false;

        String nombre =
                txtNombre != null
                        ? txtNombre.getText().trim()
                        : "";

        // =====================================================
        // EDITAR ABOGADO
        // =====================================================

        if (itemSeleccionado instanceof Abogado) {

            Abogado a = (Abogado) itemSeleccionado;

            String usuario =
                    txtUsuario != null
                            ? txtUsuario.getText().trim()
                            : ""; 

            String password =
                    txtPassword != null
                            ? txtPassword.getText()
                            : "";

            if (usuario.isEmpty()) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Validación",
                        "El nombre de usuario es obligatorio."
                );

                return;
            }

            if (password.isEmpty()) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Validación",
                        "La contraseña es obligatoria."
                );

                return;
            }

            a.setName(nombre);

            a.setLastname(
                    txtApellido != null
                            ? txtApellido.getText().trim()
                            : ""
            );


            a.setTelephone(
                    txtTelefono != null
                            ? txtTelefono.getText().trim()
                            : ""
            );

            a.setUsername(usuario);
            a.setPassword(password);

            exito = abogadoRepo.actualizar(a);

        // =====================================================
        // EDITAR CLIENTE
        // =====================================================

        } else if (itemSeleccionado instanceof Cliente) {

            Cliente c = (Cliente) itemSeleccionado;

            c.setNombre(nombre);

            c.setApellido(
                    txtApellido != null
                            ? txtApellido.getText().trim()
                            : ""
            );

            c.setTelefono(
                    txtTelefono != null
                            ? txtTelefono.getText().trim()
                            : ""
            );

            c.setDireccion(
                    txtDireccion != null
                            ? txtDireccion.getText()
                            : ""
            );

            c.setNit(
                    txtNit != null
                            ? txtNit.getText()
                            : ""
            );

            c.setAbogado(
                    cmbAsignarAbogado != null
                            ? cmbAsignarAbogado.getValue()
                            : null
            );

            exito = clienteRepo.actualizar(c);

        // =====================================================
        // EDITAR EMPRESA
        // =====================================================

        } else if (itemSeleccionado instanceof Empresas) {

            Empresas e = (Empresas) itemSeleccionado;

            e.setNombre(nombre);

            e.setTelefono(
                    txtTelefono != null
                            ? txtTelefono.getText().trim()
                            : ""
            );

            e.setDireccion(
                    txtDireccion != null
                            ? txtDireccion.getText()
                            : ""
            );

            e.setAbogado(
                    cmbAsignarAbogado != null
                            ? cmbAsignarAbogado.getValue()
                            : null
            );

            exito = empresaRepo.actualizar(e);
        }

        if (exito) {

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Registro actualizado correctamente."
            );

            actualizarTodo();

        } else {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo actualizar el registro."
            );
        }
    }

    // =========================================================
    // ELIMINAR
    // =========================================================

    @FXML
    public void handleEliminar(ActionEvent event) {

        if (itemSeleccionado == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Atención",
                    "Seleccione un registro para eliminar."
            );

            return;
        }

        boolean exito = false;

        if (itemSeleccionado instanceof Abogado) {

            exito = abogadoRepo.eliminar(
                    ((Abogado) itemSeleccionado).getIdAbogado()
            );

        } else if (itemSeleccionado instanceof Cliente) {

            exito = clienteRepo.eliminar(
                    ((Cliente) itemSeleccionado).getDpi()
            );

        } else if (itemSeleccionado instanceof Empresas) {

            exito = empresaRepo.eliminar(
                    ((Empresas) itemSeleccionado).getIdEmpresa()
            );
        }

        if (exito) {

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Registro eliminado correctamente."
            );

            actualizarTodo();

        } else {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo eliminar el registro."
            );
        }
    }

    // =========================================================
    // CERRAR SESIÓN
    // =========================================================

    @FXML
    public void handleCerrarSesion(ActionEvent event) {

        try {

            URL fxmlUrl =
                    getClass().getResource(
                            "/com/jurisPro/system/view/LoginView.fxml"
                    );

            if (fxmlUrl == null) {

                fxmlUrl =
                        getClass().getResource(
                                "/com/jurisPro/system/view/Login.fxml"
                        );
            }

            if (fxmlUrl == null) {

                fxmlUrl =
                        getClass().getResource(
                                "LoginView.fxml"
                        );
            }

            if (fxmlUrl == null) {

                throw new IOException(
                        "No se encontró el archivo FXML de la vista de Login."
                );
            }

            Parent parent =
                    FXMLLoader.load(fxmlUrl);

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(parent));
            stage.setTitle("Login - JurisPro System");
            stage.show();

        } catch (IOException e) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error de Navegación",
                    "Error al cargar login: "
                    + e.getMessage()
            );
        }
    }

    // =========================================================
    // LIMPIAR
    // =========================================================

    @FXML
    public void handleLimpiar(ActionEvent event) {

        limpiarCamposSinResetSeleccion();

        itemSeleccionado = null;

        if (tblGeneral != null) {

            tblGeneral.getSelectionModel()
                    .clearSelection();
        }
    }

    private void limpiarCamposSinResetSeleccion() {

        if (txtDpi != null) {
            txtDpi.clear();
        }

        if (txtNombre != null) {
            txtNombre.clear();
        }

        if (txtApellido != null) {
            txtApellido.clear();
        }

        if (txtTelefono != null) {
            txtTelefono.clear();
        }

        if (txtDireccion != null) {
            txtDireccion.clear();
        }

        if (txtNit != null) {
            txtNit.clear();
        }

        if (txtPassword != null) {
            txtPassword.clear();
        }

        if (txtConfirmPassword != null) {
            txtConfirmPassword.clear();
        }

        if (txtUsuario != null) {
            txtUsuario.clear();
        }

        if (cmbAsignarAbogado != null) {
            cmbAsignarAbogado.setValue(null);
        }
    }

    // =========================================================
    // ALERTAS
    // =========================================================

    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alert = new Alert(tipo);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}

