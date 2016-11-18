/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.Reserva;
import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SeleccionaClienteController implements Initializable {

    @FXML
    TableView<Cliente> table;
    @FXML
    TableColumn<Cliente, String> nombre, cedula, contrato, plan;
    ArrayList<Cliente> list;
    ReservaController rcontroller;
    String opc;
    Reserva r;
    String host;
    Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void initTable() {
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        list.forEach((c) -> {
            table.getItems().add(c);

        });
    }

    public void selectCliente(ActionEvent evt) throws RemoteException, NotBoundException, IOException {
        if (!table.getSelectionModel().isEmpty()) {
            Cliente c = table.getSelectionModel().getSelectedItem();
            Registry reg = LocateRegistry.getRegistry(host, 27019);
            oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
            switch (opc) {
                case "AUT":
                    if (!inter.abrioReserva(r.getCedula())) {
                        rcontroller.aux.setVisible(false);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/autorizar.fxml"));
                        AutorizarController controller;
                        AnchorPane pan = loader.load();
                        rcontroller.aux.getChildren().add(pan);
                        controller = loader.getController();
                        controller.reserva = rcontroller;
                        controller.client = c;
                        controller.user = this.usuario;
                        controller.host = this.host;
                        controller.myController = controller;
                        try {
                            controller.initData();
                        } catch (NotBoundException | RemoteException | SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                        rcontroller.aux.toFront();
                        rcontroller.aux.setVisible(true);
                        rcontroller.main.setVisible(false);
                    } else {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Error");
                        a.setContentText("Ya el cliente ingreso al club hoy");
                        a.show();
                    }
                    break;
                case "OPN":
                    if (!inter.abrioReserva(r.getCedula())) {
                        rcontroller.aux.setVisible(false);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/openTable.fxml"));
                        OpenTableController controller;
                        AnchorPane pan = loader.load();
                        rcontroller.aux.getChildren().add(pan);
                        controller = loader.getController();
                        controller.reserva = rcontroller;
                        controller.client = c;
                        controller.user = this.usuario;
                        controller.host = this.host;
                        controller.myController = controller;
                        try {
                            controller.initData();
                        } catch (NotBoundException | RemoteException | SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                        rcontroller.aux.toFront();
                        rcontroller.aux.setVisible(true);
                        rcontroller.main.setVisible(false);
                    } else {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Error");
                        a.setContentText("Ya el cliente ingreso al club hoy");
                        a.show();
                    }

                    break;
                case "PAS":
                    if (!inter.abrioReserva(r.getCedula())) {
                        rcontroller.aux.setVisible(false);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GenerarPase.fxml"));
                        GenerarPaseController controller;
                        AnchorPane pan = loader.load();
                        rcontroller.aux.getChildren().add(pan);
                        controller = loader.getController();
                        controller.reserva = rcontroller;
                        controller.client = c;
                        controller.user = this.usuario;
                        controller.host = this.host;
                        controller.myController = controller;
                        try {
                            controller.initData();
                        } catch (NotBoundException | RemoteException | SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                        rcontroller.aux.toFront();
                        rcontroller.aux.setVisible(true);
                        rcontroller.main.setVisible(false);
                    } else {
                        Alert a;
                        a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Error");
                        a.setContentText("Ya el cliente ingreso al club hoy");
                        a.show();
                    }
                    break;
            }
            Button b = (Button) evt.getSource();
            Scene s = b.getScene();
            Stage st = (Stage) s.getWindow();
            st.close();
        }
    }

    public void close(ActionEvent evt) {
        Button b = (Button) evt.getSource();
        Scene s = b.getScene();
        Stage st = (Stage) s.getWindow();
        st.close();

    }

}
