package controller;

import classes.Cliente;
import classes.ReporteMesa;
import classes.Usuario;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import oasiscrud.oasisrimbd;


public class AsistenciaController implements Initializable {

    @FXML
    ComboBox tmcombo,tacombo;
    @FXML
    TableColumn<ReporteMesa, String> nombre, cedula, contrato, plan, invitados, fecha;
    @FXML
    TableView table;
    SearchController menu;
    Usuario user;
    Cliente client;
    String host;
    @FXML AnchorPane aux,main;
    AsistenciaController myController;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
    }

    public void setACombos() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<String> years = new ArrayList<>();
        ArrayList<ReporteMesa> list = inter.getOpenTables();
        for (ReporteMesa asist : list) {
            if (asist.getContrato().equals(client.getContrato())) {
                if (!years.contains(Integer.toString(LocalDate.parse(asist.getFecha()).getYear()))) {
                    years.add(Integer.toString(LocalDate.parse(asist.getFecha()).getYear()));
                }
            }
        }
        for (int i = 0; i < years.size(); i++) {
            tacombo.getItems().add(years.get(i));
        }
        tacombo.getSelectionModel().selectFirst();
        updateTMCombo();
    }


    public void updateTMCombo() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<ReporteMesa> list = inter.getOpenTables();
        int[] montBool = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] montsNames = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        for (ReporteMesa asist : list) {
            if (asist.getContrato().equals(client.getContrato())) {
                if (LocalDate.parse(asist.getFecha()).getYear() - Integer.parseInt((String) tacombo.getSelectionModel().getSelectedItem()) == 0) {
                    montBool[LocalDate.parse(asist.getFecha()).getMonthValue()] = 1;
                }
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

    @FXML
    public void updateTable() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        if (!tmcombo.getSelectionModel().isEmpty()) {
            table.getItems().clear();
            ArrayList<ReporteMesa> list = inter.getOpenTables();
            String[] montsNames = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
            int mont = 0;
            for (int i = 1; i < montsNames.length; i++) {
                if (tmcombo.getSelectionModel().getSelectedItem().equals(montsNames[i])) {
                    mont = i;
                }
            }
            for (ReporteMesa asist : list) {
                if (asist.getContrato().equals(client.getContrato())) {
                    if (LocalDate.parse(asist.getFecha()).getMonthValue() - mont == 0) {
                        table.getItems().add(asist);
                    }
                }
            }
        }
    }

    public void initTable() {
        nombre.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        invitados.setCellValueFactory(new PropertyValueFactory<>("invitados"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    }

    public void initChart() {

    }

    @FXML
    public void back() {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }
    
    public void modificarAperturaMesa() throws IOException, RemoteException, NotBoundException{
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");

        if (!table.getSelectionModel().isEmpty()) {
            ReporteMesa report = (ReporteMesa) table.getSelectionModel().getSelectedItem();
                    aux.setVisible(false);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/openTable.fxml"));
                    OpenTableController controller;
                    AnchorPane pan = loader.load();
                    aux.getChildren().add(pan);
                    controller = loader.getController();
                    controller.asistenciaController = myController;
                    controller.user = this.user;
                    controller.host = this.host;
                    controller.myController = controller;
                    controller.report=report;
                    controller.initUpdateData();
                    try {
                        controller.initData();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    aux.toFront();
                    aux.setVisible(true);
                    main.setVisible(false);
        }
        table.getSelectionModel().clearSelection();
        
    }

}
