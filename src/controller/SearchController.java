/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.DataManager;
import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;

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
    DataManager dm = new DataManager();
    Usuario user;
    Stage primStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCombo();
        initTable();
        drag();
    }

    @FXML
    public void focusin(MouseEvent evt) {
        ScaleTransition st = new ScaleTransition();
        st.setNode((Node) evt.getSource());
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setDuration(Duration.millis(60));
        st.play();
    }

    @FXML
    public void focusout(MouseEvent evt) {
        ScaleTransition st = new ScaleTransition();
        st.setNode((Node) evt.getSource());
        st.setFromX(1.2);
        st.setFromY(1.2);
        st.setToX(1);
        st.setToY(1);
        st.setDuration(Duration.millis(60));
        st.play();
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

    @FXML
    public void search(ActionEvent evt) throws SQLException {
        table.getItems().clear();
        table.getSelectionModel().clearSelection();
        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
        String ampm = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
        dm.search(user.getUsuario(), (String) stipo.getSelectionModel().getSelectedItem(), str.getText(), LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                Integer.toString(time.get(Calendar.HOUR) == 0 ? 12 : time.get(Calendar.HOUR))
                + ":" + Integer.toString(time.get(Calendar.MINUTE))
                + ":" + Integer.toString(time.get(Calendar.SECOND))
                + " "+ ampm );

        if (stipo.getSelectionModel().getSelectedItem().equals("CEDULA")) {
            ResultSet rs;
            rs = dm.searchClientbyCI(str.getText().toUpperCase());
            if (rs != null) {
                while (rs.next()) {

                    table.getItems().add(new Cliente(rs.getString("cedula"), rs.getString("nombre"), rs.getString("contrato"), rs.getString("plan"), rs.getString("banco"), rs.getString("restringido")));
                }
            }
        }
        if (stipo.getSelectionModel().getSelectedItem().equals("NOMBRE")) {
            ResultSet rs;
            rs = dm.searchClientbyName(str.getText().toUpperCase());
            if (rs != null) {
                while (rs.next()) {

                    table.getItems().add(new Cliente(rs.getString("cedula"), rs.getString("nombre"), rs.getString("contrato"), rs.getString("plan"), rs.getString("banco"), rs.getString("restringido")));
                }
            }
        }
        if (stipo.getSelectionModel().getSelectedItem().equals("CONTRATO")) {
            ResultSet rs;
            rs = dm.searchClientbyContract(str.getText().toUpperCase());
            if (rs != null) {
                while (rs.next()) {

                    table.getItems().add(new Cliente(rs.getString("cedula"), rs.getString("nombre"), rs.getString("contrato"), rs.getString("plan"), rs.getString("banco"), rs.getString("restringido")));
                }
            }
        }

    }

    @FXML
    public void selectClient(ActionEvent evt) throws IOException {
        if (!table.getSelectionModel().isEmpty()) {
            Cliente c = (Cliente) table.getSelectionModel().getSelectedItem();
            if (c.getRestringido().equals("NO")) {
                Scene scene;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clientMenu.fxml"));
                AnchorPane swap = loader.load();
                ClientMenuController controller;
                controller = loader.getController();
                controller.user = this.user;
                controller.client = (Cliente) table.getSelectionModel().getSelectedItem();
                main.getChildren().clear();
                main.getChildren().add(swap);
                controller.initData();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Alerta");
                alert.setHeaderText(null);
                alert.setContentText("El usuario esta restringido");
                alert.show();
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Alerta");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar un cliente");
            alert.show();
        }

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

        topPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                xs = evt.getSceneX();
                ys = evt.getSceneY();
            }
        });

        topPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primStage.setX(event.getScreenX() - xs);
                primStage.setY(event.getScreenY() - ys);
            }
        });

    }

}
