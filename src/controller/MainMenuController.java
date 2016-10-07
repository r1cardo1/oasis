/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.DigitalClock;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class MainMenuController implements Initializable {

      @FXML Label date;
      @FXML Label hour;
      @FXML AnchorPane topPane;
      public Stage primStage;
      Double xs,ys;
      boolean maximized=false;
      boolean minimized=false;
            
            
      /**
       * Initializes the controller class.
       */
      @Override
      public void initialize(URL url, ResourceBundle rb) {
            // TODO
            new DigitalClock(date,hour);
            drag();
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
      
      @FXML public void close(ActionEvent evt){
            Stage stage;
            Button  b = (Button) evt.getSource();
            stage = (Stage) b.getScene().getWindow();
            stage.close();
      }     
      
      public void setStage(Stage s){
      primStage = s;
      }
      
      @FXML public void maximize(ActionEvent evt){
            
            Stage stage;
            Button  b = (Button) evt.getSource();
            stage = (Stage) b.getScene().getWindow();
            if(maximized){
                  stage.setMaximized(false);
                  maximized = false;
            }else{
                  stage.setMaximized(true);
                  maximized = true;
            }
            
            
      }     
      
      @FXML public void minimize(ActionEvent evt){
            Stage stage;
            Button  b = (Button) evt.getSource();
            stage = (Stage) b.getScene().getWindow();
            stage.setIconified(true);
      }     
      
      public void drag(){

            topPane.setOnMousePressed(new EventHandler<MouseEvent>(){
                  @Override
                  public void handle(MouseEvent evt){
                        xs=evt.getSceneX();
                        ys=evt.getSceneY();
                  }
            });           
            
            topPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primStage.setX(event.getScreenX() - xs);
                primStage.setY(event.getScreenY() - ys);
            }
        });
            

            
      }
            
}
