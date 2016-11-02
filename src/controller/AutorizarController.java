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
import classes.PrinterOptions;
import classes.ReporteMesa;
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
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.event.ActionEvent;
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
    TextField txtnombre, txtcedula, txtcontrato, txtplan, ninvitados,  addnombre, addapellido, addcedula,autorizado;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
    }

    public void initData() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        txtnombre.setText(client.getNombre());
        txtcedula.setText(client.getCedula());
        txtcontrato.setText(client.getContrato());
        txtplan.setText(client.getPlan());
        fecha.setValue(LocalDate.now());
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
    
    public void autorizar() throws IOException, RemoteException, NotBoundException{
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        if (!ninvitados.getText().isEmpty()) {
            if(!inter.estaPresente(client)){
                updateClient();
                String hour;
                String us = user.getNombre() + " " + user.getApellido() + " " + user.getUsuario();                
                Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
                String ampm = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
                hour =  Integer.toString(time.get(Calendar.HOUR) == 0 ? 12 : time.get(Calendar.HOUR))
                                            + ":" + Integer.toString(time.get(Calendar.MINUTE))
                                            + ":" + Integer.toString(time.get(Calendar.SECOND))
                                            + ampm;
                inter.creaAsistencia(new Asistencia(ninvitados.getText(),LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),hour,client.getContrato(),Integer.toString(table.getItems().size()),user.getUsuario()));
                inter.autorizar(new Autorizado(user.getUsuario(),client.getCedula(),
                        client.getNombre(),client.getContrato(),
                        client.getPlan(),
                        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        hour,Integer.toString(Integer.parseInt(ninvitados.getText())+table.getItems().size()),autorizado.getText()));
                for (int i = 0; i < table.getItems().size(); i++) {
                    Invitado inv = (Invitado) table.getItems().get(i);
                    inter.addInvad(inv.getNombre(), inv.getApellido(), inv.getCedula(), inv.getContrato(), fecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
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
                p.setText("1" + "\t" + "Control de" + "\t" +  "1" + "\t" + "0");
                p.newLine();
                p.setText("  " + "\t" + "Acceso");
                p.newLine();
                p.setText("  " + "\t" + "(AUTORIZADO)");
                p.newLine();
                p.setText("  " + "\t" + "Seguridad");
                p.newLine();
                p.setText("  " + "\t" + "Invitados: " + ninvitados.getText());
                p.newLine();
                if(table.getItems().size()>0)
                    p.setText("2" + "\t" + "Pase Inv Adic" + "\t" +  table.getItems().size() + "\t" + inter.precio());
                p.newLine();
                p.addLineSeperator();
                p.newLine();
                p.setText("Precio Total" + "\t" + "\t" +  table.getItems().size()*Integer.parseInt(inter.precio()));
                p.newLine();
                p.addLineSeperator();
                p.feed((byte) 3);
                p.finit();
               print(p.finalCommandSet().getBytes());
               System.out.println(p.finalCommandSet());         
            }else{
                Alert a = new Alert(AlertType.INFORMATION);
                a.setTitle("Informacion");
                a.setContentText("Ya el cliente ingreso al club, para reimprimir el ticket dirijase a la seccion de visitas en la parte inferior de la lista de busqueda de clientes");
                a.show();
            }
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
          controller.autorizar = myController;
          stage.setScene(scene);
          stage.show();
    }
    
    public void updateClient() throws RemoteException, NotBoundException{
        if(!(txtcedula.equals(client.getCedula()) && txtcontrato.equals(client.getContrato()) && txtnombre.equals(client.getNombre()) && txtplan.equals(client.getPlan()))){
            Registry reg = LocateRegistry.getRegistry(host,27019);
            oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
            Cliente c = new Cliente(client.getCedula(),client.getNombre(),client.getContrato(),client.getPlan(),client.getBanco(),client.getRestringido());
            c.setNombre(txtnombre.getText());
            c.setCedula(txtcedula.getText());
            c.setContrato(txtcontrato.getText());
            c.setPlan(txtplan.getText());
            inter.actualizaCliente(client, c);
            this.client=c;
        }
    }

}