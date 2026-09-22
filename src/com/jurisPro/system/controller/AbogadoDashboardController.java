package com.jurisPro.system.controller;

import com.jurisPro.system.model.Cliente;
import com.jurisPro.system.service.ClienteService;
import com.jurisPro.system.service.CasoService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AbogadoDashboardController {

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnCrearCliente;

    @FXML
    private Button btnGuardarCaso;

    @FXML
    private Button btnEliminarCliente;

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


    private final ClienteService clienteService =
            new ClienteService();

    private final CasoService casoService =
            new CasoService();


    private Cliente clienteSeleccionado;


    @FXML
    public void initialize() {

        cmbEstadoCaso.setItems(
                FXCollections.observableArrayList(
                        "EN PROCESO",
                        "COMPLETO"
                )
        );

        configurarTabla();

        cargarClientes();

        tblClientes.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, nuevo) -> {

                    if (nuevo != null) {

                        clienteSeleccionado = nuevo;

                        cargarClienteEnFormulario(nuevo);
                    }

                });
    }


    private void configurarTabla() {

        colDpi.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDpi()
                )
        );

        colNombre.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getNombre()
                )
        );

        colApellido.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getApellido()
                )
        );

        colNit.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getNit()
                )
        );

        colTelefono.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getTelefono()
                )
        );
    }


    private void cargarClientes() {

        tblClientes.setItems(
                FXCollections.observableArrayList(
                        clienteService.obtenerClientes()
                )
        );
    }


    private void cargarClienteEnFormulario(Cliente cliente) {

        txtDpi.setText(cliente.getDpi());
        txtNit.setText(cliente.getNit());
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtTelefono.setText(cliente.getTelefono());
        txtDireccion.setText(cliente.getDireccion());
    }


    @FXML
    private void OnCreate(MouseEvent event) {

        clienteSeleccionado = null;

        limpiarFormulario();

        txtDpi.requestFocus();
    }


    @FXML
    private void OnSave(MouseEvent event) {

        if (!validarCampos()) {
            return;
        }


        if (clienteSeleccionado == null) {

            crearCliente();

        } else {

            actualizarCliente();
        }
    }


    private void crearCliente() {

        /*
         * IMPORTANTE:
         * Este ID debe corresponder a un abogado
         * existente en la tabla Abogados.
         */

        String idAbogado = obtenerIdAbogado();


        if (idAbogado == null) {

            mostrarAlerta(
                    "Error",
                    "Abogado no encontrado",
                    "No existe un abogado asociado para crear el caso.",
                    Alert.AlertType.ERROR
            );

            return;
        }


        String estado = cmbEstadoCaso.getValue();

        String informe = txtInformeCaso.getText().trim();

        String fecha = LocalDate.now().toString();

        String descripcion =
                txtNombre.getText().trim()
                + " "
                + txtApellido.getText().trim();


        String idCaso = casoService.crearCaso(
                descripcion,
                estado,
                informe,
                fecha,
                idAbogado
        );


        if (idCaso == null) {

            mostrarAlerta(
                    "Error",
                    "No se pudo crear el caso",
                    "El caso no pudo ser registrado.",
                    Alert.AlertType.ERROR
            );

            return;
        }


        Cliente cliente = new Cliente();

        cliente.setDpi(txtDpi.getText().trim());
        cliente.setNit(txtNit.getText().trim());
        cliente.setNombre(txtNombre.getText().trim());
        cliente.setApellido(txtApellido.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setDireccion(txtDireccion.getText().trim());

        cliente.setIdCaso(idCaso);

        cliente.setIdUsuario(null);


        boolean creado =
                clienteService.crearCliente(cliente);


        if (creado) {

            mostrarAlerta(
                    "Cliente creado",
                    "Operación exitosa",
                    "El cliente fue registrado correctamente.",
                    Alert.AlertType.INFORMATION
            );

            cargarClientes();

            limpiarFormulario();

            clienteSeleccionado = null;

        } else {

            mostrarAlerta(
                    "Error",
                    "No se pudo crear el cliente",
                    "El cliente no pudo ser registrado.",
                    Alert.AlertType.ERROR
            );
        }
    }


    private void actualizarCliente() {

        Cliente cliente = new Cliente();

        cliente.setDpi(txtDpi.getText().trim());
        cliente.setNit(txtNit.getText().trim());
        cliente.setNombre(txtNombre.getText().trim());
        cliente.setApellido(txtApellido.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setDireccion(txtDireccion.getText().trim());

        cliente.setIdCaso(
                clienteSeleccionado.getIdCaso()
        );

        cliente.setIdUsuario(
                clienteSeleccionado.getIdUsuario()
        );


        boolean actualizado =
                clienteService.actualizarCliente(cliente);


        if (actualizado) {

            mostrarAlerta(
                    "Cliente actualizado",
                    "Operación exitosa",
                    "Los datos fueron actualizados correctamente.",
                    Alert.AlertType.INFORMATION
            );

            cargarClientes();

            limpiarFormulario();

            clienteSeleccionado = null;

        } else {

            mostrarAlerta(
                    "Error",
                    "No se pudo actualizar",
                    "No fue posible actualizar el cliente.",
                    Alert.AlertType.ERROR
            );
        }
    }


    @FXML
    private void OnDelete(MouseEvent event) {

        if (clienteSeleccionado == null) {

            mostrarAlerta(
                    "Atención",
                    "Cliente no seleccionado",
                    "Seleccione un cliente de la tabla.",
                    Alert.AlertType.WARNING
            );

            return;
        }


        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmacion.setTitle("Eliminar cliente");

        confirmacion.setHeaderText(
                "¿Desea eliminar este cliente?"
        );

        confirmacion.setContentText(
                clienteSeleccionado.getNombre()
                + " "
                + clienteSeleccionado.getApellido()
        );


        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                == ButtonType.OK) {

            boolean eliminado =
                    clienteService.eliminarCliente(
                            clienteSeleccionado.getDpi()
                    );


            if (eliminado) {

                /*
                 * Como Clientes tiene ON DELETE CASCADE
                 * solamente eliminar el cliente NO elimina
                 * el caso.
                 *
                 * Si quieres eliminar también el caso,
                 * se hace después.
                 */

                if (clienteSeleccionado.getIdCaso() != null) {

                    casoService.eliminarCaso(
                            clienteSeleccionado.getIdCaso()
                    );
                }


                mostrarAlerta(
                        "Cliente eliminado",
                        "Operación exitosa",
                        "El cliente fue eliminado correctamente.",
                        Alert.AlertType.INFORMATION
                );


                cargarClientes();

                limpiarFormulario();

                clienteSeleccionado = null;

            } else {

                mostrarAlerta(
                        "Error",
                        "No se pudo eliminar",
                        "No fue posible eliminar el cliente.",
                        Alert.AlertType.ERROR
                );
            }
        }
    }


    private boolean validarCampos() {

        if (txtDpi.getText().trim().isEmpty()
                || txtNit.getText().trim().isEmpty()
                || txtNombre.getText().trim().isEmpty()
                || txtApellido.getText().trim().isEmpty()
                || txtTelefono.getText().trim().isEmpty()
                || txtDireccion.getText().trim().isEmpty()
                || cmbEstadoCaso.getValue() == null) {

            mostrarAlerta(
                    "Campos incompletos",
                    "Faltan datos",
                    "Complete todos los campos obligatorios.",
                    Alert.AlertType.WARNING
            );

            return false;
        }

        return true;
    }


    private void limpiarFormulario() {

        txtDpi.clear();
        txtNit.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtDireccion.clear();

        cmbEstadoCaso.setValue(null);

        txtInformeCaso.clear();
    }


    private String obtenerIdAbogado() {

        /*
         * CAMBIAR ESTE VALOR por el Id_abogado
         * que tengas en tu base de datos.
         */

        return "a1";
    }


    @FXML
    private void OnLogout(MouseEvent event) {

        try {

            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(
                            getClass().getResource(
                                    "/com/jurisPro/system/view/Login.fxml"
                            )
                    );

            javafx.scene.Parent root = loader.load();

            Stage stage =
                    (Stage) btnLogout.getScene().getWindow();

            stage.setScene(
                    new javafx.scene.Scene(root)
            );

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

            mostrarAlerta(
                    "Error",
                    "No se pudo cerrar sesión",
                    "Ocurrió un error al regresar al login.",
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