
package com.jurisPro.system.controller;

import com.jurisPro.system.model.Abogado;
import com.jurisPro.system.model.Cliente;
import com.jurisPro.system.service.ClienteService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AbogadoDashboardController implements Initializable {

    @FXML
    private TableView<Cliente> tblClientes;

    @FXML
    private TableColumn<Cliente, String> colDpi;

    @FXML
    private TableColumn<Cliente, String> colNombre;

    @FXML
    private TableColumn<Cliente, String> colApellido;

    @FXML
    private TableColumn<Cliente, String> colNit;

    @FXML
    private TableColumn<Cliente, String> colTelefono;

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
    private ComboBox<String> cmbEstadoCaso;

    @FXML
    private TextArea txtInformeCaso;

    private final ClienteService clienteService =
            new ClienteService();

    private Abogado abogadoLogueado;

    private final ObservableList<Cliente> listaClientes =
            FXCollections.observableArrayList();

    private Cliente clienteSeleccionado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        configurarTabla();
        configurarEstadoCaso();

        if (tblClientes != null) {

            tblClientes.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((obs, oldValue, newValue) -> {

                        if (newValue != null) {

                            clienteSeleccionado = newValue;

                            cargarClienteEnFormulario(newValue);
                        }
                    });
        }
    }

    public void setAbogadoLogueado(Abogado abogado) {

        this.abogadoLogueado = abogado;

        if (abogadoLogueado != null) {

            cargarClientes();
        }
    }

    private void configurarEstadoCaso() {

        if (cmbEstadoCaso != null) {

            cmbEstadoCaso.setItems(
                    FXCollections.observableArrayList(
                            "EN PROCESO",
                            "FINALIZADO",
                            "PENDIENTE"
                    )
            );

            cmbEstadoCaso.setValue("EN PROCESO");
        }
    }

    private void configurarTabla() {

        if (colDpi != null) {

            colDpi.setCellValueFactory(
                    data -> new SimpleStringProperty(
                            data.getValue().getDpi()
                    )
            );
        }

        if (colNombre != null) {

            colNombre.setCellValueFactory(
                    data -> new SimpleStringProperty(
                            data.getValue().getNombre()
                    )
            );
        }

        if (colApellido != null) {

            colApellido.setCellValueFactory(
                    data -> new SimpleStringProperty(
                            data.getValue().getApellido()
                    )
            );
        }

        if (colNit != null) {

            colNit.setCellValueFactory(
                    data -> new SimpleStringProperty(
                            data.getValue().getNit()
                    )
            );
        }

        if (colTelefono != null) {

            colTelefono.setCellValueFactory(
                    data -> new SimpleStringProperty(
                            data.getValue().getTelefono()
                    )
            );
        }

        if (tblClientes != null) {

            tblClientes.setItems(listaClientes);
        }
    }

    private void cargarClientes() {

        if (abogadoLogueado == null) {
            return;
        }

        String idAbogado =
                abogadoLogueado.getIdAbogado();

        if (idAbogado == null || idAbogado.isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "El abogado no tiene un ID válido."
            );

            return;
        }

        List<Cliente> clientes =
                clienteService.obtenerClientesPorAbogado(
                        idAbogado
                );

        listaClientes.setAll(clientes);

        if (tblClientes != null) {

            tblClientes.refresh();
        }
    }

    /*
     * IMPORTANTE:
     * El FXML utiliza onMouseClicked.
     * Por eso estos métodos reciben MouseEvent.
     */

    @FXML
    public void OnCreate(MouseEvent event) {

        if (abogadoLogueado == null) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se ha identificado al abogado que inició sesión."
            );

            return;
        }

        String dpi =
                txtDpi.getText().trim();

        String nit =
                txtNit.getText().trim();

        String nombre =
                txtNombre.getText().trim();

        String apellido =
                txtApellido.getText().trim();

        String telefono =
                txtTelefono.getText().trim();

        String direccion =
                txtDireccion.getText().trim();

        if (dpi.isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Validación",
                    "El DPI es obligatorio."
            );

            return;
        }

        if (nit.isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Validación",
                    "El NIT es obligatorio."
            );

            return;
        }

        if (nombre.isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Validación",
                    "El nombre es obligatorio."
            );

            return;
        }

        if (apellido.isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Validación",
                    "El apellido es obligatorio."
            );

            return;
        }

        if (telefono.isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Validación",
                    "El teléfono es obligatorio."
            );

            return;
        }

        if (direccion.isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Validación",
                    "La dirección es obligatoria."
            );

            return;
        }

        Cliente nuevoCliente =
                new Cliente();

        nuevoCliente.setDpi(dpi);
        nuevoCliente.setNit(nit);
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setApellido(apellido);
        nuevoCliente.setTelefono(telefono);
        nuevoCliente.setDireccion(direccion);

        // Asignamos el abogado que inició sesión
        nuevoCliente.setAbogado(abogadoLogueado);

        boolean exito =
                clienteService.crearCliente(
                        nuevoCliente
                );

        if (exito) {

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Cliente creado correctamente."
            );

            cargarClientes();

            limpiarFormulario();

        } else {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo crear el cliente."
            );
        }
    }

    @FXML
    public void OnSave(MouseEvent event) {

        if (clienteSeleccionado == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Atención",
                    "Seleccione un cliente para actualizar."
            );

            return;
        }

        if (abogadoLogueado == null) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se ha identificado al abogado."
            );

            return;
        }

        clienteSeleccionado.setNit(
                txtNit.getText().trim()
        );

        clienteSeleccionado.setNombre(
                txtNombre.getText().trim()
        );

        clienteSeleccionado.setApellido(
                txtApellido.getText().trim()
        );

        clienteSeleccionado.setTelefono(
                txtTelefono.getText().trim()
        );

        clienteSeleccionado.setDireccion(
                txtDireccion.getText().trim()
        );

        // Mantener el abogado actual
        clienteSeleccionado.setAbogado(
                abogadoLogueado
        );

        boolean exito =
                clienteService.actualizarCliente(
                        clienteSeleccionado
                );

        if (exito) {

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Cliente actualizado correctamente."
            );

            cargarClientes();

            limpiarFormulario();

        } else {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo actualizar el cliente."
            );
        }
    }

    @FXML
    public void OnDelete(MouseEvent event) {

        if (clienteSeleccionado == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Atención",
                    "Seleccione un cliente para eliminar."
            );

            return;
        }

        boolean exito =
                clienteService.eliminarCliente(
                        clienteSeleccionado.getDpi()
                );

        if (exito) {

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Cliente eliminado correctamente."
            );

            cargarClientes();

            limpiarFormulario();

        } else {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo eliminar el cliente."
            );
        }
    }

    private void cargarClienteEnFormulario(
            Cliente cliente) {

        if (cliente == null) {
            return;
        }

        txtDpi.setText(
                cliente.getDpi()
        );

        txtNit.setText(
                cliente.getNit()
        );

        txtNombre.setText(
                cliente.getNombre()
        );

        txtApellido.setText(
                cliente.getApellido()
        );

        txtTelefono.setText(
                cliente.getTelefono()
        );

        txtDireccion.setText(
                cliente.getDireccion()
        );

        if (cmbEstadoCaso != null) {

            cmbEstadoCaso.setValue(
                    "EN PROCESO"
            );
        }

        if (txtInformeCaso != null) {

            txtInformeCaso.clear();
        }
    }

    private void limpiarFormulario() {

        if (txtDpi != null) {
            txtDpi.clear();
        }

        if (txtNit != null) {
            txtNit.clear();
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

        if (cmbEstadoCaso != null) {

            cmbEstadoCaso.setValue(
                    "EN PROCESO"
            );
        }

        if (txtInformeCaso != null) {

            txtInformeCaso.clear();
        }

        clienteSeleccionado = null;

        if (tblClientes != null) {

            tblClientes.getSelectionModel()
                    .clearSelection();
        }
    }

    @FXML
    public void OnLogout(MouseEvent event) {

        try {

            URL fxmlUrl =
                    getClass().getResource(
                            "/com/jurisPro/system/view/Login.fxml"
                    );

            if (fxmlUrl == null) {

                fxmlUrl =
                        getClass().getResource(
                                "/com/jurisPro/system/view/LoginView.fxml"
                        );
            }

            if (fxmlUrl == null) {

                throw new IOException(
                        "No se encontró el archivo de Login."
                );
            }

            Parent root =
                    FXMLLoader.load(fxmlUrl);

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        } catch (IOException e) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo cerrar la sesión: "
                    + e.getMessage()
            );
        }
    }

    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alert =
                new Alert(tipo);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}
