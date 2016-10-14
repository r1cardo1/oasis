package controller;

import classes.Asistencia;
import classes.Cliente;
import classes.DataManager;
import classes.Usuario;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AsistenciaController implements Initializable {

    @FXML
    ComboBox tmcombo, gmcombo, gacombo, tacombo;
    @FXML
    TableColumn<Asistencia, String> nombre, cedula, contrato, plan, fecha;
    @FXML
    TableView table;
    @FXML
    CategoryAxis xAxis;
    @FXML
    AreaChart chart;
    DataManager dm = new DataManager();
    ClientMenuController menu;
    Usuario user;
    Cliente client;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
    }

    public void setACombos() throws SQLException {
        ArrayList<String> years = new ArrayList<>();
        ResultSet rs = dm.getAsistenciaPorContrato(client.getContrato());
        while (rs.next()) {
            if (!years.contains(Integer.toString(LocalDate.parse(rs.getString("fecha")).getYear()))) {
                years.add(Integer.toString(LocalDate.parse(rs.getString("fecha")).getYear()));
            }
        }
        for (int i = 0; i < years.size(); i++) {
            tacombo.getItems().add(years.get(i));
            gacombo.getItems().add(years.get(i));
        }
        tacombo.getSelectionModel().selectFirst();
        gacombo.getSelectionModel().selectFirst();
        updateGMCombo();
        updateTMCombo();
    }

    public void updateGMCombo() throws SQLException {
        ResultSet rs = dm.getAsistenciaPorContrato(client.getContrato());
        int[] montBool = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] montsNames = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        while (rs.next()) {
            if (LocalDate.parse(rs.getString("fecha")).getYear() - Integer.parseInt((String) gacombo.getSelectionModel().getSelectedItem()) == 0) {
                montBool[LocalDate.parse(rs.getString("fecha")).getMonthValue()] = 1;
            }
        }
        for (int i = 0; i < montBool.length; i++) {
            if (montBool[i] == 1) {
                gmcombo.getItems().add(montsNames[i]);
            }
        }
    }

    public void updateTMCombo() throws SQLException {
        ResultSet rs = dm.getAsistenciaPorContrato(client.getContrato());
        int[] montBool = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] montsNames = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        while (rs.next()) {
            if (LocalDate.parse(rs.getString("fecha")).getYear() - Integer.parseInt((String) tacombo.getSelectionModel().getSelectedItem()) == 0) {
                montBool[LocalDate.parse(rs.getString("fecha")).getMonthValue()] = 1;
            }
        }
        for (int i = 0; i < montBool.length; i++) {
            if (montBool[i] == 1) {
                tmcombo.getItems().add(montsNames[i]);
            }
        }
    }

    public void updateTable() throws SQLException {
        table.getItems().clear();
        ResultSet rs = dm.getAsistenciaPorContrato(client.getContrato());
        while(rs.next()){
            
        }
    }

    public void initTable() {
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    }

    public void initChart() {

    }

    public void back() {

    }

}
