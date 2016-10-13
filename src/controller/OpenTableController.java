/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.DataManager;
import classes.Invitado;
import classes.Usuario;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;

public class OpenTableController implements Initializable {

      @FXML
      TextField txtnombre, txtcedula, txtcontrato, txtplan, ninvitados, nmesa, addnombre, addapellido, addcedula;
      @FXML
      DatePicker fecha;
      @FXML
      TableView table;
      @FXML
      TableColumn<Invitado, String> nombre, apellido, cedula;
      Cliente client;
      Usuario user;
      int max = 0;
      DataManager dm = new DataManager();
      ClientMenuController menu;

      @Override
      public void initialize(URL url, ResourceBundle rb) {
            initTable();
      }

      public void initData() throws SQLException {
            txtnombre.setText(client.getNombre());
            txtcedula.setText(client.getCedula());
            txtcontrato.setText(client.getContrato());
            txtplan.setText(client.getPlan());
            fecha.setValue(LocalDate.now());
            ResultSet rs = dm.getCantByPlan(client.getPlan());
            if (rs.next()) {
                  max = rs.getInt("invitados");
            } else {
                  Alert alert = new Alert(Alert.AlertType.INFORMATION);
                  alert.setTitle("Alerta");
                  alert.setHeaderText(null);
                  alert.setContentText("El plan que posee el cliente no tiene un numero de invitados,"
                          + "Contacte con el administrador para indicar uno.");
                  alert.show();
            }

      }

      @FXML
      public void openTable(ActionEvent evt) {
            if (!nmesa.getText().isEmpty() && !ninvitados.getText().isEmpty()) {
                  if (Integer.parseInt(ninvitados.getText()) <= max) {
                        Calendar time = Calendar.getInstance();
                        String hour;
                        String us = user.getNombre() + " " + user.getApellido() + " " + user.getUsuario();
                        hour = (LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
                        String result;
                        result = dm.openTable(txtcontrato.getText(), ninvitados.getText(), nmesa.getText(), fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE), hour, us);
                        for (int i = 0; i < table.getItems().size(); i++) {
                              Invitado inv = (Invitado) table.getItems().get(i);
                              dm.addInvad(inv.getNombre(), inv.getApellido(), inv.getCedula(), inv.getContrato(), fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
                        }
                        if (result.equals("OK")) {
                              Alert alert = new Alert(Alert.AlertType.INFORMATION);
                              alert.setTitle("Alerta");
                              alert.setHeaderText(null);
                              alert.setContentText("Apertura de mesa exitosa");
                              alert.show();
                        }

                  } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Alerta");
                        alert.setHeaderText(null);
                        alert.setContentText("El cliente tiene mas invitados de los permitidos por su plan");
                        alert.show();
                  }
            }
      }

      public void initTable() {
            nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
            cedula.setCellValueFactory(new PropertyValueFactory("cedula"));
      }

      @FXML
      public void addAction() {
            if (table.getItems().size() < max) {
                  if (!addnombre.getText().isEmpty()) {
                        if (!addapellido.getText().isEmpty()) {
                              table.getItems().add(new Invitado(addnombre.getText(), addapellido.getText(), addcedula.getText(), client.getContrato(),fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE)));
                        }
                  }
            } else {
                  Alert alert = new Alert(Alert.AlertType.INFORMATION);
                  alert.setTitle("Alerta");
                  alert.setHeaderText(null);
                  alert.setContentText("Ya agrego el numero maximo de invitados adicionales");
                  alert.show();
            }
      }

      @FXML
      public void deleteAction() {
            if (!table.getSelectionModel().isEmpty()) {
                  table.getItems().remove(table.getSelectionModel().getSelectedIndex());
                  table.getSelectionModel().clearSelection();
            }
      }
      
          public void back() {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

}
