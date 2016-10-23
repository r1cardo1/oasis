/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.ReporteMesa;
import classes.Usuario;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author RicardoMarcano
 */
public class ReporteApertMesaController implements Initializable {

    ReportMenuController menu;
    ReporteApertMesaController mycontroller;
    String host;
    @FXML
    private ComboBox<String> cbuser, cbano, type, cbmes;

    @FXML
    private TextField txtfilter;

    @FXML
    private HBox hbdate, hbmes, hbrangedate, hbuser, hbfilter;

    @FXML
    private DatePicker dpdate, dpfrom, dpto;

    @FXML
    TableView table;

    @FXML
    TableColumn<ReporteMesa, String> usuario, cedula, cliente, contrato, plan, fecha, hora, invitados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initTypeCombo();
        initTable();
    }

    public void back(ActionEvent evt) {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

    public void initTypeCombo() {
        type.getItems().addAll("USUARIO",
                "CLIENTE",
                "CONTRATO",
                "CEDULA",
                "MES",
                "FECHA",
                "RANGO DE FECHA",
                "TODO");
        hbuser.setVisible(true);
        type.getSelectionModel().selectFirst();

        type.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observableValue, String old, String neww) {
                changePane(old, neww);
            }
        });
    }

    public void initUserCombo() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        ArrayList<Usuario> users = inter.getUsuarios();
        for (Usuario u : users) {
            cbuser.getItems().add(u.getUsuario());
        }
    }

    public void changePane(String old, String neww) {
        switch (old) {
            case "CLIENTE":
                if (!neww.equals("CONTRATO") || !neww.equals("CEDULA")) {
                    hbfilter.setVisible(false);
                }
                break;
            case "CONTRATO":
                if (!neww.equals("CLIENTE") || !neww.equals("CEDULA")) {
                    hbfilter.setVisible(false);
                }
                break;
            case "CEDULA":
                if (!neww.equals("CONTRATO") || !neww.equals("CLIENTE")) {
                    hbfilter.setVisible(false);
                }
                break;
            case "FECHA":
                hbdate.setVisible(false);
                break;
            case "RANGO DE FECHA":
                hbrangedate.setVisible(false);
                break;
            case "USUARIO":
                hbuser.setVisible(false);
                break;
            case "MES":
                hbmes.setVisible(false);
                break;
        }
        switch (neww) {
            case "CLIENTE":
                if (!old.equals("CONTRATO") || !old.equals("CEDULA")) {
                    hbfilter.setVisible(true);
                }
                break;
            case "CONTRATO":
                if (!old.equals("CLIENTE") || !old.equals("CEDULA")) {
                    hbfilter.setVisible(true);
                }
                break;
            case "CEDULA":
                if (!old.equals("CONTRATO") || !old.equals("CLIENTE")) {
                    hbfilter.setVisible(true);
                }
                break;
            case "FECHA":
                hbdate.setVisible(true);
                break;
            case "RANGO DE FECHA":
                hbrangedate.setVisible(true);
                break;
            case "USUARIO":
                hbuser.setVisible(true);
                break;
            case "MES":
                hbmes.setVisible(true);
                break;
            case "TODAS":
                try {
                    reloadTable();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
        }
    }
    
    public void updateMesCombo() throws RemoteException, NotBoundException{
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        ArrayList<ReporteMesa> reports = inter.getOpenTables();
        String[] meses = {"","Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        int[] mbool = {0,0,0,0,0,0,0,0,0,0,0,0,0};
        if(!reports.isEmpty()){
            for(ReporteMesa r:reports){
                if(LocalDate.parse(r.getFecha()).getYear() == Integer.parseInt(cbano.getSelectionModel().getSelectedItem())){
                    mbool[LocalDate.parse(r.getFecha()).getMonthValue()]=1;
                }
            }
        }
        for(int i=0;i<mbool.length;i++){
            if(i!=0){
                cbmes.getItems().add(meses[i]);
            }
        }
    }
    
    public void initAnoCombo() throws RemoteException, NotBoundException{
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        ArrayList<ReporteMesa> reports = inter.getOpenTables();
        for(ReporteMesa r:reports){
            if(!cbano.getItems().contains(Integer.toString(LocalDate.parse(r.getFecha()).getYear()))){
                cbano.getItems().add(Integer.toString(LocalDate.parse(r.getFecha()).getYear()));
            }
        }
        if(!cbano.getItems().isEmpty()){
            cbano.getSelectionModel().selectFirst();
            updateMesCombo();
        }
        
        cbano.setOnAction((ActionEvent event) -> {
            try {
                updateMesCombo();
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(ReporteApertMesaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void initTable(){
        usuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        cliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        hora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        invitados.setCellValueFactory(new PropertyValueFactory<>("invitados"));
    }

    public void reloadTable() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        ArrayList<ReporteMesa> reports = inter.getOpenTables();
        for(ReporteMesa r:reports){
            table.getItems().add(r);
        }
    }

}
