package controller;

import classes.Asistencia;
import classes.Cliente;

import classes.Usuario;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
import oasiscrud.oasisrimbd;

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

    SearchController menu;
    Usuario user;
    Cliente client;
    String host;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
    }

    public void setACombos() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<String> years = new ArrayList<>();
        ArrayList<Asistencia> list = inter.getAsistenciaPorContrato(client.getContrato());
       for(Asistencia asist : list) {
            if (!years.contains(Integer.toString(LocalDate.parse(asist.getFecha()).getYear()))) {
                years.add(Integer.toString(LocalDate.parse(asist.getFecha()).getYear()));
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

    public void updateGMCombo() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
       oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<Asistencia> list = inter.getAsistenciaPorContrato(client.getContrato());
        int[] montBool = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] montsNames = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        for(Asistencia asist : list) {
            if (LocalDate.parse(asist.getFecha()).getYear() - Integer.parseInt((String) gacombo.getSelectionModel().getSelectedItem()) == 0) {
                montBool[LocalDate.parse(asist.getFecha()).getMonthValue()] = 1;
            }
        }
        for (int i = 0; i < montBool.length; i++) {
            if (montBool[i] == 1) {
                gmcombo.getItems().add(montsNames[i]);
            }
        }
    }

    public void updateTMCombo() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<Asistencia> list = inter.getAsistenciaPorContrato(client.getContrato());
        int[] montBool = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] montsNames = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        for(Asistencia asist:list) {
            if (LocalDate.parse(asist.getFecha()).getYear() - Integer.parseInt((String) tacombo.getSelectionModel().getSelectedItem()) == 0) {
                montBool[LocalDate.parse(asist.getFecha()).getMonthValue()] = 1;
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

    public void updateTable() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
       oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        if (!tmcombo.getSelectionModel().isEmpty()) {
            table.getItems().clear();
            ArrayList<Asistencia> list = inter.getAsistenciaPorContrato(client.getContrato());
            String[] montsNames = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
            int mont = 0;
            for (int i = 1; i < montsNames.length; i++) {
                if (tmcombo.getSelectionModel().getSelectedItem().equals(montsNames[i])) {
                    mont = i;
                }
            }
           for(Asistencia asist:list){
                if (LocalDate.parse(asist.getFecha()).getMonthValue() - mont == 0) {
                    table.getItems().add(new Asistencia(asist.getInvitados(), asist.getFecha(), asist.getHora(),
                            client.getCedula(), client.getNombre(), client.getContrato(), client.getPlan(), asist.getInvad()));
                }                
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
