/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasis;

import controller.LoginController;
import controller.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Ricardo
 */
public class Oasis extends Application {
      
      @Override
      public void start(Stage stage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));            
            Parent root = loader.load();
            LoginController con = loader.getController();            
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);            
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);      
            con.setStage(stage);
            stage.show();
      }

      /**
       * @param args the command line arguments
       */
      public static void main(String[] args) {
            launch(args);
      }
      
}
