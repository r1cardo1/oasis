/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.DataManager;
import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class LoginController implements Initializable {

      @FXML Pane topPane;
      private double xs,ys=0;
      public Stage primStage;
      DataManager dm = new DataManager();
      @FXML TextField user;
      @FXML PasswordField pass;
      
      /**
       * Initializes the controller class.
       */
      @Override
      public void initialize(URL url, ResourceBundle rb) {
            // TODO
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
      
      @FXML public void login(ActionEvent evt) throws SQLException, IOException{
            ResultSet rs = dm.login(user.getText());
            Usuario userLogin=null;

            if(rs!=null){
                  while(rs.next()){
                        userLogin = new Usuario(rs.getString("nombre"),rs.getString("apellido"),rs.getString("usuario"),rs.getString("clave"));
                        if(userLogin.getUsuario().equals(user.getText()))
                              if(userLogin.getClave().equals(pass.getText())){
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml"));
                                    MainMenuController controller;
                                    Parent root = loader.load();
                                    controller = loader.getController();
                                    controller.setController(controller);
                                    Scene scene = new Scene(root);
                                    scene.setFill(Color.TRANSPARENT);
                                    Stage stage = new Stage();
                                    stage.setScene(scene);
                                    stage.initStyle(StageStyle.TRANSPARENT);
                                    stage.show();
                                    controller.setStage(stage);
                                    controller.setUser(userLogin);
                                    primStage.close();
                              }else{
                                    JOptionPane.showMessageDialog(null,"Contraseña Incorrecta");
                              }
                  }
            }
            if(userLogin==null){
                  JOptionPane.showMessageDialog(null,"El usuario no existe");
            }
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

      public void setStage(Stage s){
           primStage = s;
      }
 
}
