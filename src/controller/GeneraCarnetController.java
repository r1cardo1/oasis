/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.Usuario;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class GeneraCarnetController implements Initializable {

     @FXML
     TextField nombre, apellido, cedula, contrato, plan;
     @FXML
     VBox base;
     CarnetController carnet;
     String host;
     Usuario usuario;
     Cliente client;
     BusquedaCarnetController menu;

     @Override
     public void initialize(URL url, ResourceBundle rb) {

     }

     public void initData(Cliente c) throws IOException {
          nombre.setText(c.getNombre());
          cedula.setText(c.getCedula());
          contrato.setText(c.getContrato());
          plan.setText(c.getPlan());
          initCarnet(c);
     }

     public void initCarnet(Cliente c) throws IOException {
          FXMLLoader panel = new FXMLLoader(getClass().getResource("/fxml/Carnet.fxml"));
          AnchorPane carnett = panel.load();

          base.getChildren().clear();
          base.getChildren().add(carnett);
          CarnetController controller = panel.getController();
          controller.myController = controller;
          controller.host = this.host;
          controller.usuario = this.usuario;
          controller.initData(c, 0);
          this.carnet = controller;
     }

     public void tomaFoto() throws IOException {
          Stage stage = new Stage();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TomarFoto.fxml"));
          Parent root = loader.load();
          TomarFotoController controller = loader.getController();
          controller.myStage = stage;
          controller.carnet = this.carnet;
          Scene sc = new Scene(root);
          stage.setScene(sc);
          stage.show();
     }

     public void seleccionaFoto() throws IOException {
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open Resource File");
          fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
          File f = fileChooser.showOpenDialog(null);
          try{
               System.out.println(f.getAbsolutePath());
          Image im = new Image("file:///"+f.getAbsolutePath());
          Stage stage = new Stage();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SeleccionaFoto.fxml"));
          Parent root = loader.load();
          SeleccionaFotoController controller = loader.getController();
          controller.myStage = stage;
          controller.carnet=this.carnet;
          controller.setFoto(im);
          Scene scene = new Scene(root);
          stage.setScene(scene);
          stage.show();
          }catch(Exception ex){
               ex.printStackTrace();
          }
     }

}
