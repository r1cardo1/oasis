/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class GeneraCarnetController implements Initializable {

     @FXML
     TextField nombre,apellido,cedula,contrato,plan;
     @FXML
     HBox base;
     CarnetController carnet;
     String host;
     Usuario usuario;
     Cliente client;
     
     
     @Override
     public void initialize(URL url, ResourceBundle rb) {
          
     }   
     
     public void initData(Cliente c) throws IOException{
          nombre.setText(c.getNombre());
          cedula.setText(c.getCedula());
          contrato.setText(c.getContrato());
          plan.setText(c.getPlan());
          initCarnet(c);
     }
     
     public void initCarnet(Cliente c) throws IOException{
          FXMLLoader panel = new FXMLLoader(getClass().getResource("/fxml/Carnet.fxml"));
          base.getChildren().add(panel.load());
          CarnetController controller = panel.getController();
          controller.myController = controller;
          controller.host = this.host;
          controller.usuario = this.usuario;
          controller.initData(c, 0);
          this.carnet = controller;
     }
     
}
