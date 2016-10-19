/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.DataManager;
import classes.Reserva;
import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
      DataManager dm = new DataManager();
      Usuario usuario;

      @Override
      public void initialize(URL url, ResourceBundle rb) {
            try {
                  initTable();
            } catch (SQLException ex) {
                  ex.printStackTrace();
            }
            initCombo();
      }

      public void initTable() throws SQLException {
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
                        } catch (SQLException ex) {
                              ex.printStackTrace();
                        }
                  }
            });
      }

      public void reloadTable() throws SQLException {
            table.getItems().clear();
            ArrayList<Reserva> reservas = dm.getReservas();
           for(Reserva r:reservas) {
                  table.getItems().add(r);
            }
      }

      public void changePane(String old, String neww) throws SQLException {
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
      
      public void filterSearch() throws SQLException{
            switch((String) combo.getSelectionModel().getSelectedItem()){
                  case "TITULAR":
                        searchByName();
                        break;
                  case "CEDULA":
                        searchByCI();
                        break;                  
            }
      }

      public void searchByName() throws SQLException {
            if (!filter.getText().isEmpty()) {
                  table.getItems().clear();
                  ArrayList<Reserva> reservas = dm.getReservasByName(filter.getText().toUpperCase());
                  for(Reserva r:reservas) {
                        table.getItems().add(r);
                  }
            }
      }

      public void searchByCI() throws SQLException {
            if (!filter.getText().isEmpty()) {
                  table.getItems().clear();
                  ArrayList<Reserva> reservas = dm.getReservasByCI(filter.getText().toUpperCase());
                  for(Reserva r:reservas) {
                        table.getItems().add(r);
                  }
            }
      }

      public void searchByDate() throws SQLException {
            table.getItems().clear();
            ArrayList<Reserva> res = dm.getReservas();
            for(Reserva rsv:res) {
                  if (date.getValue().isEqual(LocalDate.parse(rsv.getFecha())))
                        table.getItems().add(rsv);
            }
      }

      public void searchByRangeDate() throws SQLException {
            table.getItems().clear();
            ArrayList<Reserva> res = dm.getReservas();
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
      
      public void eliminaReserva() throws SQLException{
            if(!table.getSelectionModel().isEmpty()){
                  dm.eliminaReserva((Reserva)table.getSelectionModel().getSelectedItem());
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
