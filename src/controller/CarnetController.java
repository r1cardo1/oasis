/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


public class CarnetController implements Initializable {

     @FXML
     Label nombre,apellido,cedula,contrato,tipo,plan;
     CarnetController myController;
     String host;
     Usuario usuario;
     
     @Override
     public void initialize(URL url, ResourceBundle rb) {
     }   
     
     public void initData(Cliente c,int tip){
          nombre.setText(c.getNombre());
          cedula.setText(c.getCedula());
          contrato.setText(c.getContrato());
          plan.setText(c.getPlan());
          if(tip==0)
               tipo.setText("TITULAR");
          else
               tipo.setText("FAMILIAR");
     }

     public void setNombre(String nombre) {
          this.nombre.setText(nombre);
     }

     public void setApellido(String apellido) {
          this.apellido.setText(apellido);
     }

     public void setCedula(String cedula) {
          this.cedula.setText(cedula);
     }

     public void setContrato(String contrato) {
          this.contrato.setText(contrato);
     }

     public void setPlan(String plan) {
          this.plan.setText(plan);
     }

     
     
}
