/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class BlackListController implements Initializable {

    @FXML TableColumn<Cliente, String> cedula;
    @FXML TableColumn<Cliente, String> nombre;
    @FXML TableColumn<Cliente, String> contrato;
    @FXML TableColumn<Cliente, String> plan;
    @FXML TableView table;
    MainMenuController menu;
    String host;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initTable();
        } catch (SQLException | RemoteException | NotBoundException ex) {
            Logger.getLogger(BlackListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void initTable() throws SQLException, RemoteException, NotBoundException{
    cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
    nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
    plan.setCellValueFactory(new PropertyValueFactory<>("plan"));    

    Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
   ArrayList<Cliente> list =inter.getRestringido();
    if(!list.isEmpty())
        list.stream().forEach((c) -> {
            table.getItems().add(c);
    });
}
          public void back(ActionEvent evt){
          menu.aux.getChildren().clear();
          menu.main.setVisible(true);
          menu.main.toFront();
      }
}
