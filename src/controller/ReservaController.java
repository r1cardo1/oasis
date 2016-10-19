/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import classes.Reserva;
import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Ricardo Marcano
 */
public class ReservaController implements Initializable {

      @FXML
      TableColumn<Reserva, String> titular, cedula, telefono, plan, invitados, fecha;
      @FXML
      TableView table;
      @FXML
      HBox pfilter, pdate, rangeDate;
      @FXML
      ComboBox combo;
      @FXML
      TextField filter;
      @FXML
      DatePicker from, to, date;
      ReservaController myController;
      MainMenuController menu;
      Usuario usuario;
    String host;

      @Override
      public void initialize(URL url, ResourceBundle rb) {
            try {
                  initTable();
            } catch (RemoteException | NotBoundException | SQLException ex) {
              Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
          }
            initCombo();
      }

      public void initTable() throws SQLException, RemoteException, NotBoundException {
            titular.setCellValueFactory(new PropertyValueFactory<>("titular"));
            cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
            telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
            invitados.setCellValueFactory(new PropertyValueFactory<>("invitados"));
            fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
            reloadTable();
      }

      public void initCombo() {
            combo.getItems().addAll("TODAS", "TITULAR", "CEDULA", "FECHA", "RANGO DE FECHA");
            combo.getSelectionModel().selectFirst();
            combo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                  @Override
                  public void changed(ObservableValue observableValue, String old, String neww) {
                        try {
                              changePane(old, neww);                        
                        } catch (RemoteException | NotBoundException | SQLException ex) {
                          Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
                      }
                  }
            });
      }

      public void reloadTable() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
            table.getItems().clear();
            ArrayList<Reserva> reservas = inter.getReservas();
            reservas.stream().forEach((r) -> {
                table.getItems().add(r);
          });
      }

      public void changePane(String old, String neww) throws SQLException, RemoteException, NotBoundException {
            switch (old) {
                  case "TITULAR":
                        if (!neww.equals("CONTRATO") || !neww.equals("CEDULA")) {
                              pfilter.setVisible(false);
                        }
                        break;
                  case "CEDULA":
                        if (!neww.equals("CONTRATO") || !neww.equals("NOMBRE")) {
                              pfilter.setVisible(false);
                        }
                        break;
                  case "FECHA":
                        pdate.setVisible(false);
                        break;
                  case "RANGO DE FECHA":
                        rangeDate.setVisible(false);
                        break;
            }
            switch (neww) {
                  case "TITULAR":
                        if (!old.equals("CONTRATO") || !old.equals("CEDULA")) {
                              pfilter.setVisible(true);
                        }
                        break;
                  case "CEDULA":
                        if (!old.equals("CONTRATO") || !old.equals("NOMBRE")) {
                              pfilter.setVisible(true);
                        }
                        break;
                  case "FECHA":
                        pdate.setVisible(true);
                        break;
                  case "RANGO DE FECHA":
                        rangeDate.setVisible(true);
                        break;
                  case "TODAS":
                        reloadTable();
                        break;

            }
      }
      
      public void filterSearch() throws SQLException, RemoteException, NotBoundException{
            switch((String) combo.getSelectionModel().getSelectedItem()){
                  case "TITULAR":
                        searchByName();
                        break;
                  case "CEDULA":
                        searchByCI();
                        break;                  
            }
      }

      public void searchByName() throws SQLException, RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
            if (!filter.getText().isEmpty()) {
                  table.getItems().clear();
                  ArrayList<Reserva> reservas = inter.getReservasByName(filter.getText().toUpperCase());
                  reservas.stream().forEach((r) -> {
                      table.getItems().add(r);
              });
            }
      }

      public void searchByCI() throws SQLException, RemoteException, NotBoundException {
            if (!filter.getText().isEmpty()) {
                  table.getItems().clear();
                  Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
                  ArrayList<Reserva> reservas = inter.getReservasByCI(filter.getText().toUpperCase());
                  reservas.stream().forEach((r) -> {
                      table.getItems().add(r);
                });
            }
      }

      public void searchByDate() throws SQLException, RemoteException, NotBoundException {
            table.getItems().clear();
            Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
            ArrayList<Reserva> res = inter.getReservas();
            for(Reserva rsv:res) {
                  if (date.getValue().isEqual(LocalDate.parse(rsv.getFecha())))
                        table.getItems().add(rsv);
            }
      }

      public void searchByRangeDate() throws SQLException, RemoteException, NotBoundException {
            table.getItems().clear();
            Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
            ArrayList<Reserva> res = inter.getReservas();
            for(Reserva rsv:res) {
                   if ((from.getValue().isEqual(LocalDate.parse(rsv.getFecha())) || from.getValue().isBefore(LocalDate.parse(rsv.getFecha())))
                                && (to.getValue().isEqual(LocalDate.parse(rsv.getFecha())) || to.getValue().isAfter(LocalDate.parse(rsv.getFecha()))))
                        table.getItems().add(rsv);
            }
      }

      public void back() {
            menu.aux.getChildren().clear();
            menu.main.setVisible(true);
            menu.main.toFront();
      }
      
      public void nuevaReserva() throws IOException{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaReserva.fxml"));
            Parent root = loader.load();
            NuevaReservaController controller = loader.getController();
            controller.primStage = stage;
            Scene scene = new Scene(root);
            controller.menu=myController;
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/task.png")));
            stage.setTitle("Nueva reserva");
            stage.show();
      }
      
      public void editaReserva() throws IOException{
            if(!table.getSelectionModel().isEmpty()){
                  Stage stage = new Stage();
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaReserva.fxml"));
                  Parent root = loader.load();
                  NuevaReservaController controller = loader.getController();
                  controller.primStage = stage;
                  controller.menu=myController;
                  controller.save=false;
                  controller.r =(Reserva) table.getSelectionModel().getSelectedItem();
                  controller.initData();
                  Scene scene = new Scene(root);
                  stage.initStyle(StageStyle.UNDECORATED);
                  stage.setScene(scene);
                  stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/task.png")));
                  stage.setTitle("Editar Reserva");
                  stage.show();
            }
      }
      
      public void eliminaReserva() throws SQLException, RemoteException, NotBoundException{
            if(!table.getSelectionModel().isEmpty()){
                Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
                  inter.eliminaReserva((Reserva)table.getSelectionModel().getSelectedItem());
                  reloadTable();
            }
      }

      public void fromDate() {
            final Callback<DatePicker, DateCell> dayCellFactory
                    = new Callback<DatePicker, DateCell>() {
                  @Override
                  public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                              @Override
                              public void updateItem(LocalDate item, boolean empty) {
                                    super.updateItem(item, empty);

                                    if (item.isBefore(
                                            from.getValue().plusDays(1))) {
                                          setDisable(true);
                                          setStyle("-fx-background-color: #ffc0cb;");
                                    }
                              }
                        };
                  }
            };
            to.setDayCellFactory(dayCellFactory);
            to.setValue(from.getValue().plusDays(1));
      }

}
