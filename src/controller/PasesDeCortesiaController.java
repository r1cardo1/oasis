/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.PaseCortesia;
import classes.PrinterOptions;
import classes.Usuario;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import oasiscrud.oasisrimbd;

public class PasesDeCortesiaController implements Initializable {

     @FXML
     TextField filtro, tnombre, tcedula, ttelefono, tcodigo, tinvitados;
     @FXML
     DatePicker ddfecha, desde, hasta, dfecha;
     @FXML
     TableView<PaseCortesia> tabla;
     @FXML
     TableColumn<PaseCortesia, String> nombre, cedula, telefono, fecha, invitados, codigo;
     @FXML
     ComboBox<String> combo;
     @FXML
     HBox hfecha, hfiltro, hrango;
     String host;
     Usuario usuario;
     Boolean editar = false;
     int white = 1;
     PasesDeCortesiaController myController;
     MainMenuController menu;

     @Override
     public void initialize(URL url, ResourceBundle rb) {
          configuraTabla();
          configuraComboBox();
     }

     public void configuraComboBox() {
          combo.getItems().addAll("TODOS", "NOMBRE", "CEDULA", "FECHA", "RANGO DE FECHA");
          combo.getSelectionModel().selectFirst();
          combo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
               @Override
               public void changed(ObservableValue observableValue, String old, String neww) {
                    try {
                         changePane(old, neww);
                    } catch (RemoteException | NotBoundException ex) {
                         Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
               }
          });
     }

     public void configuraTabla() {
          nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
          cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
          telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
          fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
          invitados.setCellValueFactory(new PropertyValueFactory<>("invitados"));
          codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
     }

     public void iniciaDatos() throws RemoteException, NotBoundException {
          tabla.getItems().clear();
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<PaseCortesia> pases = inter.damePasesCortesia();
          for (PaseCortesia p : pases) {
               tabla.getItems().add(p);
          }
     }

     public void desdeFecha() {
          final Callback<DatePicker, DateCell> dayCellFactory
                 = new Callback<DatePicker, DateCell>() {
               @Override
               public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                         @Override
                         public void updateItem(LocalDate item, boolean empty) {
                              super.updateItem(item, empty);

                              if (item.isBefore(
                                     desde.getValue().plusDays(1))) {
                                   setDisable(true);
                                   setStyle("-fx-background-color: #ffc0cb;");
                              }
                         }
                    };
               }
          };
          hasta.setDayCellFactory(dayCellFactory);

     }

     public void changePane(String old, String neww) throws RemoteException, NotBoundException {
          switch (old) {
               case "NOMBRE":
                    if (!neww.equals("CEDULA")) {
                         hfiltro.setVisible(false);
                    }
                    break;
               case "CEDULA":
                    if (!neww.equals("NOMBRE")) {
                         hfiltro.setVisible(false);
                    }
                    break;
               case "FECHA":
                    hfecha.setVisible(false);
                    break;
               case "RANGO DE FECHA":
                    hrango.setVisible(false);
                    break;
          }
          switch (neww) {
               case "NOMBRE":
                    if (!old.equals("CEDULA")) {
                         hfiltro.setVisible(true);
                    }
                    filtro.setOnKeyReleased((KeyEvent event) -> {
                         if (event.getCode() == KeyCode.ENTER) {
                              try {
                                   buscaPorCedula();
                              } catch (RemoteException | NotBoundException ex) {
                                   Logger.getLogger(PasesDeCortesiaController.class.getName()).log(Level.SEVERE, null, ex);
                              }
                         }
                    });
                    break;
               case "CEDULA":
                    if (!old.equals("NOMBRE")) {
                         hfiltro.setVisible(true);
                    }
                    filtro.setOnKeyReleased((KeyEvent event) -> {
                         if (event.getCode() == KeyCode.ENTER) {
                              try {
                                   buscaPorNombre();
                              } catch (RemoteException | NotBoundException ex) {
                                   Logger.getLogger(PasesDeCortesiaController.class.getName()).log(Level.SEVERE, null, ex);
                              }
                         }
                    });
                    break;
               case "FECHA":
                    hfecha.setVisible(true);
                    break;
               case "RANGO DE FECHA":
                    hrango.setVisible(true);
                    break;
               case "TODAS":
                    iniciaDatos();
                    break;

          }
     }

     public void buscaPorCedula() throws RemoteException, NotBoundException {
          tabla.getItems().clear();
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<PaseCortesia> pases = inter.damePasesPorCedula(filtro.getText().toUpperCase());
          pases.forEach((p) -> {
               tabla.getItems().add(p);
          });
     }

     public void buscaPorNombre() throws RemoteException, NotBoundException {
          tabla.getItems().clear();
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<PaseCortesia> pases = inter.damePasesPorNombre(filtro.getText().toUpperCase());
          pases.forEach((p) -> {
               tabla.getItems().add(p);
          });
     }

     public void buscaPorFecha() throws RemoteException, NotBoundException {
          tabla.getItems().clear();
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<PaseCortesia> pases = inter.damePasesPorFecha(dfecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
          pases.forEach((p) -> {
               tabla.getItems().add(p);
          });
     }

     public void buscaPorRangoDeFecha() throws RemoteException, NotBoundException {
          tabla.getItems().clear();
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<PaseCortesia> pases = inter.damePasesPorRangoDeFecha(desde.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE), hasta.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
          pases.forEach((p) -> {
               tabla.getItems().add(p);
          });
     }

     public void nuevoPase() throws RemoteException, NotBoundException {
          if (editar) {
               if (!tnombre.getText().isEmpty()
                      && !tcedula.getText().isEmpty()
                      && !ttelefono.getText().isEmpty()
                      && !(ddfecha.getValue() == null)
                      && !tinvitados.getText().isEmpty()
                      && !tcodigo.getText().isEmpty()) {
                    Registry reg = LocateRegistry.getRegistry(host, 27019);
                    oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
                    inter.actualizaPaseCortesia(new PaseCortesia(tnombre.getText().toUpperCase(),
                           tcedula.getText().toUpperCase(),
                           ttelefono.getText().toUpperCase(),
                           ddfecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE),
                           tinvitados.getText().toUpperCase(),
                           usuario.getNombre() + " " + usuario.getApellido(),
                           LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                           tcodigo.getText().toUpperCase()),
                           tabla.getSelectionModel().getSelectedItem());
                    iniciaDatos();
                    Alert a = new Alert(AlertType.INFORMATION);
                    a.setTitle("Informacion");
                    a.setHeaderText("Pase guardado con exito");
                    a.show();
               } else {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Todos los campos deben estar llenos");
                    a.show();
               }
          } else {
               if (!tnombre.getText().isEmpty()
                      && !tcedula.getText().isEmpty()
                      && !ttelefono.getText().isEmpty()
                      && !(ddfecha.getValue() == null)
                      && !tinvitados.getText().isEmpty()
                      && !tcodigo.getText().isEmpty()) {
                    Registry reg = LocateRegistry.getRegistry(host, 27019);
                    oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
                    inter.nuevoPaseCortesia(new PaseCortesia(tnombre.getText().toUpperCase(),
                           tcedula.getText().toUpperCase(),
                           ttelefono.getText().toUpperCase(),
                           ddfecha.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE),
                           tinvitados.getText().toUpperCase(),
                           usuario.getNombre() + " " + usuario.getApellido(),
                           LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                           tcodigo.getText().toUpperCase()));
                    iniciaDatos();
                    Alert a = new Alert(AlertType.INFORMATION);
                    a.setTitle("Informacion");
                    a.setHeaderText("Pase guardado con exito");
                    a.show();
               } else {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText("Todos los campos deben estar llenos");
                    a.show();
               }
          }
     }

     public void eliminaPase() throws RemoteException, NotBoundException {
          if (!tabla.getSelectionModel().isEmpty()) {
               Alert a = new Alert(AlertType.CONFIRMATION);
               a.setTitle("Confirmar");
               a.setHeaderText("Esta seguro de eliminar el pase seleccionado?");
               Optional<ButtonType> b = a.showAndWait();
               if (b.get() == ButtonType.OK) {
                    Registry reg = LocateRegistry.getRegistry(host, 27019);
                    oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
                    inter.eliminaPaseCortesia(tabla.getSelectionModel().getSelectedItem());
                    Alert c = new Alert(AlertType.INFORMATION);
                    c.setTitle("Informacion");
                    c.setHeaderText("Pase eliminado con exito");
                    c.show();
                    iniciaDatos();
               }
          }
     }

     public void editaPase() {
          if (!tabla.getSelectionModel().isEmpty()) {
               editar = true;
               tabla.setDisable(true);
               PaseCortesia pase = tabla.getSelectionModel().getSelectedItem();
               tnombre.setText(pase.nombre);
               tcedula.setText(pase.cedula);
               ttelefono.setText(pase.telefono);
               ddfecha.setValue(LocalDate.parse(pase.fecha));
               tcodigo.setText(pase.codigo);
               tinvitados.setText(pase.invitados);
          }
     }

     public void generaPase() throws RemoteException, NotBoundException, IOException {
          if (!tabla.getSelectionModel().isEmpty()) {
               PaseCortesia pase = tabla.getSelectionModel().getSelectedItem();
               Registry reg = LocateRegistry.getRegistry(host, 27019);
               oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
               PrinterOptions p = new PrinterOptions();
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
               p.setText("Fecha \t\t:" + pase.fecha);
               p.newLine();
               p.setText("Cliente \t:" + pase.nombre);
               p.newLine();
               p.setText("Cedula \t\t:" + pase.cedula);
               p.newLine();
               p.setText("Codigo \t:" + pase.codigo);
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
               p.setText("1" + "\t" + "Control de" + "\t" + "1" + "\t" + "0");
               p.newLine();
               p.setText("  " + "\t" + "Acceso");
               p.newLine();
               p.setText("  " + "\t" + "(PASE CORTESIA)");
               p.newLine();
               p.setText("  " + "\t" + "Seguridad");
               p.newLine();
               p.setText("  " + "\t" + "Invitados: " + pase.invitados);
               p.newLine();
               p.addLineSeperator();
               p.newLine();
               p.setText("Gracias por su visita!");
               p.newLine();
               p.addLineSeperator();
               p.feed((byte) 3);
               p.finit();
               print(p.finalCommandSet().getBytes());
               System.out.println(p.finalCommandSet());
          }
     }

     public void print(byte[] b) throws IOException {
          Stage stage = new Stage();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/selectPrinter.fxml"));
          Parent root = loader.load();
          Scene scene = new Scene(root);
          SelectPrinterController controller = loader.getController();
          controller.a = b;
          stage.setScene(scene);
          stage.show();
     }

     public void exportPDF() throws RemoteException, NotBoundException, FileNotFoundException, IOException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
          FileChooser file = new FileChooser();
          file.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documento PDF", " *.pdf"));
          File f = file.showSaveDialog(null);
          PdfWriter writer = new PdfWriter(f.getAbsolutePath());
          PdfDocument pdf = new PdfDocument(writer);
          pdf.setDefaultPageSize(PageSize.LETTER);
          Document document = new Document(pdf);
          com.itextpdf.layout.element.Image img = new com.itextpdf.layout.element.Image(ImageDataFactory.create(getClass().getResource("/images/pdf-logo.png")));
          img.setHorizontalAlignment(HorizontalAlignment.CENTER);
          document.add(img);
          generaTitulo(document, "Lista de Pases de Cortesia");
          document.add(new Paragraph("\n"));
          Table t = generaCabezera(Arrays.asList("Nombre", "Cedula", "Telefono", "Codigo", "Invitados", "Fecha"));
          document.add(t);
          for (int i = 0; i < tabla.getItems().size(); i++) {
               generaPaseCortesia(document, tabla.getItems().get(i));
               white *= -1;
          }

          document.close();
     }

     private void generaTitulo(Document document, String text) throws IOException {
          Table t = new Table(1);
          Cell c = new Cell();
          c.setTextAlignment(TextAlignment.CENTER);
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setFontSize(18);
          c.setFontColor(Color.BLACK);
          c.add("\n");
          c.add(text);
          c.setBorder(Border.NO_BORDER);
          c.setBorderBottom(new SolidBorder(Color.GRAY, 1));
          t.addCell(c);
          document.add(t);

     }

     private Table generaCabezera(List<String> l) throws IOException {
          Table t = new Table(l.size());
          for (String s : l) {
               t.addCell(generaCeldaNegra(s));
          }
          return t;
     }

     public Cell generaCeldaNegra(String txt) throws IOException {
          Cell c = new Cell();
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setFontSize(8);
          c.setTextAlignment(TextAlignment.CENTER);
          c.setBackgroundColor(Color.BLACK);
          c.setFontColor(Color.WHITE);
          c.add(txt);
          return c;

     }

     private Cell generaCeldaNormal(String txt) throws IOException {
          Cell c = new Cell();
          c.setBorder(Border.NO_BORDER);
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setFontSize(8);
          c.setTextAlignment(TextAlignment.CENTER);
          if (txt != null) {
               c.add(txt);
          }
          return c;
     }

     public void generaPaseCortesia(Document doc, PaseCortesia res) throws IOException {
          Table t = new Table(6);
          if (white < 0) {
               t.setBackgroundColor(Color.LIGHT_GRAY);
          }
          t.addCell(generaCeldaNormal(res.nombre));
          t.addCell(generaCeldaNormal(res.cedula));
          t.addCell(generaCeldaNormal(res.telefono));
          t.addCell(generaCeldaNormal(res.codigo));
          t.addCell(generaCeldaNormal(res.invitados));
          t.addCell(generaCeldaNormal(res.fecha));
          doc.add(t);
     }

}
