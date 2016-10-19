/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Busqueda;
import classes.DataManager;
import classes.Usuario;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Ricardo Marcano
 */
public class SearchReportController implements Initializable {

    @FXML
    AnchorPane main, aux, tUserView, tDateView, tRangeDateView;
    @FXML
    HBox hgDate, hgRangeDate, hgUser;
    @FXML
    ComboBox tcUser, gcMode, mgUser;
    @FXML
    BarChart<String, Integer> barChart;
    @FXML
    CategoryAxis xAxis;
    @FXML
    TableColumn<Busqueda, String> user, tipo, filtro, fecha, hora;
    @FXML
    TableView table;
    DataManager dm = new DataManager();
    Usuario usuario;
    ReportMenuController menu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initCombo();
            first();
            initTable();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void first() throws SQLException {

        ArrayList<String> list = new ArrayList<>();

        ArrayList<Usuario> users = dm.getUsuarios();
        for (Usuario u : users) {
            list.add(u.getUsuario());
        }
        int count;
        int[] searchs = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            count = dm.getSearchByUser(list.get(i));
            searchs[i] = count;
        }
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (int i = 0; i < list.size(); i++) {
            series.getData().add(new XYChart.Data<>(list.get(i), searchs[i]));
        }
        barChart.getData().add(series);
    }

    public void initCombo() throws SQLException {
        tcUser.getItems().addAll("TODOS");
        mgUser.getItems().add("TODOS");
        ResultSet rs;
        ArrayList<Usuario> users = dm.getUsuarios();
        for (Usuario u : users) {
            String str = u.getUsuario();
            tcUser.getItems().add(str);
            mgUser.getItems().add(str);
        }
        mgUser.getSelectionModel().selectFirst();
        tcUser.getSelectionModel().selectFirst();
        gcMode.getItems().addAll("Por Usuario", "Por Fecha", "Por rango de fecha");
        gcMode.getSelectionModel().selectFirst();
        gcMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observableValue, String old, String neww) {
                changePane(old, neww);
            }
        });
        gcMode.getSelectionModel().selectFirst();
        hgUser.setVisible(true);
    }

    public void changePane(String old, String neww) {
        switch (old) {
            case "Por Usuario":
                hgUser.setVisible(false);
                break;
            case "Por Fecha":
                hgDate.setVisible(false);
                break;
            case "Por rango de fecha":
                hgRangeDate.setVisible(false);
                break;
        }
        switch (neww) {
            case "Por Usuario":
                hgUser.setVisible(true);
                break;
            case "Por Fecha":
                hgDate.setVisible(true);
                break;
            case "Por rango de fecha":
                hgRangeDate.setVisible(true);
                break;
        }
    }

    @FXML
    public void action() throws SQLException {
        int count;
        barChart.getData().clear();
        if (!mgUser.getSelectionModel().getSelectedItem().equals("TODOS")) {
            count = dm.getSearchByUser((String) mgUser.getSelectionModel().getSelectedItem());
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>((String) mgUser.getSelectionModel().getSelectedItem(), count));
            barChart.getData().add(series);
        } else {
            ArrayList<String> list = new ArrayList<>();

            ArrayList<Usuario> users = dm.getUsuarios();
            for(Usuario u:users) {
                list.add(u.getUsuario());
            }
            int[] searchs = new int[list.size()];
            
            for (int i = 0; i < list.size(); i++) {
                count = dm.getSearchByUser(list.get(i));                
                    searchs[i] = count;
                
            }
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            for (int i = 0; i < list.size(); i++) {
                series.getData().add(new XYChart.Data<>(list.get(i), searchs[i]));
            }
            barChart.getData().add(series);
        }
    }

    public void initTable() throws SQLException {
        user.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        filtro.setCellValueFactory(new PropertyValueFactory<>("filtro"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        hora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        ArrayList<Busqueda> search = dm.getAllSearch();
        for(Busqueda s:search) {
            table.getItems().add(s);
        }
    }

    public void backAction(ActionEvent evt) {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

    @FXML
    public void taction() throws SQLException {
        table.getItems().clear();
        ArrayList<Busqueda> search = dm.getAllSearch();
        for(Busqueda s:search) {
            if (s.getUsuario().equals(tcUser.getSelectionModel().getSelectedItem())) {
                table.getItems().add(s);
            } else {
                if (tcUser.getSelectionModel().getSelectedItem().equals("TODOS")) {
                    table.getItems().add(s);
                }
            }
        }

    }
}
