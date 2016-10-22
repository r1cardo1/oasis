/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.ReporteMesa;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private ComboBox cbuser,cbano,type,cbmes;

    @FXML
    private TextField txtfilter;

    @FXML
    private HBox hbdate,hbmes,hbrangedate,hbuser,hbfilter;

    @FXML
    private DatePicker dpdate,dpfrom,dpto;
    
    @FXML TableView table;
    
    @FXML TableColumn<ReporteMesa,String> usuario,cedula,cliente,contrato,plan,fecha,hora,invitados;
    


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void back(ActionEvent evt) {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

}
