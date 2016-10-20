/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.Usuario;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class SearchController implements Initializable {

    MainMenuController menuController;
    @FXML
    TableColumn<Cliente, String> contrato;
    @FXML
    TableColumn<Cliente, String> nombre;
    @FXML
    TableColumn<Cliente, String> cedula;
    @FXML
    TableColumn<Cliente, String> plan;
    @FXML
    TableColumn<Cliente, String> restringido;
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
    SearchController myController;
    String host;

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
        
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        
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
    
    public void keysearch(KeyEvent evt) throws SQLException, RemoteException, NotBoundException{
        if(evt.getCode().equals(KeyCode.ENTER))
            search();
    }

    public void initTable() {
        contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        restringido.setCellValueFactory(new PropertyValueFactory("restringido"));
    }

    public void initCombo() {
        stipo.getItems().addAll("CEDULA", "NOMBRE", "CONTRATO");
        stipo.getSelectionModel().select(1);
    }

    public void setMenuController(MainMenuController c) {
        this.menuController = c;
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

    public void viewAsistencia() throws IOException, SQLException, RemoteException, NotBoundException {
        if (!table.getSelectionModel().isEmpty()) {
            aux.setVisible(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/asistencia.fxml"));
            AsistenciaController controller;
            AnchorPane pan = loader.load();
            aux.getChildren().add(pan);
            controller = loader.getController();
            controller.client = (Cliente) table.getSelectionModel().getSelectedItem();
            controller.user = this.user;
            controller.menu = myController;
            controller.host = this.host;
            controller.setACombos();
            controller.initTable();
            aux.toFront();
            aux.setVisible(true);
            main.setVisible(false);
        }
    }

    public void openTable(ActionEvent evt) throws IOException, SQLException {
        if (!table.getSelectionModel().isEmpty()) {
            aux.setVisible(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/openTable.fxml"));
            OpenTableController controller;
            AnchorPane pan = loader.load();
            aux.getChildren().add(pan);
            controller = loader.getController();
            controller.menu = myController;
            controller.client = (Cliente) table.getSelectionModel().getSelectedItem();
            controller.user = this.user;
            controller.host = this.host;
            try {
                controller.initData();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            aux.toFront();
            aux.setVisible(true);
            main.setVisible(false);
        }

    }

    public void viewInvitados() throws IOException, RemoteException, NotBoundException {
        if (!table.getSelectionModel().isEmpty()) {
            aux.setVisible(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/invitados.fxml"));
            InvitadosController controller;
            AnchorPane pan = loader.load();
            aux.getChildren().add(pan);
            controller = loader.getController();
            controller.client = (Cliente) table.getSelectionModel().getSelectedItem();
            controller.user = this.user;
            controller.menu = myController;
            controller.host = this.host;
            controller.initTable();
            aux.toFront();
            aux.setVisible(true);
            main.setVisible(false);
        }
    }
    
    public void reservar() throws IOException{
          if(!table.getSelectionModel().isEmpty()){
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaReserva.fxml"));
            Parent root = loader.load();
            NuevaReservaController controller = loader.getController();
            controller.client = (Cliente) table.getSelectionModel().getSelectedItem();
            controller.initClient();
            controller.primStage=stage;
            controller.host = this.host;
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            
          }
    }

}
