/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.DataManager;
import classes.Usuario;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Ricardo Marcano
 */
public class SearchReportController implements Initializable {

    @FXML
    AnchorPane main, aux, gUserView, gDateView, gRangeDateView, tUserView, tDateView, tRangeDateView;
    @FXML
    HBox hgDate, hgRangeDate, hgUser;
    @FXML
    ComboBox tcUser, gcMode,mgUser;
    DataManager dm = new DataManager();
    Usuario usuario;
    ReportMenuController menu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initCombo();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void initCombo() throws SQLException {
        tcUser.getItems().addAll("TODOS");
        mgUser.getItems().add("TODOS");
        ResultSet rs;
        rs = dm.getUsuarios();
        while (rs.next()) {
            String str = rs.getString("usuario");
            tcUser.getItems().add(str);
            mgUser.getItems().add(str);
        }
        tcUser.getSelectionModel().selectFirst();
        

        gcMode.getItems().addAll("Por Usuario", "Por Fecha", "Por rango de fecha");        
        gcMode.getSelectionModel().selectFirst();
        gcMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observableValue, String old, String neww) {
                changePane(old, neww);
            }
        });
        gcMode.getSelectionModel().selectFirst();
        hgUser.setVisible(true);
    }

    public void changePane(String old, String neww) {
        switch (old) {
            case "Por Usuario":
                hgUser.setVisible(false);
                break;
            case "Por Fecha":
                hgDate.setVisible(false);
                break;
            case "Por rango de fecha":
                hgRangeDate.setVisible(false);
                break;
        }
        switch(neww){
            case "Por Usuario":
                hgUser.setVisible(true);
                break;
            case "Por Fecha":
                hgDate.setVisible(true);
                break;
            case "Por rango de fecha":
                hgRangeDate.setVisible(true);
                break;
        }
    }
}
