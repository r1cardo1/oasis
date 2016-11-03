/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Asistencia;
import classes.Cliente;
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
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import oasiscrud.oasisrimbd;

/**
 * FXML Controller class
 *
 * @author RicardoMarcano
 */
public class ReporteDiarioController implements Initializable {

     ReporteDiarioController mycontroller;

     ArrayList<ReporteMesa> apMesas;
     ArrayList<ReporteMesa> pases;

     String host;
     @FXML
     DatePicker date;
     int white = 1;

     @Override
     public void initialize(URL url, ResourceBundle rb) {
          initDate();
     }

     public void initDate() {
          date.setValue(LocalDate.now());
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
          tc.addCell(generaCabezera("Reporte diario del dia " + date.getValue().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault())
                 + " " + date.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE)));
          document.add(tc);

          generaTitulo(document, "Detalles de asistencias General");
          generaTablaAsistencia(document);

          document.close();
          System.out.println(f.getAbsolutePath());
          close(evt);
     }

     private void generaTablaAsistencia(Document doc) throws IOException, RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<Asistencia> asist = inter.asistenciaPorFecha(date.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
          int inv = 0, invad = 0;
          for (Asistencia a : asist) {
               inv += Integer.parseInt(a.getInvitados());
               invad += Integer.parseInt(a.getInvad());
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
          c.add(Integer.toString(invad));
          c.add("\n");
          tc.addCell(c);
          doc.add(tc);
          ArrayList<String> l = new ArrayList<>(Arrays.asList("Cedula", "Cliente", "Contrato", "Plan", "Fecha", "Invitados", "Inv Adicionales"));
          tc = generaCabezera(l);
          doc.add(tc);
          for (Asistencia a : asist) {
               generaCliente(doc, a);
               white *= -1;
          }

     }

     private void generaCliente(Document doc, Asistencia asist) throws IOException, RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          Cliente c = inter.clientePorContrato(asist.getContrato());
          Table tc = new Table(7);
          tc.addCell(generaCeldaNormal(c.getCedula()));
          tc.addCell(generaCeldaNormal(c.getNombre()));
          tc.addCell(generaCeldaNormal(asist.getContrato()));
          tc.addCell(generaCeldaNormal(c.getPlan()));
          tc.addCell(generaCeldaNormal(asist.getFecha()));
          tc.addCell(generaCeldaNormal(asist.getInvitados()));
          tc.addCell(generaCeldaNormal(asist.getInvad()));
          doc.add(tc);

     }

     private Cell generaCeldaNormal(String txt) throws IOException {
          Cell c = new Cell();
          if (white > 0) {
               c.setBackgroundColor(Color.LIGHT_GRAY);
          }
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

}
