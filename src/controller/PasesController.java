package controller;

import classes.Cliente;
import classes.Invitado;
import classes.PrinterOptions;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import oasiscrud.oasisrimbd;

public class PasesController implements Initializable {

    @FXML
    ComboBox tmcombo, tacombo;
    @FXML
    TableColumn<ReporteMesa, String> nombre, cedula, contrato, plan, invitados, fecha;
    @FXML
    TableView table;
    SearchController menu;
    Usuario user;
    Cliente client;
    String host;
    @FXML
    AnchorPane aux, main;
    PasesController myController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
    }

    public void setACombos() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<String> years = new ArrayList<>();
        ArrayList<ReporteMesa> list = inter.getPases();
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
        ArrayList<ReporteMesa> list = inter.getPases();
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
            ArrayList<ReporteMesa> list = inter.getPases();
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

    public void modificaPase() throws IOException, RemoteException, NotBoundException {

        ReporteMesa report = (ReporteMesa) table.getSelectionModel().getSelectedItem();
        aux.setVisible(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GenerarPase.fxml"));
        GenerarPaseController controller;
        AnchorPane pan = loader.load();
        aux.getChildren().add(pan);
        controller = loader.getController();
        controller.paseController = myController;
        controller.user = this.user;
        controller.host = this.host;
        controller.myController = controller;
        controller.report = report;
        controller.initUpdateInvitados();
        controller.initUpdateData();
        controller.update = true;
        try {
            controller.initData();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        aux.toFront();
        aux.setVisible(true);
        main.setVisible(false);

    }

    public void generarRecibo() throws RemoteException, IOException, NumberFormatException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");

        if (!table.getSelectionModel().isEmpty()) {
            ReporteMesa report = (ReporteMesa) table.getSelectionModel().getSelectedItem();
            Cliente client = inter.clientePorContrato(report.getContrato());
            ArrayList<Invitado> inv = inter.getInvitados();
            ArrayList<Invitado> invid = new ArrayList<>();
            for(int i = 0;i<inv.size();i++){                
                if(inv.get(i).getFecha().equals(report.getFecha()) && inv.get(i).getContrato().equals(report.getContrato())){
                    invid.add(inv.get(i));
                }
            }
            PrinterOptions p = new PrinterOptions();
            p.resetAll();
            p.initialize();
            p.feedBack((byte) 2);
            p.color(0);
            p.alignCenter();
            p.setText("Oasis Club C.A");
            p.newLine();
            p.setText("Carretera Kilómetro 7 1/2");
            p.newLine();
            p.setText("Vía la Cañada Sector Camuri.");
            p.newLine();
            p.setText("San francisco, Zulia");
            p.newLine();
            p.addLineSeperator();
            p.alignLeft();
            p.newLine();
            p.setText("Fecha \t\t:" + report.getFecha());
            p.newLine();
            p.setText("Cliente \t:" + client.getNombre());
            p.newLine();
            p.setText("Cedula \t\t:" + client.getCedula());
            p.newLine();
            p.setText("Contrato \t:" + client.getContrato());
            p.newLine();
            p.setText("Plan \t\t:" + client.getPlan());
            p.newLine();
            p.addLineSeperator();
            p.newLine();
            p.alignCenter();
            p.setText(" Articulos ");
            p.newLine();
            p.alignLeft();
            p.addLineSeperator();

            p.newLine();

            p.setText("No \tArt\t\tCant\tPrec");
            p.newLine();
            p.addLineSeperator();
            p.newLine();
            p.setText("1" + "\t" + "Control de" + "\t" + "1" + "\t" + "0");
            p.newLine();
            p.setText("  " + "\t" + "Acceso");
            p.newLine();
            p.setText("  " + "\t" + "(Seguridad)");
            p.newLine();
            p.setText("  " + "\t" + "Invitados: " + (Integer.parseInt(report.getInvitados())+invid.size()));
            p.newLine();
            if (inv.size() > 0) {
                p.setText("1" + "\t" + "Pase Inv Adic" + "\t" + invid.size() + "\t" + inter.precio());
            }
            p.newLine();
            p.addLineSeperator();
            p.newLine();
            p.setText("Precio Total" + "\t" + "\t" + invid.size() * Integer.parseInt(inter.precio()));
            p.newLine();
            p.addLineSeperator();
            p.feed((byte) 3);
            p.finit();
            print(p.finalCommandSet().getBytes());
            System.out.println(p.finalCommandSet());
        }
    }

    public void print(byte[] b) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/selectPrinter.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        SelectPrinterController controller = loader.getController();
        controller.a = b;
        stage.setScene(scene);
        stage.show();
    }

    public void eliminarPase() throws RemoteException, NotBoundException, SQLException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        if(!table.getSelectionModel().isEmpty()){
            ReporteMesa report = (ReporteMesa) table.getSelectionModel().getSelectedItem();
            inter.eliminaPase((ReporteMesa)table.getSelectionModel().getSelectedItem());
            inter.eliminaAsistencia(report.getContrato(),report.getFecha());
            inter.eliminaInvitados(report.getContrato(), report.getFecha());
        }
        updateTable();
    }

    @FXML
    public void back() {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

}
