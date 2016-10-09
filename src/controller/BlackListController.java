/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.DataManager;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    DataManager dm = new DataManager();
    MainMenuController menu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initTable();
        } catch (SQLException ex) {
            Logger.getLogger(BlackListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void initTable() throws SQLException{
    cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
    nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
    plan.setCellValueFactory(new PropertyValueFactory<>("plan"));    

    
    ResultSet  rs =dm.getRestringidos();
    if(rs != null)
    while(rs.next()){
        table.getItems().add(new Cliente(rs.getString("cedula"),rs.getString("nombre"),rs.getString("contrato"),rs.getString("plan"),rs.getString("banco"),rs.getString("restringido")));
    }
}
          public void back(ActionEvent evt){
          menu.aux.getChildren().clear();
          menu.main.setVisible(true);
          menu.main.toFront();
      }
}
