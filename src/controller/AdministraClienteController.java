/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.Usuario;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import oasiscrud.oasisrimbd;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class AdministraClienteController implements Initializable {

    AdminMenuController menuController;
    @FXML
    TableColumn<Cliente, String> contrato, nombre, cedula, plan, restringido;
    @FXML
    TextField txtcedula, txtnombre, txtcontrato, txtplan;
    @FXML
    TableView table;
    @FXML
    ComboBox stipo;
    @FXML
    TextField str;
    @FXML
    AnchorPane aux;
    @FXML
    AnchorPane main;
    @FXML
    Label topPane;
    Double xs, ys;
    Usuario user;
    Stage primStage;
    String host;
    AdministraClienteController myController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCombo();
        initTable();
        drag();
    }

    @FXML
    public void minimize(ActionEvent evt) {
        Stage stage;
        Button b = (Button) evt.getSource();
        stage = (Stage) b.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void close(ActionEvent evt) {
        Stage stage;
        Button b = (Button) evt.getSource();
        stage = (Stage) b.getScene().getWindow();
        stage.close();
    }

    public void search() throws SQLException, RemoteException, NotBoundException {

        table.getItems().clear();
        table.getSelectionModel().clearSelection();

        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");

        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
        String ampm = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
        inter.search(user.getUsuario(), (String) stipo.getSelectionModel().getSelectedItem(), str.getText(), LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                Integer.toString(time.get(Calendar.HOUR) == 0 ? 12 : time.get(Calendar.HOUR))
                + ":" + Integer.toString(time.get(Calendar.MINUTE))
                + ":" + Integer.toString(time.get(Calendar.SECOND))
                + " " + ampm);

        if (stipo.getSelectionModel().getSelectedItem().equals("CEDULA")) {
            ArrayList<Cliente> clients = inter.searchClientbyCI(str.getText().toUpperCase());
            if (!clients.isEmpty()) {
                clients.stream().forEach((cli) -> {
                    table.getItems().add(cli);
                });
            }
        }
        if (stipo.getSelectionModel().getSelectedItem().equals("NOMBRE")) {
            ArrayList<Cliente> clients = inter.searchClientbyName(str.getText().toUpperCase());
            if (!clients.isEmpty()) {
                clients.stream().forEach((cli) -> {
                    table.getItems().add(cli);
                });
            }
        }
        if (stipo.getSelectionModel().getSelectedItem().equals("CONTRATO")) {
            ResultSet rs;
            ArrayList<Cliente> client = inter.searchClientbyContract(str.getText().toUpperCase());
            if (!client.isEmpty()) {
                client.stream().forEach((cli) -> {
                    table.getItems().add(cli);
                });
            }
        }

    }

    public void keysearch(KeyEvent evt) throws SQLException, RemoteException, NotBoundException {
        if (evt.getCode().equals(KeyCode.ENTER)) {
            search();
        }
    }

    public void initTable() {
        contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        restringido.setCellValueFactory(new PropertyValueFactory("restringido"));
        restringido.setCellFactory(column -> {
            return new TableCell<Cliente, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem().toString());
                    setGraphic(null);

                    TableRow<Cliente> currentRow = getTableRow();

                    if (!isEmpty()) {

                        if (item.equals("SI")) {
                            currentRow.getStyleClass().clear();
                            currentRow.getStylesheets().add("/css/theme.css");
                            currentRow.getStyleClass().add("restringidos");

                        } else {
                            currentRow.getStyleClass().clear();
                            currentRow.getStylesheets().add("/css/theme.css");
                            currentRow.getStyleClass().add("permitidos");
                        }
                    }
                }
            };
        });
    }

    public void initCombo() {
        stipo.getItems().addAll("CEDULA", "NOMBRE", "CONTRATO");
        stipo.getSelectionModel().select(1);
    }

    public void restringir() throws RemoteException, NotBoundException, SQLException {
        if (!table.getSelectionModel().isEmpty()) {
            Registry reg = LocateRegistry.getRegistry(host, 27019);
            oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
            inter.restringe((Cliente) table.getSelectionModel().getSelectedItem());
            search();
            table.getSelectionModel().clearSelection();
        }
    }

    public void quitarRestriccion() throws SQLException, RemoteException, NotBoundException {
        if (!table.getSelectionModel().isEmpty()) {
            Registry reg = LocateRegistry.getRegistry(host, 27019);
            oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
            inter.quitaRestriccion((Cliente) table.getSelectionModel().getSelectedItem());
            search();
            table.getSelectionModel().clearSelection();
        }
    }

    public void nuevoCliente() throws RemoteException, NotBoundException, SQLException {
        if (!txtcedula.getText().isEmpty()) {
            if (!txtcontrato.getText().isEmpty()) {
                if (!txtnombre.getText().isEmpty()) {
                    if (!txtplan.getText().isEmpty()) {

                        Registry reg = LocateRegistry.getRegistry(host, 27019);
                        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
                        if (!inter.existeCliente(new Cliente("V-" + txtcedula.getText().toUpperCase().replace("V-", ""), txtnombre.getText().toUpperCase(), txtcontrato.getText().toUpperCase(), txtplan.getText().toUpperCase(), ""))) {
                            inter.nuevoCliente(new Cliente("V-" + txtcedula.getText().toUpperCase().replace("V-", ""), txtnombre.getText().toUpperCase(), txtcontrato.getText().toUpperCase(), txtplan.getText().toUpperCase(), ""));
                            search();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Informacion");
                            alert.setContentText("Cliente añadido con exito");
                            alert.show();
                        }else{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setContentText("El Cliente ya existe");
                        alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Todos los campos deben estar llenos");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Todos los campos deben estar llenos");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Todos los campos deben estar llenos");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Todos los campos deben estar llenos");
            alert.show();
        }
    }

    public void eliminaCliente() throws RemoteException, NotBoundException, SQLException {
        if (!table.getSelectionModel().isEmpty()) {
            Registry reg = LocateRegistry.getRegistry(host, 27019);
            oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");

            inter.eliminaCliente((Cliente) table.getSelectionModel().getSelectedItem());
            search();
        }
    }

    public void drag() {

        topPane.setOnMousePressed((MouseEvent evt) -> {
            xs = evt.getSceneX();
            ys = evt.getSceneY();
        });

        topPane.setOnMouseDragged((MouseEvent event) -> {
            primStage.setX(event.getScreenX() - xs);
            primStage.setY(event.getScreenY() - ys);
        });

    }

}
