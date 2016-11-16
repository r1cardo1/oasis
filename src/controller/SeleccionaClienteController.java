/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class SeleccionaClienteController implements Initializable {

    
    @FXML TableView<Cliente> table;
    @FXML TableColumn<Cliente,String> nombre,cedula,contrato,plan;
    ArrayList<Cliente> list;
    ReservaController controller;
    String opc;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }    
    
    public void initTable(){
        list.forEach((c) -> {
            table.getItems().add(c);
        });
    }
    
    public void selectCliente(){
        
    }
    
}
