/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oasis;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
            con.primStage=stage;
            scene.setFill(Color.TRANSPARENT);            
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);      
            con.setStage(stage);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/task.png")));
            stage.setTitle("Inicio de sesion Oasis Club");
            stage.show();
            con.initHost();
      }

      /**
       * @param args the command line arguments
       */
      public static void main(String[] args) {
            launch(args);
      }
      
      
      
}
