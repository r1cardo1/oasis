/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Asistencia;
import classes.Autorizado;
import classes.Cliente;
import classes.Invitado;
import classes.Plan;
import classes.PrinterOptions;
import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import oasiscrud.oasisrimbd;

public class AutorizarController implements Initializable {

     @FXML
     TextField txtnombre, txtcedula, txtcontrato, txtplan, ninvitados, addnombre, addapellido, addcedula, autorizado;
     @FXML
     DatePicker fecha;
     @FXML
     TableView table;
     @FXML
     TableColumn<Invitado, String> nombre, apellido, cedula;
     Cliente client;
     Usuario user;
     int max = 0;
     SearchController menu;
     AutorizarController myController;
     String printer;
     String host;
     Autorizado report;
     AutorizadosController auto;
     boolean update = false;
     int inViejos = 0;
     ReservaController reserva;

     @Override
     public void initialize(URL url, ResourceBundle rb) {
          initTable();
     }

     public void initData() throws SQLException, RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          update();
     }

     public void update() {
          txtnombre.setText(client.getNombre());
          txtcedula.setText(client.getCedula());
          txtcontrato.setText(client.getContrato());
          txtplan.setText(client.getPlan());
          fecha.setValue(LocalDate.now());
          if (report != null) {
               autorizado.setText(report.getAutorizado());
          }
     }

     public void initTable() {
          nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
          apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
          cedula.setCellValueFactory(new PropertyValueFactory("cedula"));
     }

     public void addAction() {
          if (!addnombre.getText().isEmpty()) {
               if (!addapellido.getText().isEmpty()) {
                    table.getItems().add(new Invitado(addnombre.getText(), addapellido.getText(), addcedula.getText(), client.getContrato(), fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE)));
               }
          }
     }

     public void deleteAction() {
          if (!table.getSelectionModel().isEmpty()) {
               table.getItems().remove(table.getSelectionModel().getSelectedIndex());
               table.getSelectionModel().clearSelection();
          }
     }

     public void autorizar() throws IOException, RemoteException, NotBoundException, SQLException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          if (!ninvitados.getText().isEmpty()) {
               if (!update) {
                    if (!inter.estaPresente(client)) {
                         updateClient();
                         String hour;
                         String us = user.getNombre() + " " + user.getApellido() + " " + user.getUsuario();
                         Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
                         String ampm = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
                         hour = Integer.toString(time.get(Calendar.HOUR) == 0 ? 12 : time.get(Calendar.HOUR))
                                + ":" + Integer.toString(time.get(Calendar.MINUTE))
                                + ":" + Integer.toString(time.get(Calendar.SECOND))
                                + ampm;
                         inter.creaAsistencia(new Asistencia(ninvitados.getText(), LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), hour, client.getContrato(), Integer.toString(table.getItems().size()), user.getUsuario()));
                         inter.autorizar(new Autorizado(user.getUsuario(), client.getCedula(),
                                client.getNombre(), client.getContrato(),
                                client.getPlan(),
                                LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                                hour, Integer.toString(Integer.parseInt(ninvitados.getText()) + table.getItems().size()), autorizado.getText()));
                         for (int i = 0; i < table.getItems().size(); i++) {
                              Invitado inv = (Invitado) table.getItems().get(i);
                              inter.addInvad(inv.getNombre(), inv.getApellido(), inv.getCedula(), inv.getContrato(), fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
                         }
                         imprimeFactura(inter);
                    } else {
                         Alert a = new Alert(AlertType.INFORMATION);
                         a.setTitle("Informacion");
                         a.setContentText("Ya el cliente ingreso al club, para reimprimir el ticket dirijase a la seccion de visitas en la parte inferior de la lista de busqueda de clientes");
                         a.show();
                    }
               } else {
                    updateClient();
                    String hour;
                    String us = user.getNombre() + " " + user.getApellido() + " " + user.getUsuario();
                    Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
                    String ampm = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
                    hour = Integer.toString(time.get(Calendar.HOUR) == 0 ? 12 : time.get(Calendar.HOUR))
                           + ":" + Integer.toString(time.get(Calendar.MINUTE))
                           + ":" + Integer.toString(time.get(Calendar.SECOND))
                           + ampm;
                    inter.updateAsistencia(new Asistencia(report.getInvitados(), report.getFecha(), report.getHora(), report.getContrato(), Integer.toString(table.getItems().size()), user.getUsuario()),
                           new Asistencia(ninvitados.getText(), LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), hour, client.getContrato(), Integer.toString(table.getItems().size()), user.getUsuario()));
                    inter.updateAutorizar(report, new Autorizado(user.getUsuario(), client.getCedula(),
                           client.getNombre(), client.getContrato(),
                           client.getPlan(),
                           LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                           hour, ninvitados.getText(), autorizado.getText()));
                    inter.eliminaInvitados(report.getContrato(), report.getFecha());
                    for (int i = 0; i < table.getItems().size(); i++) {
                         Invitado inv = (Invitado) table.getItems().get(i);
                         inter.addInvad(inv.getNombre(), inv.getApellido(), inv.getCedula(), inv.getContrato(), fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    }
                    imprimeFactura(inter);
                    auto.updateTable();
               }
          }
     }

     public void imprimeFactura(oasisrimbd inter) throws IOException, RemoteException, NumberFormatException, NotBoundException {
          PrinterOptions p = new PrinterOptions();
          ArrayList<Plan> plan = inter.getPlans();

          p.resetAll();
          p.initialize();
          p.feedBack((byte) 2);
          p.color(0);
          p.alignCenter();
          p.setText("Oasis Club S.A");
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
          p.setText("Cedula \t\t:" + client.getCedula());
          p.newLine();
          p.setText("Contrato \t:" + client.getContrato());
          p.newLine();
          p.setText("Plan \t\t:" + client.getPlan());
          p.newLine();
          Boolean t= false;
            for (Plan o : plan) {
               if (o.getPlan().equals(client.getPlan())) {
                    t = true;
               }
               
          }
            if (t) {
                    p.setText("Area Picnic \t:" + "SI");
                    p.newLine();
               } else {
                    if (!t) {
                         p.setText("Area Picnic \t:" + "NO");
                         p.newLine();
                    }
               }
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
          p.setText("1" + "\t" + "Control de" + "\t" + "1" + "\t" + "0");
          p.newLine();
          p.setText("  " + "\t" + "Acceso");
          p.newLine();
          p.setText("  " + "\t" + "(AUTORIZADO)");
          p.newLine();
          p.setText("  " + "\t" + "Seguridad");
          p.newLine();
          p.setText("  " + "\t" + "Invitados: " + (Integer.parseInt(ninvitados.getText()) + table.getItems().size()));
          p.newLine();
          if (table.getItems().size() > 0) {
               p.setText("1" + "\t" + "Pase Inv Adic" + "\t" + table.getItems().size() + "\t" + inter.precio());
          }
          p.newLine();
          p.addLineSeperator();
          p.newLine();
          p.setText("Precio Total" + "\t" + "\t" + table.getItems().size() * Integer.parseInt(inter.precio()));
          p.newLine();
          p.addLineSeperator();
          p.feed((byte) 3);
          p.finit();
          print(p.finalCommandSet().getBytes());
          System.out.println(p.finalCommandSet());
     }

     public void back() {
          if (menu != null) {
               menu.aux.getChildren().clear();
               menu.main.setVisible(true);
               menu.main.toFront();
          }
          if (auto != null) {
               auto.aux.getChildren().clear();
               auto.main.setVisible(true);
               auto.main.toFront();
          }

          if (reserva != null) {
               reserva.aux.getChildren().clear();
               reserva.main.setVisible(true);
               reserva.main.toFront();
          }
     }

     public void print(byte[] b) throws IOException {
          Stage stage = new Stage();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/selectPrinter.fxml"));
          Parent root = loader.load();
          Scene scene = new Scene(root);
          SelectPrinterController controller = loader.getController();
          controller.a = b;
          controller.autorizar = myController;
          stage.setScene(scene);
          stage.show();
     }

     public void updateClient() throws RemoteException, NotBoundException {
          if (!(txtcedula.equals(client.getCedula()) && txtcontrato.equals(client.getContrato()) && txtnombre.equals(client.getNombre()) && txtplan.equals(client.getPlan()))) {
               Registry reg = LocateRegistry.getRegistry(host, 27019);
               oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
               Cliente c = new Cliente(client.getCedula(), client.getNombre(), client.getContrato(), client.getPlan(), client.getBanco(), client.getRestringido());
               c.setNombre(txtnombre.getText());
               c.setCedula(txtcedula.getText());
               c.setContrato(txtcontrato.getText());
               c.setPlan(txtplan.getText());
               inter.actualizaCliente(client, c);
               this.client = c;
          }
     }

     public void initUpdateData() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          client = inter.clientePorContrato(report.getContrato());
          update();
          ninvitados.setText(report.getInvitados());
     }

     public void initDataInvitadosAdicional() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<Invitado> inv = inter.getInvitados();
          for (Invitado in : inv) {
               if (in.getContrato().equals(report.getContrato())) {
                    if (in.getFecha().equals(report.getFecha())) {
                         table.getItems().add(in);
                    }
               }
          }
          inViejos = table.getItems().size();
     }

}
