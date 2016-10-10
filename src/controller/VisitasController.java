/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Asistencia;
import classes.DataManager;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class VisitasController implements Initializable {

    @FXML
    TableColumn<Asistencia, String> cedula;
    @FXML
    TableColumn<Asistencia, String> nombre;
    @FXML
    TableColumn<Asistencia, String> contrato;
    @FXML
    TableColumn<Asistencia, String> plan;
    @FXML
    TableColumn<Asistencia, String> fecha;
    @FXML
    TableColumn<Asistencia, String> hora;
    @FXML
    TableColumn<Asistencia, String> nmesa;
    @FXML
    TableColumn<Asistencia, String> ninvitados;
    @FXML
    TableView table;
    @FXML
    ComboBox<String> combo;
    @FXML
    DatePicker from;
    @FXML
    DatePicker to;
    @FXML
    DatePicker date;
    @FXML
    TextField filter;
    @FXML
    Pane rangeDate;
    @FXML
    Pane pdate;
    @FXML
    Pane pfilter;
    DataManager dm = new DataManager();
    DataManager dmaux = new DataManager();
    MainMenuController menu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initTable();
        } catch (SQLException ex) {
            Logger.getLogger(VisitasController.class.getName()).log(Level.SEVERE, null, ex);
        }

        initCombo();
    }

    public void initTable() throws SQLException {
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        hora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        nmesa.setCellValueFactory(new PropertyValueFactory<>("mesa"));
        ninvitados.setCellValueFactory(new PropertyValueFactory<>("invitados"));

        cedula.prefWidthProperty().bind(table.widthProperty().divide(8));
        nombre.prefWidthProperty().bind(table.widthProperty().divide(8));
        contrato.prefWidthProperty().bind(table.widthProperty().divide(8));
        plan.prefWidthProperty().bind(table.widthProperty().divide(8));
        fecha.prefWidthProperty().bind(table.widthProperty().divide(8));
        hora.prefWidthProperty().bind(table.widthProperty().divide(8));
        nmesa.prefWidthProperty().bind(table.widthProperty().divide(8));
        ninvitados.prefWidthProperty().bind(table.widthProperty().divide(8));

        ResultSet rs = dm.visits();
        ResultSet aux;
        Asistencia asist = null;
        while (rs.next()) {
            aux = dmaux.searchClientbyContract(rs.getString("contrato"));
            if (aux.next()) {
                asist = new Asistencia(rs.getString("mesa"), rs.getString("num_inv"), rs.getString("fecha"), rs.getString("hora"),
                        aux.getString("cedula"), aux.getString("nombre"), aux.getString("contrato"), aux.getString("plan"));
            }
            table.getItems().add(asist);

        }
    }

    public void initCombo() {
        combo.getItems().addAll("NOMBRE", "CONTRATO", "CEDULA", "FECHA", "RANGO DE FECHA");
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
                    pfilter.setVisible(false);
                }
                break;
            case "CONTRATO":
                if (!neww.equals("NOMBRE") || !neww.equals("CEDULA")) {
                    pfilter.setVisible(false);
                }
                break;
            case "CEDULA":
                if (!neww.equals("CONTRATO") || !neww.equals("NOMBRE")) {
                    pfilter.setVisible(false);
                }
                break;
            case "FECHA":
                pdate.setVisible(false);
                break;
            case "RANGO DE FECHA":
                rangeDate.setVisible(false);
                break;
        }
        switch (neww) {
            case "NOMBRE":
                if (!old.equals("CONTRATO") || !old.equals("CEDULA")) {
                    pfilter.setVisible(true);
                }
                break;
            case "CONTRATO":
                if (!old.equals("NOMBRE") || !old.equals("CEDULA")) {
                    pfilter.setVisible(true);
                }
                break;
            case "CEDULA":
                if (!old.equals("CONTRATO") || !old.equals("NOMBRE")) {
                    pfilter.setVisible(true);
                }
                break;
            case "FECHA":
                pdate.setVisible(true);
                break;
            case "RANGO DE FECHA":
                rangeDate.setVisible(true);
                break;
        }
    }

    public void backAction(ActionEvent evt) {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
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

}
