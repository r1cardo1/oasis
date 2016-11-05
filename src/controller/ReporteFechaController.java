/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Asistencia;
import classes.Autorizado;
import classes.Busqueda;
import classes.Cliente;
import classes.Invitado;
import classes.Login;
import classes.ReporteMesa;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import oasiscrud.oasisrimbd;

/**
 * FXML Controller class
 *
 * @author RicardoMarcano
 */
public class ReporteFechaController implements Initializable {

     ReporteFechaController mycontroller;

     ArrayList<ReporteMesa> apMesas;
     ArrayList<ReporteMesa> pases;

     String host;
     @FXML
     DatePicker from, to;
     int white = 1;

     @Override
     public void initialize(URL url, ResourceBundle rb) {
          initDate();
     }

     public void initDate() {
          from.setValue(LocalDate.now());
          fromDate();
     }

     public void close(ActionEvent evt) {
          Stage stage;
          Button b = (Button) evt.getSource();
          stage = (Stage) b.getScene().getWindow();
          stage.close();
     }

     public void generaPDF(ActionEvent evt) throws FileNotFoundException, MalformedURLException, IOException, RemoteException, NotBoundException {

          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");

          FileChooser file = new FileChooser();
          file.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documento PDF", " *.PDF"));
          File f = file.showSaveDialog(null);
          PdfWriter writer = new PdfWriter(f.getAbsolutePath());

          PdfDocument pdf = new PdfDocument(writer);
          pdf.setDefaultPageSize(PageSize.LETTER.rotate());

          Document document = new Document(pdf);

          Cell c;
          Table tc;
          Paragraph p = new Paragraph();
          Image img = new Image(ImageDataFactory.create(getClass().getResource("/images/pdf-logo.png")));
          img.setHorizontalAlignment(HorizontalAlignment.CENTER);
          document.add(img);

          document.add(new Paragraph("\n"));

          tc = new Table(1);
          tc.addCell(generaCabezera("Reporte desde el dia "
                 + from.getValue().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " "
                 + from.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE) + " hasta el dia "
                 + to.getValue().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " "
                 + to.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE)));
          document.add(tc);

          generaTitulo(document, "Detalles de asistencias General");
          generaTablaAsistencia(document);
          pdf.addNewPage();
          document.add(new AreaBreak());
          generaTitulo(document, "Detalles de Aperturas de mesas");
          generaTablaAperturaMesas(document);
          pdf.addNewPage();
          document.add(new AreaBreak());
          generaTitulo(document, "Detalles de Pases generados");
          generaTablaPases(document);
          pdf.addNewPage();
          document.add(new AreaBreak());
          generaTitulo(document, "Detalles de Autorizaciones");
          generaTablaAutorizaciones(document);
          pdf.addNewPage();
          document.add(new AreaBreak());
          generaTitulo(document, "Detalles de accesos al sistema");
          generaTablaAcceso(document);
          pdf.addNewPage();
          document.add(new AreaBreak());
          generaTitulo(document, "Detalles de busquedas en el sistema");
          generaTablaBusqueda(document);

          document.close();
          System.out.println(f.getAbsolutePath());
          close(evt);
     }

     private void generaTablaAsistencia(Document doc) throws IOException, RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<Asistencia> asist = inter.asistenciaPorRangoDeFecha(from.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE),
                 to.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
          ArrayList<Invitado> invad = inter.getInvitados();
          List<String> ls = new ArrayList<>();
          for (Asistencia o : asist) {
               ls.add(o.getContrato());
          }
          invad.removeIf(r -> !(LocalDate.parse(r.getFecha()).plusDays(1).isAfter(from.getValue())
                 && LocalDate.parse(r.getFecha()).minusDays(1).isBefore(to.getValue())
                 && ls.contains(r.getContrato())));
          int inv = 0, invadd = invad.size();
          for (Asistencia a : asist) {
               inv += Integer.parseInt(a.getInvitados());
          }

          Table tc = new Table(2);
          Cell c = new Cell();
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setBorder(Border.NO_BORDER);
          c.add("\n");
          c.add("Numero de titulares");
          c.add("Numero de invitados");
          c.add("Numero de invitados Adicionales");
          c.add("\n");
          tc.addCell(c);
          c = new Cell();
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setBorder(Border.NO_BORDER);
          c.add("\n");
          c.add(Integer.toString(asist.size()));
          c.add(Integer.toString(inv));
          c.add(Integer.toString(invadd));
          c.add("\n");
          tc.addCell(c);
          doc.add(tc);
          ArrayList<String> l = new ArrayList<>(Arrays.asList("Cedula", "Cliente", "Contrato", "Plan", "Fecha", "Invitados", "Inv Adicionales"));
          tc = generaCabezera(l);
          doc.add(tc);
          for (Asistencia a : asist) {
               generaClienteAsistencia(doc, a);
               white *= -1;
          }

     }

     private void generaClienteAsistencia(Document doc, Asistencia asist) throws IOException, RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Cliente c = inter.clientePorContrato(asist.getContrato());
          ArrayList<Invitado> inv = inter.getInvitados();
          inv.removeIf(r -> !r.getContrato().equals(asist.getContrato()));
          Table tc = new Table(7);
          if (white < 0) {
               tc.setBackgroundColor(Color.LIGHT_GRAY);
          }
          tc.addCell(generaCeldaNormal(c.getCedula()));
          tc.addCell(generaCeldaNormal(c.getNombre()));
          tc.addCell(generaCeldaNormal(asist.getContrato()));
          tc.addCell(generaCeldaNormal(c.getPlan()));
          tc.addCell(generaCeldaNormal(asist.getFecha()));
          tc.addCell(generaCeldaNormal(asist.getInvitados()));
          tc.addCell(generaCeldaInvitados(inv));
          doc.add(tc);
     }

     public Cell generaCeldaInvitados(ArrayList<Invitado> l) throws IOException {
          Cell c = new Cell();
          c.setBorder(Border.NO_BORDER);
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setFontSize(8);
          c.setTextAlignment(TextAlignment.LEFT);
          for (Invitado r : l) {
               if (LocalDate.parse(r.getFecha()).plusDays(1).isAfter(from.getValue())
                      && LocalDate.parse(r.getFecha()).minusDays(1).isBefore(to.getValue())) {
                    c.add(r.getNombre() + " " + r.getApellido());
               }
          }
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

     private Table generaCabezera(List<String> l) throws IOException {
          Table t = new Table(l.size());
          for (String s : l) {
               t.addCell(generaCeldaNegra(s));
          }
          return t;
     }

     private Cell generaCabezera(String string) throws IOException {
          Cell c = new Cell();
          c.setHorizontalAlignment(HorizontalAlignment.CENTER);
          c.setTextAlignment(TextAlignment.CENTER);
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setFontSize(20);
          c.setBorder(Border.NO_BORDER);
          c.setBorderBottom(new SolidBorder(Color.GRAY, 1));
          c.add(string);
          return c;
     }

     private void generaTitulo(Document document, String text) throws IOException {
          Table t = new Table(1);
          Cell c = new Cell();
          c.setTextAlignment(TextAlignment.CENTER);
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setFontSize(16);
          c.setFontColor(Color.GRAY);
          c.add("\n");
          c.add(text);
          c.setBorder(Border.NO_BORDER);
          t.addCell(c);
          document.add(t);
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

     private void generaTablaAperturaMesas(Document doc) throws RemoteException, NotBoundException, IOException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<ReporteMesa> open = inter.apMesasFechas(from.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE), to.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
          ArrayList<Invitado> invad = inter.getInvitados();
          List<String> ls = new ArrayList<>();
          for (ReporteMesa o : open) {
               ls.add(o.getContrato());
          }
          invad.removeIf(r -> !(LocalDate.parse(r.getFecha()).plusDays(1).isAfter(from.getValue())
                 && LocalDate.parse(r.getFecha()).minusDays(1).isBefore(to.getValue())
                 && ls.contains(r.getContrato())));
          int inv = 0, invadd = invad.size();
          for (ReporteMesa r : open) {
               inv += Integer.parseInt(r.getInvitados());
          }
          if (open.size() == 0) {
               inv = 0;
               invadd = 0;
          }
          Table tc = new Table(2);
          Cell c = new Cell();
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setBorder(Border.NO_BORDER);
          c.add("\n");
          c.add("Numero de titulares");
          c.add("Numero de invitados");
          c.add("Numero de invitados Adicionales");
          c.add("\n");
          tc.addCell(c);
          c = new Cell();
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setBorder(Border.NO_BORDER);
          c.add("\n");
          c.add(Integer.toString(open.size()));
          c.add(Integer.toString(inv));
          c.add(Integer.toString(invadd));
          c.add("\n");
          tc.addCell(c);
          doc.add(tc);
          List<String> l = Arrays.asList("Usuario", "Cliente", "Cedula", "Contrato", "Plan", "Fecha", "Invitados", "Inv. Adicional");
          tc = generaCabezera(l);
          doc.add(tc);
          for (ReporteMesa r : open) {
               generaClienteApMesa(doc, r);
               white *= -1;
          }

     }

     private void generaClienteApMesa(Document doc, ReporteMesa asist) throws IOException, RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Cliente c = inter.clientePorContrato(asist.getContrato());
          ArrayList<Invitado> inv = inter.getInvitados();
          inv.removeIf(r -> !r.getContrato().equals(asist.getContrato()));
          Table tc = new Table(8);
          if (white < 0) {
               tc.setBackgroundColor(Color.LIGHT_GRAY);
          }
          tc.addCell(generaCeldaNormal(asist.getUsuario()));
          tc.addCell(generaCeldaNormal(c.getNombre()));
          tc.addCell(generaCeldaNormal(c.getCedula()));
          tc.addCell(generaCeldaNormal(asist.getContrato()));
          tc.addCell(generaCeldaNormal(c.getPlan()));
          tc.addCell(generaCeldaNormal(asist.getFecha()));
          tc.addCell(generaCeldaNormal(asist.getInvitados()));
          tc.addCell(generaCeldaInvitados(inv));
          doc.add(tc);
     }

     private void generaTablaPases(Document doc) throws RemoteException, NotBoundException, IOException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<ReporteMesa> open = inter.pasesFechas(from.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE),
                 to.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
          ArrayList<Invitado> invad = inter.getInvitados();
          List<String> ls = new ArrayList<>();
          for (ReporteMesa o : open) {
               ls.add(o.getContrato());
          }
          invad.removeIf(r -> !(LocalDate.parse(r.getFecha()).plusDays(1).isAfter(from.getValue())
                 && LocalDate.parse(r.getFecha()).minusDays(1).isBefore(to.getValue())
                 && ls.contains(r.getContrato())));
          int inv = 0, invadd = invad.size();
          for (ReporteMesa r : open) {
               inv += Integer.parseInt(r.getInvitados());
          }
          if (open.size() == 0) {
               inv = 0;
               invadd = 0;
          }
          Table tc = new Table(2);
          Cell c = new Cell();
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setBorder(Border.NO_BORDER);
          c.add("\n");
          c.add("Numero de titulares");
          c.add("Numero de invitados");
          c.add("Numero de invitados Adicionales");
          c.add("\n");
          tc.addCell(c);
          c = new Cell();
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setBorder(Border.NO_BORDER);
          c.add("\n");
          c.add(Integer.toString(open.size()));
          c.add(Integer.toString(inv));
          c.add(Integer.toString(invadd));
          c.add("\n");
          tc.addCell(c);
          doc.add(tc);
          List<String> l = Arrays.asList("Usuario", "Cliente", "Cedula", "Contrato", "Plan", "Fecha", "Invitados", "Inv. Adicional");
          tc = generaCabezera(l);
          doc.add(tc);
          for (ReporteMesa r : open) {
               generaClienteApMesa(doc, r);
               white *= -1;
          }
     }

     private void generaTablaAutorizaciones(Document doc) throws RemoteException, NotBoundException, IOException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<Autorizado> open = inter.AutorizadoFechas(from.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE),
                 to.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
          ArrayList<Invitado> invad = inter.getInvitados();
          List<String> ls = new ArrayList<>();
          for (Autorizado o : open) {
               ls.add(o.getContrato());
          }
          invad.removeIf(r -> !(LocalDate.parse(r.getFecha()).plusDays(1).isAfter(from.getValue())
                 && LocalDate.parse(r.getFecha()).minusDays(1).isBefore(to.getValue())
                 && ls.contains(r.getContrato())));
          int inv = 0, invadd = invad.size();
          for (Autorizado r : open) {
               inv += Integer.parseInt(r.getInvitados());
          }
          if (open.size() == 0) {
               inv = 0;
               invadd = 0;
          }
          Table tc = new Table(2);
          Cell c = new Cell();
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setBorder(Border.NO_BORDER);
          c.add("\n");
          c.add("Numero de titulares");
          c.add("Numero de invitados");
          c.add("Numero de invitados Adicionales");
          c.add("\n");
          tc.addCell(c);
          c = new Cell();
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setBorder(Border.NO_BORDER);
          c.add("\n");
          c.add(Integer.toString(open.size()));
          c.add(Integer.toString(inv));
          c.add(Integer.toString(invadd));
          c.add("\n");
          tc.addCell(c);
          doc.add(tc);
          List<String> l = Arrays.asList("Usuario", "Autorizado", "Cliente", "Cedula", "Contrato", "Plan", "Fecha", "Invitados", "Inv. Adicional");
          tc = generaCabezera(l);
          doc.add(tc);
          for (Autorizado r : open) {
               generaClienteAutorizado(doc, r);
               white *= -1;
          }
     }

     private void generaClienteAutorizado(Document doc, Autorizado asist) throws IOException, RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Cliente c = inter.clientePorContrato(asist.getContrato());
          ArrayList<Invitado> inv = inter.getInvitados();
          inv.removeIf(r -> !r.getContrato().equals(asist.getContrato()));
          Table tc = new Table(9);
          if (white < 0) {
               tc.setBackgroundColor(Color.LIGHT_GRAY);
          }
          tc.addCell(generaCeldaNormal(asist.getUsuario()));
          tc.addCell(generaCeldaNormal(asist.getAutorizado()));
          tc.addCell(generaCeldaNormal(c.getNombre()));
          tc.addCell(generaCeldaNormal(c.getCedula()));
          tc.addCell(generaCeldaNormal(asist.getContrato()));
          tc.addCell(generaCeldaNormal(c.getPlan()));
          tc.addCell(generaCeldaNormal(asist.getFecha()));
          tc.addCell(generaCeldaNormal(asist.getInvitados()));
          tc.addCell(generaCeldaInvitados(inv));
          doc.add(tc);
     }

     private void generaTablaAcceso(Document doc) throws RemoteException, NotBoundException, IOException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Table tc;
          ArrayList<Login> log = inter.getLogLogins();
          log.removeIf(r -> !(LocalDate.parse(r.getFecha()).plusDays(1).isAfter(from.getValue())
                 && LocalDate.parse(r.getFecha()).minusDays(1).isBefore(to.getValue())));
          List<String> l = Arrays.asList("Nombre", "Apellido", "Usuario", "Fecha", "Hora");
          tc = generaCabezera(l);
          doc.add(tc);
          for (Login ll : log) {
               generaLogin(doc, ll);
               white *= -1;
          }

     }

     private void generaLogin(Document doc, Login ll) throws IOException {
          Table tc = new Table(5);
          if (white < 0) {
               tc.setBackgroundColor(Color.LIGHT_GRAY);
          }
          tc.addCell(generaCeldaNormal(ll.getNombre()));
          tc.addCell(generaCeldaNormal(ll.getApellido()));
          tc.addCell(generaCeldaNormal(ll.getUsuario()));
          tc.addCell(generaCeldaNormal(ll.getFecha()));
          tc.addCell(generaCeldaNormal(ll.getHora()));
          doc.add(tc);
     }

     private void generaTablaBusqueda(Document doc) throws RemoteException, NotBoundException, IOException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Table tc;
          ArrayList<Busqueda> log = inter.getAllSearch();
          log.removeIf(r -> !(LocalDate.parse(r.getFecha()).plusDays(1).isAfter(from.getValue())
                 && LocalDate.parse(r.getFecha()).minusDays(1).isBefore(to.getValue())));
          List<String> l = Arrays.asList("Usuario", "Tipo", "Filtro", "Fecha", "Hora");
          tc = generaCabezera(l);
          doc.add(tc);
          for (Busqueda ll : log) {
               generaBusqueda(doc, ll);
               white *= -1;
          }
     }

     private void generaBusqueda(Document doc, Busqueda ll) throws IOException {
          Table tc = new Table(5);
          if (white < 0) {
               tc.setBackgroundColor(Color.LIGHT_GRAY);
          }
          tc.addCell(generaCeldaNormal(ll.getUsuario()));
          tc.addCell(generaCeldaNormal(ll.getTipo()));
          tc.addCell(generaCeldaNormal(ll.getFiltro()));
          tc.addCell(generaCeldaNormal(ll.getFecha()));
          tc.addCell(generaCeldaNormal(ll.getHora()));
          doc.add(tc);
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
