package com.jurisPro.system.controller;

import com.jurisPro.system.model.Abogado;
import com.jurisPro.system.model.Cliente;
import com.jurisPro.system.model.Empresas;
import com.jurisPro.system.repository.AbogadoRepository;
import com.jurisPro.system.repository.CasoRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

/**
 * Controlador del portal compartido por Clientes y Empresas.
 */
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

    private final AbogadoRepository abogadoRepository = new AbogadoRepository();
    private final CasoRepository casoRepository = new CasoRepository();

    private Cliente cliente;
    private Empresas empresa;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.empresa = null;

        if (cliente == null) return;

        String nombreCompleto =
                (cliente.getNombre() == null ? "" : cliente.getNombre())
                + " "
                + (cliente.getApellido() == null ? "" : cliente.getApellido());

        lblNombreCliente.setText(nombreCompleto.trim());
        cargarInformacionAbogado(cliente.getAbogado());
        cargarCasoCliente(cliente.getDpi(),
                cliente.getAbogado() == null ? null : cliente.getAbogado().getIdAbogado());
    }

    /** Recibe una empresa autenticada y utiliza exactamente la misma vista. */
    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
        this.cliente = null;

        if (empresa == null) return;

        lblNombreCliente.setText(
                empresa.getNombre() == null ? "" : empresa.getNombre()
        );

        cargarInformacionAbogado(empresa.getAbogado());
        cargarCasoEmpresa(empresa.getIdEmpresa());
    }

    private void cargarInformacionAbogado(Abogado abogado) {

        if (abogado == null || abogado.getIdAbogado() == null
                || abogado.getIdAbogado().isBlank()) {
            lblNombreAbogado.setText("Sin abogado asignado");
            return;
        }

        Abogado abogadoCompleto =
                abogadoRepository.obtenerPorId(abogado.getIdAbogado());

        if (abogadoCompleto == null) {
            lblNombreAbogado.setText("Abogado no encontrado");
            return;
        }

        String nombre = abogadoCompleto.getName() == null
                ? "" : abogadoCompleto.getName().trim();
        String apellido = abogadoCompleto.getLastname() == null
                ? "" : abogadoCompleto.getLastname().trim();

        String nombreCompleto = (nombre + " " + apellido).trim();

        lblNombreAbogado.setText(
                nombreCompleto.isEmpty() ? "Abogado asignado" : nombreCompleto
        );
    }

    private void cargarCasoCliente(String dpi, String idAbogado) {
        Map<String, String> caso =
                casoRepository.obtenerCasoPorCliente(dpi, idAbogado);
        mostrarCaso(caso);
    }

    private void cargarCasoEmpresa(String idEmpresa) {
        Map<String, String> caso = casoRepository.obtenerCasoPorEmpresa(idEmpresa);
        mostrarCaso(caso);
    }

    private void mostrarCaso(Map<String, String> caso) {

        if (caso == null) {
            lblEstadoCaso.setText("Sin caso registrado");
            lblDescripcionCaso.setText(
                    "Todavía no hay actualizaciones de un caso registradas por el abogado."
            );
            return;
        }

        // El estado que ve el cliente/empresa es EXACTAMENTE el valor
        // que el abogado guardó en Casos.status.
        String estado = caso.get("status");
        lblEstadoCaso.setText(
                estado == null || estado.isBlank() ? "Sin estado" : estado.trim()
        );

        // La descripción de la vista del cliente muestra EXACTAMENTE
        // el último informe guardado por el abogado en Casos.informe.
        String informe = caso.get("informe");
        if (informe == null || informe.isBlank()) {
            lblDescripcionCaso.setText(
                    "Todavía no hay un informe de avance registrado por el abogado."
            );
        } else {
            lblDescripcionCaso.setText(informe.trim());
        }
    }

    /** Mantiene compatibilidad con el método anterior. */
    public void setUsuario(String usuario) {
        lblNombreCliente.setText(usuario == null ? "" : usuario);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/jurisPro/system/view/Login.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo cerrar la sesión");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
