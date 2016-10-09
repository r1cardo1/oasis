/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;


public class AdminMenuController implements Initializable {

    MainMenuController menu;
    @FXML AnchorPane main;
    @FXML AnchorPane aux;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    
    @FXML public void back(ActionEvent evt){
          menu.aux.getChildren().clear();
          menu.main.setVisible(true);
          menu.main.toFront();
    }
    
}
