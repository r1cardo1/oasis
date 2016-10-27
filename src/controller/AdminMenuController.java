/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdminMenuController implements Initializable {

    MainMenuController menu;
    @FXML
    AnchorPane main;
    @FXML
    AnchorPane aux;
    AdminMenuController myController;
    Usuario usuario;
    String host;
    Usuario user;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void userPanel(ActionEvent evt) throws IOException, SQLException, RemoteException, NotBoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userPanel.fxml"));
        Pane pan = loader.load();
        aux.getChildren().add(pan);
        UserPanelController controller = loader.getController();
        controller.menu = myController;
        controller.myController = controller;
        controller.usuario = this.usuario;
        controller.host = this.host;
        controller.initTable();
        aux.toFront();
        main.setVisible(false);
    }

    @FXML
    public void plans() throws IOException, SQLException, RemoteException, NotBoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plans.fxml"));
        Pane pan = loader.load();
        aux.getChildren().add(pan);
        PlansController controller = loader.getController();
        controller.menu = myController;
        controller.myController = controller;
        controller.usuario = this.usuario;
        controller.host = this.host;
        controller.initTable();
        aux.toFront();
        main.setVisible(false);
    }

    @FXML
    public void back(ActionEvent evt) {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }
    
    public void clientes() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdministraCliente.fxml"));
        Parent root = loader.load();
       AdministraClienteController controller = loader.getController();
        controller.menuController=myController;
        controller.user = this.user;
        controller.myController = controller;
        controller.host = this.host;
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        controller.primStage = stage;
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/task.png")));
        stage.setTitle("Busqueda de clientes Oasis");
        stage.show();
    }

}
