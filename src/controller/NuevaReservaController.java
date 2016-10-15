/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.DataManager;
import classes.Reserva;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class NuevaReservaController implements Initializable {

      @FXML
      Label topPane;
      @FXML TextField titular,cedula,plan,telefono,invitados,observacion;
      @FXML DatePicker fecha;
      DataManager dm = new DataManager();
      Double xs, ys;
      Stage primStage;
      Reserva r;
      Boolean save = true;
      ReservaController menu;

      @Override
      public void initialize(URL url, ResourceBundle rb) {
            // TODO
            drag();
      }

      public void close(ActionEvent evt) {
            Stage stage;
            Button b = (Button) evt.getSource();
            stage = (Stage) b.getScene().getWindow();
            stage.close();
      }
      
      public void initData(){
            titular.setText(r.getTitular());
            cedula.setText(r.getCedula());
            plan.setText(r.getPlan());
            telefono.setText(r.getTelefono());
            invitados.setText(r.getInvitados());
            observacion.setText(r.getObservaciones());
            fecha.setValue(LocalDate.parse(r.getFecha()));
      }

      public void minimize(ActionEvent evt) {
            Stage stage;
            Button b = (Button) evt.getSource();
            stage = (Stage) b.getScene().getWindow();
            stage.setIconified(true);
      }

      public void drag() {

            topPane.setOnMousePressed(new EventHandler<MouseEvent>() {
                  @Override
                  public void handle(MouseEvent evt) {
                        xs = evt.getSceneX();
                        ys = evt.getSceneY();
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
      
      public void save(ActionEvent evt) throws SQLException{
            if(!titular.getText().isEmpty() && !cedula.getText().isEmpty())
                  if(save){
                        dm.guardaReservacion(new Reserva(titular.getText(),cedula.getText(),telefono.getText(),plan.getText(),invitados.getText(),fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE),observacion.getText()));
                        menu.reloadTable();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Alerta");
                        alert.setHeaderText(null);
                        alert.setContentText("El usuario ya existe");
                        alert.show();
                        Stage stage;
                        Button b = (Button) evt.getSource();
                        stage = (Stage) b.getScene().getWindow();
                        stage.close();
                  }else{
                        dm.actualizaReservacion(r,new Reserva(titular.getText(),cedula.getText(),telefono.getText(),plan.getText(),invitados.getText(),fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE),observacion.getText()));
                        menu.reloadTable();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Alerta");
                        alert.setHeaderText(null);
                        alert.setContentText("El usuario ya existe");
                        alert.show();
                        Stage stage;
                        Button b = (Button) evt.getSource();
                        stage = (Stage) b.getScene().getWindow();
                        stage.close();
                  }
      }

}
