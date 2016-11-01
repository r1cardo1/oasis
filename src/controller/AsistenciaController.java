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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

public class AsistenciaController implements Initializable {

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
        Collections.reverse(years);
        for (int i = 0; i < years.size(); i++) {
            tacombo.getItems().add(years.get(i));
        }
        tacombo.getSelectionModel().selectLast();
        updateTMCombo();
    }

    public void updateTMCombo() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<ReporteMesa> list = inter.getOpenTables();
        Integer[] montBool = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] montsNames = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        for (ReporteMesa asist : list) {
            if (asist.getContrato().equals(client.getContrato())) {
                if (LocalDate.parse(asist.getFecha()).getYear() - Integer.parseInt((String) tacombo.getSelectionModel().getSelectedItem()) == 0) {
                    montBool[LocalDate.parse(asist.getFecha()).getMonthValue()] = 1;
                }
            }
        }
        List<String> monts = Arrays.asList(montsNames);
        List<Integer> mont = Arrays.asList(montBool);
        
        
        for (int i = 0; i < mont.size(); i++) {
            if (mont.get(i) == 1) {
                tmcombo.getItems().add(monts.get(i));
            }
        }
        if (!tmcombo.getItems().isEmpty()) {
            tmcombo.getSelectionModel().selectLast();
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
            Collections.reverse(list);
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

    public void modificarAperturaMesa() throws IOException, RemoteException, NotBoundException {
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
        table.getSelectionModel().clearSelection();

    }

    public void imprimeFactura() throws IOException, RemoteException, NotBoundException {
        if(!table.getSelectionModel().isEmpty()){
        int inViejos = 0;
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ReporteMesa report = (ReporteMesa) table.getSelectionModel().getSelectedItem();
        ArrayList<Invitado> list = inter.getInvitados();
        ArrayList<Invitado> table = new ArrayList<>();
        for (Invitado in : list) {
            if (in.getContrato().equals(report.getContrato())) {
                if (in.getFecha().equals(report.getFecha())) {
                    table.add(in);
                }
            }
        }

        PrinterOptions p = new PrinterOptions();
        p.resetAll();
        p.initialize();
        p.feedBack((byte) 2);
        p.color(0);
        p.alignCenter();
        p.setText("Oasis Club S.A");
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
        p.setText("Cliente \t:" + report.getCliente());
        p.newLine();
        p.setText("Cedula \t\t:" + report.getCedula());
        p.newLine();
        p.setText("Contrato \t:" + report.getContrato());
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
        p.setText("1" + "\t" + "Apert. Mesa" + "\t" + "1" + "\t" + "0");
        p.newLine();
        if (table.size() > 0) {
            p.setText("1" + "\t" + "Pase Inv Adic" + "\t" + table.size() + "\t" + (Integer.parseInt(inter.precio()) * table.size()));
            if (inViejos != 0) {
                p.newLine();
                p.setText("1" + "\t" + "Pase Inv Adic" + "\t" + inViejos + "\t" + "-" + (Integer.parseInt(inter.precio()) * inViejos));
                p.newLine();
                p.setText("  " + "\t" + "Pagado");
            }
        }
        p.newLine();
        p.addLineSeperator();
        p.newLine();
        p.setText("Precio Total" + "\t" + "\t" + (table.size() - inViejos) * Integer.parseInt(inter.precio()));
        p.newLine();
        p.addLineSeperator();
        p.feed((byte) 3);
        p.finit();
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
        p.setText("Cliente \t:" + report.getCliente());
        p.newLine();
        p.setText("Cedula \t\t:" + report.getCedula());
        p.newLine();
        p.setText("Contrato \t:" + report.getContrato());
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
        p.setText("  " + "\t" + "Invitados:"+report.getInvitados());
        p.newLine();
        if (table.size() > 0) {
            p.setText("1" + "\t" + "Pase Inv Adic" + "\t" + table.size() + "\t" + (Integer.parseInt(inter.precio()) * table.size()));
            if (inViejos != 0) {
                p.newLine();
                p.setText("1" + "\t" + "Pase Inv Adic" + "\t" + inViejos + "\t" + "-" + (Integer.parseInt(inter.precio()) * inViejos));
                p.newLine();
                p.setText("  " + "\t" + "Pagado");
            }
        }
        p.newLine();
        p.addLineSeperator();
        p.newLine();
        p.setText("Precio Total" + "\t" + "\t" + (table.size() - inViejos) * Integer.parseInt(inter.precio()));
        p.newLine();
        p.addLineSeperator();
        p.feed((byte) 3);
        p.finit();
        System.out.println(p.finalCommandSet());
        print(p.finalCommandSet().getBytes());
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
    
    public void eliminaOpenTable() throws RemoteException, NotBoundException, SQLException{
        if(!table.getSelectionModel().isEmpty()){
            Registry reg = LocateRegistry.getRegistry(host,27019);
            oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
            ReporteMesa report = (ReporteMesa) table.getSelectionModel().getSelectedItem();
            inter.eliminaAsistencia(report.getContrato(),report.getFecha());
            inter.eliminaOpenTable(report.getContrato(),report.getFecha());
            inter.eliminaInvitados(report.getContrato(),report.getFecha());
            updateTable();
        }
    }
}
