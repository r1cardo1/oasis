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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Ricardo Marcano
 */
public class UserPanelController implements Initializable {

    AdminMenuController menu;
    UserPanelController myController;
    DataManager dm = new DataManager();
    @FXML TableColumn<Usuario, String> nombre,apellido,user,pass,nivel;
    @FXML TableView table;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            initTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }    
    
        @FXML public void back(ActionEvent evt){
          menu.aux.getChildren().clear();
          menu.main.setVisible(true);
          menu.main.toFront();
    }
        
    public void initTable() throws SQLException{
        table.getItems().clear();
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        user.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        pass.setCellValueFactory(new PropertyValueFactory<>("clave"));
        nivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        
        ResultSet rs;
        rs = dm.getUsuarios();
        if(rs!=null)
            while(rs.next()){
                table.getItems().add(new Usuario(rs.getString("nombre"),rs.getString("apellido"),rs.getString("usuario"),rs.getString("clave"),rs.getInt("nivel")));
            }
    }
    
    @FXML public void newUser(ActionEvent evt) throws IOException{
        Stage stage =new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newUser.fxml"));
        Scene scene = new Scene(loader.load());
        NewUserController controller = loader.getController(); 
        controller.panel = myController;
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        controller.primStage=stage;
        controller.drag();
        stage.show();
    }
    
}
