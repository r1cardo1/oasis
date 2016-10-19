/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.DataManager;
import classes.Invitado;
import classes.PrinterOptions;
import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class OpenTableController implements Initializable {

    @FXML
    TextField txtnombre, txtcedula, txtcontrato, txtplan, ninvitados,  addnombre, addapellido, addcedula;
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
    SearchController menu;
    String printer;

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
        int cant = dm.getCantByPlan(client.getPlan());
        /*if (rs.next()) {
            max = rs.getInt("invitados");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alerta");
            alert.setHeaderText(null);
            alert.setContentText("El plan que posee el cliente no tiene un numero de invitados,"
                    + "Contacte con el administrador para indicar uno.");
            alert.show();
        }*/

    }

    @FXML
    public void openTable(ActionEvent evt) throws IOException {
        if (!ninvitados.getText().isEmpty()) {            
                
                String hour;
                String us = user.getNombre() + " " + user.getApellido() + " " + user.getUsuario();                
                Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
                String ampm = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
                hour =  Integer.toString(time.get(Calendar.HOUR) == 0 ? 12 : time.get(Calendar.HOUR))
                                            + ":" + Integer.toString(time.get(Calendar.MINUTE))
                                            + ":" + Integer.toString(time.get(Calendar.SECOND))
                                            + ampm;
                dm.openTable(txtcontrato.getText(), ninvitados.getText(),Integer.toString(table.getItems().size()), fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE), hour, us);
                for (int i = 0; i < table.getItems().size(); i++) {
                    Invitado inv = (Invitado) table.getItems().get(i);
                    dm.addInvad(inv.getNombre(), inv.getApellido(), inv.getCedula(), inv.getContrato(), fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
                }
                PrinterOptions p = new PrinterOptions();

                p.resetAll();
                p.initialize();
                p.feedBack((byte) 2);
                p.color(0);
                p.alignCenter();
                p.setText("Oasis Club C.A");
                p.newLine();
                p.setText("Carretera Kilómetro 7 1/2");
                p.newLine();                
                p.setText("Vía la Cañada Sector Camuri.");
                p.newLine();
                p.setText("San francisco, Zulia");
                p.newLine();
                p.addLineSeperator();
                p.alignLeft();
                p.newLine();
                p.setText("Fecha \t\t:" + fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
                p.newLine();
                p.setText("Cliente \t:" + client.getNombre());
                p.newLine();
                p.setText("Cedula \t\t:" +client.getCedula());
                p.newLine();
                p.setText("Contrato \t:"+client.getContrato());
                p.newLine();
                p.addLineSeperator();
                p.newLine();
                p.alignCenter();
                p.setText(" Articulos ");
                p.newLine();
                p.alignLeft();
                p.addLineSeperator();

                p.newLine();

                p.setText("No \tArt\t\tCant\tPrec");
                p.newLine();
                p.addLineSeperator();
                p.newLine();
                p.setText("1" + "\t" + "Apert. Mesa" + "\t" +  "1" + "\t" + "0");
                p.newLine();
                if(table.getItems().size()>0)
                    p.setText("1" + "\t" + "Pase Inv Adic" + "\t" +  table.getItems().size() + "\t" + "4000");
                p.newLine();
                p.addLineSeperator();
                p.newLine();
                p.setText("Precio Total" + "\t" + "\t" +  table.getItems().size()*4000 );
                p.newLine();
                p.addLineSeperator();
                p.feed((byte) 3);
                p.finit();
               // feedPrinter(p.finalCommandSet().getBytes());
               print(p.finalCommandSet().getBytes());
               System.out.println(p.finalCommandSet());           
        }
    }

    public void initTable() {
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        cedula.setCellValueFactory(new PropertyValueFactory("cedula"));
    }

    @FXML
    public void addAction() {
            if (!addnombre.getText().isEmpty()) {
                if (!addapellido.getText().isEmpty()) {
                    table.getItems().add(new Invitado(addnombre.getText(), addapellido.getText(), addcedula.getText(), client.getContrato(), fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE)));
                }
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

    public void print(byte[] b) throws IOException{
          Stage stage = new Stage();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/selectPrinter.fxml"));
          Parent root = loader.load();
          Scene scene = new Scene(root);
          SelectPrinterController controller = loader.getController();
          controller.a=b;          
          stage.setScene(scene);
          stage.show();
    }

}
