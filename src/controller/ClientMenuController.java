/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

    

public class ClientMenuController implements Initializable {

    public Cliente client;
    @FXML Label contrato;
    @FXML Label nombre;
    @FXML Label cedula;
    @FXML Label plan;
    @FXML AnchorPane mainPane;
    @FXML AnchorPane aux;
    Usuario user;
    
    
    
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       aux.toBack();
    }    
    
    @FXML public void focusin(MouseEvent evt){
          ScaleTransition st = new ScaleTransition();
          st.setNode((Node) evt.getSource());
          st.setFromX(1);
          st.setFromY(1);
          st.setToX(1.2);
          st.setToY(1.2);
          st.setDuration(Duration.millis(60));
          st.play();
    }
    
    @FXML public void focusout(MouseEvent evt){
          ScaleTransition st = new ScaleTransition();
          st.setNode((Node) evt.getSource());
          st.setFromX(1.2);
          st.setFromY(1.2);
          st.setToX(1);
          st.setToY(1);
          st.setDuration(Duration.millis(60));
          st.play();
    }
    
    @FXML public void openTable(ActionEvent evt) throws IOException, SQLException{
        aux.setVisible(false);
        FXMLLoader loader  = new FXMLLoader(getClass().getResource("/fxml/openTable.fxml"));
        OpenTableController controller;
        AnchorPane pan = loader.load();
        aux.getChildren().add(pan);
        pan.prefHeightProperty().bind(aux.heightProperty());
        pan.prefWidthProperty().bind(aux.widthProperty());
        controller = loader.getController();
        controller.client = this.client;
        controller.user = this.user;
        try{
             controller.initData();
        }catch(Exception ex)
        {System.out.println(ex.getMessage());}
        aux.toFront();
        aux.setVisible(true);
        mainPane.setVisible(false);     
        
    }
    
    public void initData(){
        contrato.setText(client.getContrato());
        nombre.setText(client.getNombre());
        cedula.setText(client.getCedula());
        plan.setText(client.getPlan());
    }
}
