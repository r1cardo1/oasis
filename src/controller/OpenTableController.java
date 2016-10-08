/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.DataManager;
import classes.Usuario;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;


public class OpenTableController implements Initializable {

    @FXML TextField txtnombre;
    @FXML TextField txtcedula;
    @FXML TextField txtcontrato;
    @FXML TextField txtplan;
    @FXML TextField ninvitados;
    @FXML TextField nmesa;
    @FXML DatePicker fecha;
    Cliente client;
    Usuario user;
    DataManager dm = new DataManager();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
    }    
    
    public void initData(){
        txtnombre.setText(client.getNombre());
        txtcedula.setText(client.getCedula());
        txtcontrato.setText(client.getContrato());
        txtplan.setText(client.getPlan());
        fecha.setValue(LocalDate.now());
    }
    
    @FXML public void openTable(ActionEvent evt){
        if(!nmesa.getText().isEmpty() && !ninvitados.getText().isEmpty()){
            Calendar time = Calendar.getInstance();
            String hour;
            String us = user.getNombre() + " " + user.getApellido()+ " "+ user.getUsuario();
            hour = (LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
            String result;
            result = dm.openTable(txtcontrato.getText(), ninvitados.getText(), nmesa.getText(), fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE),hour,us);
            if(result.equals("OK"))
                JOptionPane.showMessageDialog(null, "Apertura de mesa exitosa");
            if(result.equals("FAIL"))
                JOptionPane.showMessageDialog(null, "Fallo al abrir la mesa");
        }
    }
    
    
    
}
