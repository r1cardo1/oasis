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
    TableColumn<Asistencia, String> nombre, cedula, contrato, plan, invitados, invad, fecha;
    @FXML
    TableView table;
    @FXML
    CategoryAxis xAxis;
    @FXML
    AreaChart chart;
    DataManager dm = new DataManager();
    SearchController menu;
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
        if (!tmcombo.getItems().isEmpty()) {
            tmcombo.getSelectionModel().selectFirst();
        }
        updateTable();
    }

    public void updateTable() throws SQLException {
        if (!tmcombo.getSelectionModel().isEmpty()) {
            table.getItems().clear();
            ResultSet rs = dm.getAsistenciaPorContrato(client.getContrato());
            DataManager dmaux = new DataManager();
            String[] montsNames = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
            int mont = 0;
            for (int i = 1; i < montsNames.length; i++) {
                if (tmcombo.getSelectionModel().getSelectedItem().equals(montsNames[i])) {
                    mont = i;
                }
            }
            while (rs.next()) {
                if (LocalDate.parse(rs.getString("fecha")).getMonthValue() - mont == 0) {
                    table.getItems().add(new Asistencia(rs.getString("num_inv"), rs.getString("fecha"), rs.getString("hora"),
                            client.getCedula(), client.getNombre(), client.getContrato(), client.getPlan(), rs.getString("invad")));
                }
                System.out.println(rs.getString("invad"));
            }
        }
    }

    public void initTable() {
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        invitados.setCellValueFactory(new PropertyValueFactory<>("invitados"));
        invad.setCellValueFactory(new PropertyValueFactory<>("invad"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    }

    public void initChart() {
        
    }

    public void back() {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

}
