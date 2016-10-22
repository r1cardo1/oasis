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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ricardo Marcano
 */
public class ReportMenuController implements Initializable {

    Usuario usuario;
    Stage stage;
    MainMenuController menu;
    @FXML
    AnchorPane main;
    @FXML
    AnchorPane aux;
    ReportMenuController myController;
    String host;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void back(ActionEvent evt) {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

    @FXML
    public void loginReport(ActionEvent evt) throws IOException, SQLException, RemoteException, NotBoundException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginReport.fxml"));
        Pane pan = loader.load();
        LoginReportController controller = loader.getController();
        controller.menu = myController;
        controller.host = this.host;
        controller.initUsers();
        controller.initCombo();
        controller.initTable();
        aux.getChildren().add(pan);
        main.setVisible(false);
        aux.toFront();
    }

    @FXML
    public void searchReport() throws IOException, SQLException, RemoteException, NotBoundException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/searchReport.fxml"));
        Pane pan = loader.load();
        SearchReportController controller = loader.getController();
        controller.menu = myController;
        controller.host = this.host;
        controller.initCombo();
        controller.first();
        controller.initTable();
        aux.getChildren().add(pan);
        main.setVisible(false);
        aux.toFront();
    }

    public void apertMesa() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reporteApertMesa.fxml"));
        Pane pan = loader.load();
        ReporteApertMesaController controller = loader.getController();
        controller.menu = myController;
        controller.host = this.host;        
        aux.getChildren().add(pan);
        main.setVisible(false);
        aux.toFront();
    }

}
