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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import oasiscrud.oasisrimbd;

public class AdminMenuController implements Initializable {

     MainMenuController menu;
     @FXML
     AnchorPane main;
     @FXML
     AnchorPane aux;
     AdminMenuController myController;
     Usuario usuario;
     String host;
     Usuario user;

     @Override
     public void initialize(URL url, ResourceBundle rb) {
          // TODO
     }

     @FXML
     public void userPanel(ActionEvent evt) throws IOException, SQLException, RemoteException, NotBoundException {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userPanel.fxml"));
          Pane pan = loader.load();
          aux.getChildren().add(pan);
          UserPanelController controller = loader.getController();
          controller.menu = myController;
          controller.myController = controller;
          controller.usuario = this.usuario;
          controller.host = this.host;
          controller.initTable();
          aux.toFront();
          main.setVisible(false);
     }

     @FXML
     public void plans() throws IOException, SQLException, RemoteException, NotBoundException {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plans.fxml"));
          Pane pan = loader.load();
          aux.getChildren().add(pan);
          PlansController controller = loader.getController();
          controller.menu = myController;
          controller.myController = controller;
          controller.usuario = this.usuario;
          controller.host = this.host;
          controller.initTable();
          aux.toFront();
          main.setVisible(false);
     }

     @FXML
     public void back(ActionEvent evt) {
          menu.aux.getChildren().clear();
          menu.main.setVisible(true);
          menu.main.toFront();
     }

     @FXML
     public void clientes() throws IOException {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdministraCliente.fxml"));
          Parent root = loader.load();
          AdministraClienteController controller = loader.getController();
          controller.menuController = myController;
          controller.user = this.user;
          controller.myController = controller;
          controller.host = this.host;
          Scene scene = new Scene(root);
          scene.setFill(Color.TRANSPARENT);
          Stage stage = new Stage();
          stage.setScene(scene);
          stage.initStyle(StageStyle.TRANSPARENT);
          controller.primStage = stage;
          stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/task.png")));
          stage.setTitle("Busqueda de clientes Oasis");
          stage.show();
     }

     @FXML
     public void precio() throws IOException, RemoteException, NotBoundException {
          Stage stage = new Stage();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/precio.fxml"));
          Parent root = loader.load();
          Scene scn = new Scene(root);
          PrecioController controller = loader.getController();
          controller.host = this.host;
          controller.initPrecio();
          stage.setScene(scn);
          controller.myStage = stage;
          stage.show();
     }

     @FXML
     public void deletePases() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Alert alert = new Alert(AlertType.CONFIRMATION);
          alert.setTitle("Confirmacion");
          alert.setHeaderText("¿Desea eliminar todos los pases?");
          alert.setContentText("Si presiona si, seran eliminados de la base de datos todos los pases agregados "
                 + "hagalo solo si ya tiene todos los reportes de los pases y ya no necesite esta informacion\n\n"
                 + "Para eliminarlos presione OK, si no, Cancelar.");
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK) {
               inter.truncatePases();
          }

     }

     @FXML
     public void deleteLogins() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Alert alert = new Alert(AlertType.CONFIRMATION);
          alert.setTitle("Confirmacion");
          alert.setHeaderText("¿Desea eliminar el registro de inicio de sesion?");
          alert.setContentText("Si presiona si, sera eliminado todo el registro de los inicios de sesion "
                 + "de los usuarios, si no necesita esta informacion lo puede eliminar.\n\n"
                 + "Presione OK para continuar");
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK) {
               inter.truncateLogins();
          }

     }

     @FXML
     public void deleteReservas() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Alert alert = new Alert(AlertType.CONFIRMATION);
          alert.setTitle("Confirmacion");
          alert.setHeaderText("¿Desea eliminar todas las reservas?");
          alert.setContentText("Si presiona si, seran eliminadas todas las reservas de la base de datos, "
                 + "haga esto solo si no necesita dicha informacion en un futuro\n\n"
                 + "Presione OK para continuar.");
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK) {
               inter.truncateReservas();
          }

     }

     @FXML
     public void deleteBusquedas() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Alert alert = new Alert(AlertType.CONFIRMATION);
          alert.setTitle("Confirmacion");
          alert.setHeaderText("¿Desea eliminar todas las busquedas?");
          alert.setContentText("Si presiona si, seran eliminado el historial de busquedas de clientes de todos los usuarios\n\n"
                 + "Presione OK para continuar");
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK) {
               inter.truncateBusquedas();
          }

     }

     @FXML
     public void deleteOpenTables() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Alert alert = new Alert(AlertType.CONFIRMATION);
          alert.setTitle("Confirmacion");
          alert.setHeaderText("¿Desea eliminar todas las aperturas de mesas?");
          alert.setContentText("Si presiona si, seran eliminadas todas las aperturas de mesas del sistema, haga esto"
                 + " solo si no necesita dicha infomacion en un futuro\n\n"
                 + "Presione OK para continuar.");
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK) {
               inter.truncateOpenTables();
          }

     }

     @FXML
     public void deleteAutorizados() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Alert alert = new Alert(AlertType.CONFIRMATION);
          alert.setTitle("Confirmacion");
          alert.setHeaderText("¿Desea eliminar todos los Autorizados?");
          alert.setContentText("Si presiona si, seran eliminados de la base de datos todos los autorizados agregados "
                 + "hagalo solo si ya tiene todos los reportes de los autorizados y ya no necesite esta informacion\n\n"
                 + "Para eliminarlos presione OK");
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK) {
               inter.truncateAutorizados();
          }

     }

     @FXML
     public void eliminaInvitados() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Alert alert = new Alert(AlertType.CONFIRMATION);
          alert.setTitle("Confirmacion");
          alert.setHeaderText("¿Desea eliminar todos los invitados?");
          alert.setContentText("Si presiona si, seran eliminados de la base de datos todos los invitados agregados "
                 + "hagalo solo si ya tiene todos los reportes de los invitados y ya no necesite esta informacion\n\n"
                 + "Para eliminarlos presione OK.");
          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == ButtonType.OK) {
               inter.truncateInvitados();
          }

     }
     
     
     
     
}
