package controller;

import classes.Cliente;
import classes.Autorizado;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import oasiscrud.oasisrimbd;

public class AutorizadosController implements Initializable {

    @FXML
    ComboBox tmcombo, tacombo;
    @FXML
    TableColumn<Autorizado, String> nombre, cedula, contrato, plan, invitados, fecha, autorizado;
    @FXML
    TableView table;
    @FXML
    AnchorPane main, aux;
    SearchController menu;
    Usuario user;
    Cliente client;
    String host;
    AutorizadosController myController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
    }

    public void setACombos() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<String> years = new ArrayList<>();
        ArrayList<Autorizado> list = inter.getAutorizados();
        for (Autorizado asist : list) {
            if (asist.getContrato().equals(client.getContrato())) {
                if (!years.contains(Integer.toString(LocalDate.parse(asist.getFecha()).getYear()))) {
                    years.add(Integer.toString(LocalDate.parse(asist.getFecha()).getYear()));
                }
            }
        }
        for (int i = 0; i < years.size(); i++) {
            tacombo.getItems().add(years.get(i));
        }
        tacombo.getSelectionModel().selectLast();
        updateTMCombo();
    }

    public void updateTMCombo() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<Autorizado> list = inter.getAutorizados();
        int[] montBool = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] montsNames = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        for (Autorizado asist : list) {
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
            tmcombo.getSelectionModel().selectLast();
        }
        updateTable();
    }

    public void updateTable() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        if (!tmcombo.getSelectionModel().isEmpty()) {
            table.getItems().clear();
            ArrayList<Autorizado> list = inter.getAutorizados();
            String[] montsNames = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
            int mont = 0;
            for (int i = 1; i < montsNames.length; i++) {
                if (tmcombo.getSelectionModel().getSelectedItem().equals(montsNames[i])) {
                    mont = i;
                }
            }
            for (Autorizado asist : list) {
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
        autorizado.setCellValueFactory(new PropertyValueFactory<>("autorizado"));
    }

    public void modificaAutorizado() throws IOException, RemoteException, NotBoundException, SQLException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        if (!table.getSelectionModel().isEmpty()) {
            Autorizado report = (Autorizado) table.getSelectionModel().getSelectedItem();
            if(report.getFecha().equals(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))){
                aux.setVisible(false);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/autorizar.fxml"));
                AutorizarController controller;
                aux.getChildren().add(loader.load());
                controller = loader.getController();
                controller.host = this.host;
                controller.myController = controller;
                controller.report = report;
                controller.update = true;
                controller.auto = myController;
                controller.user = user;
                controller.initUpdateData();
                controller.initDataInvitadosAdicional();
                aux.toFront();
                aux.setVisible(true);
                main.setVisible(false);
            }
        }
        table.getSelectionModel().clearSelection();

    }

    public void reimprimeFactura() {

    }

    public void eliminaAutorizado() throws RemoteException, NotBoundException, SQLException {
         Registry reg = LocateRegistry.getRegistry(host,27019);
         oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
         if(!table.getSelectionModel().isEmpty()){
              Autorizado auto = (Autorizado) table.getSelectionModel().getSelectedItem();
              inter.eliminaInvitados(auto.getContrato(),auto.getFecha());
              inter.eliminaAsistencia(auto.getContrato() ,auto.getFecha());
              inter.eliminaAutorizado(auto.getContrato(),auto.getFecha());
              updateTable();
         }
    }

    public void back() {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

}
