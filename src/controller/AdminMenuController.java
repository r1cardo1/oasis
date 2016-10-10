/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class AdminMenuController implements Initializable {

    MainMenuController menu;
    @FXML
    AnchorPane main;
    @FXML
    AnchorPane aux;
    AdminMenuController myController;
    Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void userPanel(ActionEvent evt) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userPanel.fxml"));
        Pane pan = loader.load();
        aux.getChildren().add(pan);
        UserPanelController controller = loader.getController();
        controller.menu = myController;
        controller.myController = controller;
        controller.usuario = this.usuario;
        aux.toFront();
        main.setVisible(false);
    }

    @FXML
    public void back(ActionEvent evt) {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

}
