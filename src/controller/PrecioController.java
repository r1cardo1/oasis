/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import oasiscrud.oasisrimbd;

/**
 * FXML Controller class
 *
 * @author Ricardo Marcano
 */
public class PrecioController implements Initializable {

    String host;
    @FXML TextField precio;
    public Stage myStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public void initPrecio() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        
        precio.setText(inter.precio());
    }
    
    public void updatePrecio() throws RemoteException, NotBoundException{
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        inter.upPrecio(precio.getText());
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Confirmacion");
        a.setContentText("Precio actualizado exitosamente");
        a.show();
        myStage.close();
    }
            
}
