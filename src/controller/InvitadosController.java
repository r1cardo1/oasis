/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.Invitado;
import classes.Usuario;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class InvitadosController implements Initializable {

    @FXML
    TableColumn<Invitado, String> nombre, apellido, cedula, fecha;
    @FXML
    TableView table;
    @FXML
    HBox hbfield, hbdate, hbrange;
    @FXML
    ComboBox combo;
    @FXML
    DatePicker from, to, date;
    SearchController menu;
    Cliente client;
    Usuario user;
    String host;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void initTable() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        ArrayList<Invitado> list = inter.getInvitados();        
            for(Invitado in:list) {
                if (client.getContrato().equals(in.getContrato()));
                    table.getItems().add(in);
            }
        
    }

    public void exportExcel() {

    }

    public void back() {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

    public void fromDate() {
        final Callback<DatePicker, DateCell> dayCellFactory
                = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(
                                from.getValue().plusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        to.setDayCellFactory(dayCellFactory);
        to.setValue(from.getValue().plusDays(1));
    }

    public void initCombo() {
        combo.getItems().addAll("NOMBRE", "APELLIDO", "CEDULA", "FECHA", "RANGO DE FECHA", "TODOS");
        combo.getSelectionModel().selectFirst();
        combo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observableValue, String old, String neww) {
                changePane(old, neww);
            }
        });
    }

    public void changePane(String old, String neww) {
        switch (old) {
            case "NOMBRE":
                if (!neww.equals("CONTRATO") || !neww.equals("CEDULA")) {
                    hbfield.setVisible(false);
                }
                break;
            case "CONTRATO":
                if (!neww.equals("NOMBRE") || !neww.equals("CEDULA")) {
                    hbfield.setVisible(false);
                }
                break;
            case "CEDULA":
                if (!neww.equals("CONTRATO") || !neww.equals("NOMBRE")) {
                    hbfield.setVisible(false);
                }
                break;
            case "FECHA":
                hbdate.setVisible(false);
                break;
            case "RANGO DE FECHA":
                hbrange.setVisible(false);
                break;
        }
        switch (neww) {
            case "NOMBRE":
                if (!old.equals("CONTRATO") || !old.equals("CEDULA")) {
                    hbfield.setVisible(true);
                }
                break;
            case "CONTRATO":
                if (!old.equals("NOMBRE") || !old.equals("CEDULA")) {
                    hbfield.setVisible(true);
                }
                break;
            case "CEDULA":
                if (!old.equals("CONTRATO") || !old.equals("NOMBRE")) {
                    hbfield.setVisible(true);
                }
                break;
            case "FECHA":
                hbdate.setVisible(true);
                break;
            case "RANGO DE FECHA":
                hbrange.setVisible(true);
                break;
            case "TODAS":
                try {
                    reloadTable();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
        }
    }

    public void reloadTable() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        table.getItems().clear();
        ArrayList<Invitado> list = inter.getInvitados();
       for(Invitado in:list)
             table.getItems().add(in);
    }

}
