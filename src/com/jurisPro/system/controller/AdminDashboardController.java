package com.jurispro.system.controller;

import com.jurispro.system.model.Abogado;
import com.jurispro.system.model.Cliente;
import com.jurispro.system.model.Empresas;
import com.jurispro.system.repository.AbogadoRepository;
import com.jurispro.system.repository.ClienteRepository;
import com.jurispro.system.repository.EmpresasRepository;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AdminDashboardController implements Initializable {

    // Componentes de la Tabla
    @FXML
    private TableView<Object> tablaUsuarios;
    @FXML
    private TableColumn<Object, String> colId;
    @FXML
    private TableColumn<Object, String> colNombre;
    @FXML
    private TableColumn<Object, String> colRol;
    @FXML
    private TableColumn<Object, String> colTelefono;
    @FXML
    private TableColumn<Object, String> colAbogadoAsignado;

    // Filtro y Controles
    @FXML
    private ComboBox<String> cmbTipoUsuario;
    @FXML
    private ComboBox<String> cmbFormTipo;
    @FXML
    private ComboBox<Abogado> cmbAsignarAbogado;

    // Campos de Texto del Formulario
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
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;

    // Botones
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLogout;

    private final AbogadoRepository abogadoRepo = new AbogadoRepository();
    private final ClienteRepository clienteRepo = new ClienteRepository();
    private final EmpresasRepository empresaRepo = new EmpresasRepository();

    private final ObservableList<Object> listaUsuarios = FXCollections.observableArrayList();
    private Object itemSeleccionado = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        configurarComboAbogados();
        cargarComboTipos();
        cargarComboAbogados();
        cargarDatosTabla();

        if (cmbFormTipo != null) {
            cmbFormTipo.valueProperty().addListener((obs, oldVal, newVal) -> {
                actualizarCamposPorRol(newVal);
            });
        }

        if (tablaUsuarios != null) {
            tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                if (newSel != null) {
                    itemSeleccionado = newSel;
                    cargarFormularioDesdeTabla(newSel);
                } else {
                    limpiarCampos();
                }
            });
        }
    }

    private void configurarTabla() {
        if (colId != null) {
            colId.setCellValueFactory(data -> {
                Object item = data.getValue();
                if (item instanceof Abogado) {
                    return new SimpleStringProperty(((Abogado) item).getIdAbogado());
                }
                if (item instanceof Cliente) {
                    return new SimpleStringProperty(((Cliente) item).getDpi());
                }
                if (item instanceof Empresas) {
                    return new SimpleStringProperty(((Empresas) item).getIdEmpresa());
                }
                return new SimpleStringProperty("");
            });
        }

        if (colNombre != null) {
            colNombre.setCellValueFactory(data -> {
                Object item = data.getValue();
                if (item instanceof Abogado) {
                    Abogado a = (Abogado) item;
                    return new SimpleStringProperty((a.getName() != null ? a.getName() : "") + " " + (a.getLastname() != null ? a.getLastname() : ""));
                }
                if (item instanceof Cliente) {
                    Cliente c = (Cliente) item;
                    return new SimpleStringProperty((c.getNombre() != null ? c.getNombre() : "") + " " + (c.getApellido() != null ? c.getApellido() : ""));
                }
                if (item instanceof Empresas) {
                    return new SimpleStringProperty(((Empresas) item).getNombre());
                }
                return new SimpleStringProperty("");
            });
        }

        if (colRol != null) {
            colRol.setCellValueFactory(data -> {
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

        if (colTelefono != null) {
            colTelefono.setCellValueFactory(data -> {
                Object item = data.getValue();
                if (item instanceof Abogado) {
                    return new SimpleStringProperty(((Abogado) item).getTelephone());
                }
                if (item instanceof Cliente) {
                    return new SimpleStringProperty(((Cliente) item).getTelefono());
                }
                if (item instanceof Empresas) {
                    return new SimpleStringProperty(((Empresas) item).getTelefono());
                }
                return new SimpleStringProperty("");
            });
        }

        if (colAbogadoAsignado != null) {
            colAbogadoAsignado.setCellValueFactory(data -> {
                Object item = data.getValue();
                if (item instanceof Cliente) {
                    Cliente c = (Cliente) item;
                    return new SimpleStringProperty(c.getAbogado() != null ? c.getAbogado().getName() : "N/A");
                }
                if (item instanceof Empresas) {
                    Empresas e = (Empresas) item;
                    return new SimpleStringProperty(e.getAbogado() != null ? e.getAbogado().getName() : "N/A");
                }
                return new SimpleStringProperty("N/A");
            });
        }
    }

    private void actualizarCamposPorRol(String tipo) {
        if (tipo == null) {
            return;
        }

        boolean esAbogado = "Abogado".equalsIgnoreCase(tipo);
        boolean esCliente = "Cliente".equalsIgnoreCase(tipo);
        boolean esEmpresa = "Empresa".equalsIgnoreCase(tipo);

        if (txtDpi != null) {
            txtDpi.setDisable(esAbogado || esEmpresa);
            if (esAbogado || esEmpresa) {
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

        if (txtPassword != null) {
            txtPassword.setDisable(esCliente);
            if (esCliente) {
                txtPassword.clear();
            }
        }
        if (txtConfirmPassword != null) {
            txtConfirmPassword.setDisable(esCliente);
            if (esCliente) {
                txtConfirmPassword.clear();
            }
        }
    }

    private void configurarComboAbogados() {
        if (cmbAsignarAbogado != null) {
            cmbAsignarAbogado.setConverter(new StringConverter<Abogado>() {
                @Override
                public String toString(Abogado abogado) {
                    return abogado != null ? abogado.getName() + " " + (abogado.getLastname() != null ? abogado.getLastname() : "") : "";
                }

                @Override
                public Abogado fromString(String string) {
                    return null;
                }
            });
        }
    }

    private void cargarComboTipos() {
        ObservableList<String> tipos = FXCollections.observableArrayList("Abogado", "Cliente", "Empresa");
        if (cmbTipoUsuario != null) {
            cmbTipoUsuario.setItems(tipos);
        }
        if (cmbFormTipo != null) {
            cmbFormTipo.setItems(tipos);
        }
    }

    private void cargarComboAbogados() {
        if (cmbAsignarAbogado != null) {
            cmbAsignarAbogado.setItems(FXCollections.observableArrayList(abogadoRepo.obtenerTodos()));
        }
    }

    private void cargarDatosTabla() {
        listaUsuarios.clear();
        listaUsuarios.addAll(abogadoRepo.obtenerTodos());
        listaUsuarios.addAll(clienteRepo.obtenerTodos());
        listaUsuarios.addAll(empresaRepo.obtenerTodos());
        if (tablaUsuarios != null) {
            tablaUsuarios.setItems(listaUsuarios);
        }
    }

    private void cargarFormularioDesdeTabla(Object item) {
        limpiarCamposSinResetSeleccion();
        itemSeleccionado = item;

        if (item instanceof Abogado) {
            Abogado a = (Abogado) item;
            if (cmbFormTipo != null) {
                cmbFormTipo.setValue("Abogado");
            }
            if (txtDpi != null) {
                txtDpi.setText(a.getIdAbogado());
            }
            if (txtNombre != null) {
                txtNombre.setText(a.getName());
            }
            if (txtApellido != null) {
                txtApellido.setText(a.getLastname());
            }
            if (txtTelefono != null) {
                txtTelefono.setText(a.getTelephone());
            }
            if (txtPassword != null) {
                txtPassword.setText(a.getPassword());
            }

        } else if (item instanceof Cliente) {
            Cliente c = (Cliente) item;
            if (cmbFormTipo != null) {
                cmbFormTipo.setValue("Cliente");
            }
            if (txtDpi != null) {
                txtDpi.setText(c.getDpi());
            }
            if (txtNombre != null) {
                txtNombre.setText(c.getNombre());
            }
            if (txtApellido != null) {
                txtApellido.setText(c.getApellido());
            }
            if (txtTelefono != null) {
                txtTelefono.setText(c.getTelefono());
            }
            if (txtDireccion != null) {
                txtDireccion.setText(c.getDireccion());
            }
            if (txtNit != null) {
                txtNit.setText(c.getNit());
            }
            if (cmbAsignarAbogado != null) {
                cmbAsignarAbogado.setValue(c.getAbogado());
            }

        } else if (item instanceof Empresas) {
            Empresas e = (Empresas) item;
            if (cmbFormTipo != null) {
                cmbFormTipo.setValue("Empresa");
            }
            if (txtDpi != null) {
                txtDpi.setText(e.getIdEmpresa());
            }
            if (txtNombre != null) {
                txtNombre.setText(e.getNombre());
            }
            if (txtTelefono != null) {
                txtTelefono.setText(e.getTelefono());
            }
            if (txtDireccion != null) {
                txtDireccion.setText(e.getDireccion());
            }
            if (cmbAsignarAbogado != null) {
                cmbAsignarAbogado.setValue(e.getAbogado());
            }
        }
    }

    @FXML
    public void handleCrear(ActionEvent event) {
        String tipo = cmbFormTipo != null ? cmbFormTipo.getValue() : null;
        if (tipo == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Validación", "Seleccione un tipo de registro.");
            return;
        }

        String id = txtDpi != null ? txtDpi.getText().trim() : "";
        String nombre = txtNombre != null ? txtNombre.getText().trim() : "";
        String apellido = txtApellido != null ? txtApellido.getText().trim() : "";
        String telefono = txtTelefono != null ? txtTelefono.getText().trim() : "";
        String pass = txtPassword != null ? txtPassword.getText() : "";
        String confirmPass = txtConfirmPassword != null ? txtConfirmPassword.getText() : "";

        if (nombre.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Validación", "El campo nombre es obligatorio.");
            return;
        }

        if ("Abogado".equalsIgnoreCase(tipo) || "Empresa".equalsIgnoreCase(tipo)) {
            if (!pass.isEmpty() && !pass.equals(confirmPass)) {
                mostrarAlerta(Alert.AlertType.ERROR, "Validación", "Las contraseñas no coinciden.");
                return;
            }
        }

        boolean exito = false;

        if ("Abogado".equalsIgnoreCase(tipo)) {
            // Lectura dinámica del UUID de socio
            String idSocio = abogadoRepo.obtenerPrimerIdSocio();
            if (idSocio == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No existe ningún registro en la tabla 'socio' de la BD.");
                return;
            }

            Abogado nuevo = new Abogado();
            nuevo.setIdAbogado(id);
            nuevo.setName(nombre);
            nuevo.setLastname(apellido);
            nuevo.setTelephone(telefono);
            nuevo.setPassword(pass);
            nuevo.setIdSocio(idSocio);

            exito = abogadoRepo.guardar(nuevo);

        } else if ("Cliente".equalsIgnoreCase(tipo)) {
            Cliente nuevo = new Cliente();
            nuevo.setDpi(id);
            nuevo.setNombre(nombre);
            nuevo.setApellido(apellido);
            nuevo.setTelefono(telefono);
            nuevo.setDireccion(txtDireccion != null ? txtDireccion.getText() : "");
            nuevo.setNit(txtNit != null ? txtNit.getText() : "");
            nuevo.setAbogado(cmbAsignarAbogado != null ? cmbAsignarAbogado.getValue() : null);
            exito = clienteRepo.guardar(nuevo);

        } else if ("Empresa".equalsIgnoreCase(tipo)) {
            Empresas nueva = new Empresas();
            nueva.setIdEmpresa(id);
            nueva.setNombre(nombre);
            nueva.setTelefono(telefono);
            nueva.setDireccion(txtDireccion != null ? txtDireccion.getText() : "");
            nueva.setPassword(pass);
            nueva.setAbogado(cmbAsignarAbogado != null ? cmbAsignarAbogado.getValue() : null);
            exito = empresaRepo.guardar(nueva);
        }

        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Registro guardado correctamente.");
            cargarDatosTabla();
            cargarComboAbogados();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el registro.");
        }
    }

    @FXML
    public void handleEditar(ActionEvent event) {
        if (itemSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione un registro para actualizar.");
            return;
        }

        boolean exito = false;
        String nombre = txtNombre != null ? txtNombre.getText().trim() : "";

        if (itemSeleccionado instanceof Abogado) {
            Abogado a = (Abogado) itemSeleccionado;
            a.setName(nombre);
            a.setLastname(txtApellido != null ? txtApellido.getText().trim() : "");
            a.setTelephone(txtTelefono != null ? txtTelefono.getText().trim() : "");
            a.setPassword(txtPassword != null ? txtPassword.getText() : "");
            exito = abogadoRepo.actualizar(a);

        } else if (itemSeleccionado instanceof Cliente) {
            Cliente c = (Cliente) itemSeleccionado;
            c.setNombre(nombre);
            c.setApellido(txtApellido != null ? txtApellido.getText().trim() : "");
            c.setTelefono(txtTelefono != null ? txtTelefono.getText().trim() : "");
            c.setDireccion(txtDireccion != null ? txtDireccion.getText() : "");
            c.setNit(txtNit != null ? txtNit.getText() : "");
            c.setAbogado(cmbAsignarAbogado != null ? cmbAsignarAbogado.getValue() : null);
            exito = clienteRepo.actualizar(c);

        } else if (itemSeleccionado instanceof Empresas) {
            Empresas e = (Empresas) itemSeleccionado;
            e.setNombre(nombre);
            e.setTelefono(txtTelefono != null ? txtTelefono.getText().trim() : "");
            e.setDireccion(txtDireccion != null ? txtDireccion.getText() : "");
            e.setAbogado(cmbAsignarAbogado != null ? cmbAsignarAbogado.getValue() : null);
            exito = empresaRepo.actualizar(e);
        }

        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Registro actualizado correctamente.");
            cargarDatosTabla();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el registro.");
        }
    }

    @FXML
    public void handleEliminar(ActionEvent event) {
        if (itemSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione un registro para eliminar.");
            return;
        }

        boolean exito = false;
        if (itemSeleccionado instanceof Abogado) {
            exito = abogadoRepo.eliminar(((Abogado) itemSeleccionado).getIdAbogado());
        } else if (itemSeleccionado instanceof Cliente) {
            exito = clienteRepo.eliminar(((Cliente) itemSeleccionado).getDpi());
        } else if (itemSeleccionado instanceof Empresas) {
            exito = empresaRepo.eliminar(((Empresas) itemSeleccionado).getIdEmpresa());
        }

        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Registro eliminado correctamente.");
            cargarDatosTabla();
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el registro.");
        }
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/jurispro/system/view/LoginView.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getResource("/com/jurispro/system/view/Login.fxml");
            }
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getResource("LoginView.fxml");
            }

            if (fxmlUrl == null) {
                throw new IOException("No se encontró el archivo FXML de la vista de Login.");
            }

            Parent parent = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Login - JurisPro System");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Navegación", "Error al cargar login: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        limpiarCamposSinResetSeleccion();
        itemSeleccionado = null;
        if (tablaUsuarios != null) {
            tablaUsuarios.getSelectionModel().clearSelection();
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
        if (cmbAsignarAbogado != null) {
            cmbAsignarAbogado.setValue(null);
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
