/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.PaseCortesia;
import classes.Usuario;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import oasiscrud.oasisrimbd;

public class PasesDeCortesiaController implements Initializable {

    @FXML
    TextField filtro, tnombre, tcedula, ttelefono, tplan, tcodigo, tinvitados;
    @FXML
    DatePicker ddfecha, desde, hasta, dfecha;
    @FXML
    TableView<PaseCortesia> tabla;
    @FXML
    TableColumn<PaseCortesia, String> nombre, cedula, telefono, fecha, plan, codigo;
    @FXML
    ComboBox<String> combo;
    @FXML
    HBox hfecha, hfiltro, hrango;
    String host;
    Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configuraTabla();
    }

    public void configuraComboBox() {
        combo.getItems().addAll("TODOS", "NOMBRE", "CEDULA", "FECHA", "RANGO DE FECHA");
        combo.getSelectionModel().selectFirst();
        combo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observableValue, String old, String neww) {
                try {
                    changePane(old, neww);
                } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void configuraTabla() {
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
    }

    public void iniciaDatos() throws RemoteException, NotBoundException {
        tabla.getItems().clear();
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<PaseCortesia> pases = inter.damePasesCortesia();
        for (PaseCortesia p : pases) {
            tabla.getItems().add(p);
        }
    }

    public void desdeFecha() {
        final Callback<DatePicker, DateCell> dayCellFactory
                = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(
                                desde.getValue().plusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        hasta.setDayCellFactory(dayCellFactory);

    }

    public void changePane(String old, String neww) throws RemoteException, NotBoundException {
        switch (old) {
            case "NOMBRE":
                if (!neww.equals("CEDULA")) {
                    hfiltro.setVisible(false);
                }
                break;
            case "CEDULA":
                if (!neww.equals("NOMBRE")) {
                    hfiltro.setVisible(false);
                }
                break;
            case "FECHA":
                hfecha.setVisible(false);
                break;
            case "RANGO DE FECHA":
                hrango.setVisible(false);
                break;
        }
        switch (neww) {
            case "NOMBRE":
                if (!old.equals("CEDULA")) {
                    hfiltro.setVisible(true);
                }
                filtro.setOnKeyReleased((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        try {
                            buscaPorCedula();
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(PasesDeCortesiaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                break;
            case "CEDULA":
                if (!old.equals("NOMBRE")) {
                    hfiltro.setVisible(true);
                }
                filtro.setOnKeyReleased((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        try {
                            buscaPorNombre();
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(PasesDeCortesiaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                break;
            case "FECHA":
                hfecha.setVisible(true);
                break;
            case "RANGO DE FECHA":
                hrango.setVisible(true);
                break;
            case "TODAS":
                iniciaDatos();
                break;

        }
    }

    public void buscaPorCedula() throws RemoteException, NotBoundException {
        tabla.getItems().clear();
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<PaseCortesia> pases = inter.damePasesPorCedula(filtro.getText().toUpperCase());
        pases.forEach((p) -> {
            tabla.getItems().add(p);
        });
    }

    public void buscaPorNombre() throws RemoteException, NotBoundException {
        tabla.getItems().clear();
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<PaseCortesia> pases = inter.damePasesPorNombre(filtro.getText().toUpperCase());
        pases.forEach((p) -> {
            tabla.getItems().add(p);
        });
    }

    public void buscaPorFecha() throws RemoteException, NotBoundException {
        tabla.getItems().clear();
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<PaseCortesia> pases = inter.damePasesPorFecha(dfecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
        pases.forEach((p) -> {
            tabla.getItems().add(p);
        });
    }

    public void buscaPorRangoDeFecha() throws RemoteException, NotBoundException {
        tabla.getItems().clear();
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<PaseCortesia> pases = inter.damePasesPorRangoDeFecha(desde.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE), hasta.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
        pases.forEach((p) -> {
            tabla.getItems().add(p);
        });
    }

    public void nuevoPase() throws RemoteException, NotBoundException {
        if (!tnombre.getText().isEmpty()
                && !tcedula.getText().isEmpty()
                && !ttelefono.getText().isEmpty()
                && !(ddfecha.getValue() == null)
                && !tplan.getText().isEmpty()
                && !tcodigo.getText().isEmpty()) {
            Registry reg = LocateRegistry.getRegistry(host, 27019);
            oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
            inter.nuevoPaseCortesia(new PaseCortesia(tnombre.getText().toUpperCase(),
                    tcedula.getText().toUpperCase(),
                    ttelefono.getText().toUpperCase(),
                    ddfecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    tinvitados.getText().toUpperCase(),
                    usuario.getNombre() + " " + usuario.getApellido(),
                    LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    tcodigo.getText().toUpperCase()));
            iniciaDatos();
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("Informacion");
            a.setHeaderText("Pase guardado con exito");
            a.show();
        } else {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Todos los campos deben estar llenos");
            a.show();
        }

    }

    public void eliminaPase() throws RemoteException, NotBoundException {
        if (!tabla.getSelectionModel().isEmpty()) {
            Alert a = new Alert(AlertType.CONFIRMATION);
            a.setTitle("Confirmar");
            a.setHeaderText("Esta seguro de eliminar el pase seleccionado?");
            Optional<ButtonType> b = a.showAndWait();
            if (b.get() == ButtonType.OK) {
                Registry reg = LocateRegistry.getRegistry(host, 27019);
                oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
                inter.eliminaPaseCortesia(tabla.getSelectionModel().getSelectedItem());
                Alert c = new Alert(AlertType.INFORMATION);
                c.setTitle("Informacion");
                c.setHeaderText("Pase eliminado con exito");
                c.show();
                iniciaDatos();
            }
        }
    }

    public void editaPase() {

    }

    public void generaPase() {

    }

}
