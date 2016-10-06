/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import static javafx.animation.Animation.INDEFINITE;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class SearchController implements Initializable {

      MainMenuController menuController;
      @FXML ImageView logo;
      @Override
      public void initialize(URL url, ResourceBundle rb) {

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
      
      @FXML public void minimize(ActionEvent evt){
            Stage stage;
            Button  b = (Button) evt.getSource();
            stage = (Stage) b.getScene().getWindow();
            stage.setIconified(true);
      }    
      
      @FXML public void close(ActionEvent evt){
            Stage stage;
            Button  b = (Button) evt.getSource();
            stage = (Stage) b.getScene().getWindow();
            stage.close();
      }     
     
      public void setMenuController(MainMenuController c){
            this.menuController = c;
      }
      
}
