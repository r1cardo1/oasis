/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import classes.Login;
import classes.Usuario;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Ricardo Marcano
 */
public class LoginReportController implements Initializable {

    @FXML
    BarChart<String, Integer> barChart;
    @FXML
    CategoryAxis xAxis;
    @FXML
    ComboBox tcUser, gcMode, mgUser;
    @FXML
    HBox hgDate, hgRangeDate, hgUser;
    @FXML
    TableView table;
    ArrayList<String> users = new ArrayList<>();
    @FXML
    TableColumn<Login, String> nombre, apellido, usuario, fecha, hora;

    ReportMenuController menu;
    int[] logins;
    String host;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initUsers();
            initCombo();
            initTable();
        } catch (SQLException | RemoteException | NotBoundException ex) {
            Logger.getLogger(LoginReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initUsers() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        ArrayList<Usuario> user = inter.getUsuarios();
        user.stream().forEach((u) -> {
            users.add(u.getUsuario());
        });
        logins = new int[users.size()];
        int log;
        for (int i = 0; i < users.size(); i++) {
            log = inter.getLogins(users.get(i));
            logins[i] = log;
        }
        XYChart.Series<String, Integer> series;
        series = new XYChart.Series<>();
        for (int i = 0; i < users.size(); i++) {
            XYChart.Data data = new XYChart.Data<>(users.get(i), logins[i]);
            series.getData().add(data);

        }
        barChart.getData().add(series);

    }

    public void initCombo() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        tcUser.getItems().addAll("TODOS");
        mgUser.getItems().add("TODOS");
        ArrayList<Usuario> user = inter.getUsuarios();
        for (Usuario u : user) {
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
    public void backAction(ActionEvent evt) {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

    public void initTable() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        usuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        hora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        ArrayList<Login> logins = inter.getLogLogins();
        for (Login log : logins) {
            table.getItems().add(log);
        }
    }

    @FXML
    public void taction() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        table.getItems().clear();
        ArrayList<Login> logins = inter.getLogLogins();
        for (Login log : logins) {
            if (log.getUsuario().equals(tcUser.getSelectionModel().getSelectedItem())) {
                table.getItems().add(log);
            } else if (tcUser.getSelectionModel().getSelectedItem().equals("TODOS")) {
                table.getItems().add(log);
            }
        }
    }

    @FXML
    public void action() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        barChart.getData().clear();
        if (!mgUser.getSelectionModel().getSelectedItem().equals("TODOS")) {
            int count = inter.getLogins((String) mgUser.getSelectionModel().getSelectedItem());
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>((String) mgUser.getSelectionModel().getSelectedItem(), count));
            barChart.getData().add(series);
        } else {
            ArrayList<String> list = new ArrayList<>();
            ArrayList<Usuario> users = inter.getUsuarios();
            for (Usuario u : users) {
                list.add(u.getUsuario());
            }
            int[] searchs = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                int count = inter.getLogins(list.get(i));
                searchs[i] = count;
            }
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            for (int i = 0; i < list.size(); i++) {
                series.getData().add(new XYChart.Data<>(list.get(i), searchs[i]));
            }
            barChart.getData().add(series);
        }
    }

}
