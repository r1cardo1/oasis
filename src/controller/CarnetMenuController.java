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

/**
 * FXML Controller class
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class CarnetMenuController implements Initializable {

    @FXML
    private AnchorPane aux;
    @FXML
    private AnchorPane main;
    MainMenuController menu;
    CarnetMenuController myController;
    Usuario usuario;
    String host;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TODO
    }

    @FXML
    private void generaCarnetCliente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/busquedaCarnet.fxml"));
        AnchorPane pane = loader.load();
        BusquedaCarnetController controller = loader.getController();
        controller.myController=controller;
        controller.host=this.host;
        controller.user = this.usuario;
        aux.getChildren().add(pane);
        aux.toFront();
        main.setVisible(false);
        
    }

    @FXML
    private void generaCarnetFamiliar(ActionEvent event) {
        
    }

    @FXML
    private void listaCarnetGenerados(ActionEvent event) {
        
    }

    @FXML
    private void back(ActionEvent event) {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

}
