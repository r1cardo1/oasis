/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Asistencia;
import classes.Cliente;
import classes.Invitado;
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
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import oasiscrud.oasisrimbd;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class VisitasController implements Initializable {

     @FXML
     TableColumn<Asistencia, String> cedula;
     @FXML
     TableColumn<Asistencia, String> nombre;
     @FXML
     TableColumn<Asistencia, String> contrato;
     @FXML
     TableColumn<Asistencia, String> plan;
     @FXML
     TableColumn<Asistencia, String> fecha;
     @FXML
     TableColumn<Asistencia, String> hora;
     @FXML
     TableColumn<Asistencia, String> ninvitados;
     @FXML
     TableView table;
     @FXML
     ComboBox<String> combo;
     @FXML
     DatePicker from;
     @FXML
     DatePicker to;
     @FXML
     DatePicker date;
     @FXML
     TextField filter;
     @FXML
     Pane rangeDate;
     @FXML
     Pane pdate;
     @FXML
     Pane pfilter;
     MainMenuController menu;
     String host;
     int white = 1;

     @Override
     public void initialize(URL url, ResourceBundle rb) {

          initCombo();
          date.setOnAction((ActionEvent) -> {
               try {
                    date();
               } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(VisitasController.class.getName()).log(Level.SEVERE, null, ex);
               }
          });
          to.setOnAction((ActionEvent) -> {
               try {
                    toDate();
               } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(VisitasController.class.getName()).log(Level.SEVERE, null, ex);
               }
          });
     }

     public void initTable() throws SQLException, RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
          nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
          contrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
          plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
          fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
          hora.setCellValueFactory(new PropertyValueFactory<>("hora"));
          ninvitados.setCellValueFactory(new PropertyValueFactory<>("invitados"));

          cedula.prefWidthProperty().bind(table.widthProperty().divide(8));
          nombre.prefWidthProperty().bind(table.widthProperty().divide(8));
          contrato.prefWidthProperty().bind(table.widthProperty().divide(8));
          plan.prefWidthProperty().bind(table.widthProperty().divide(8));
          fecha.prefWidthProperty().bind(table.widthProperty().divide(8));
          hora.prefWidthProperty().bind(table.widthProperty().divide(8));
          ninvitados.prefWidthProperty().bind(table.widthProperty().divide(8));

          ArrayList<Asistencia> asists = inter.visits();
          ResultSet aux;
          Asistencia asist = null;
          for (Asistencia as : asists) {
               Cliente client = inter.clientePorContrato(as.getContrato());
               if (client != null) {
                    asist = new Asistencia(as.getInvitados(), as.getFecha(), as.getHora(),
                           client.getCedula(), client.getNombre(), client.getContrato(), client.getPlan(), as.getInvad());
               }
               table.getItems().add(asist);
          }
     }

     public void reloadTable() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          table.getItems().clear();
          ArrayList<Asistencia> asists = inter.visits();
          ResultSet aux;
          Asistencia asist = null;
          for (Asistencia as : asists) {
               Cliente client = inter.clientePorContrato(as.getContrato());
               if (client != null) {
                    asist = new Asistencia(as.getInvitados(), as.getFecha(), as.getHora(),
                           client.getCedula(), client.getNombre(), client.getContrato(), client.getPlan(), as.getInvad());
               }
               table.getItems().add(asist);
          }
     }

     public void initCombo() {
          combo.getItems().addAll("NOMBRE", "CONTRATO", "CEDULA", "FECHA", "RANGO DE FECHA", "TODAS");
          combo.getSelectionModel().selectFirst();
          combo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
               @Override
               public void changed(ObservableValue observableValue, String old, String neww) {
                    changePane(old, neww);
               }
          });
     }

     public void changePane(String old, String neww) {
          switch (old) {
               case "NOMBRE":
                    if (!neww.equals("CONTRATO") || !neww.equals("CEDULA")) {
                         pfilter.setVisible(false);
                    }
                    break;
               case "CONTRATO":
                    if (!neww.equals("NOMBRE") || !neww.equals("CEDULA")) {
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
               case "NOMBRE":
                    if (!old.equals("CONTRATO") || !old.equals("CEDULA")) {
                         pfilter.setVisible(true);
                    }
                    break;
               case "CONTRATO":
                    if (!old.equals("NOMBRE") || !old.equals("CEDULA")) {
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
                    try {
                         reloadTable();
                    } catch (Exception ex) {
                         System.out.println(ex.getMessage());
                    }
          }
     }

     public void backAction(ActionEvent evt) {
          menu.aux.getChildren().clear();
          menu.main.setVisible(true);
          menu.main.toFront();
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

     }

     public void date() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          table.getItems().clear();
          ArrayList<Asistencia> asists = inter.visits();
          ResultSet aux;
          Asistencia asist = null;
          for (Asistencia as : asists) {
               if (date.getValue().isEqual(LocalDate.parse(as.getFecha()))) {
                    Cliente client = inter.clientePorContrato(as.getContrato());
                    if (client != null) {
                         asist = new Asistencia(as.getInvitados(), as.getFecha(), as.getHora(),
                                client.getCedula(), client.getNombre(), client.getContrato(), client.getPlan(), as.getInvad());
                    }
                    table.getItems().add(asist);
               }
          }

     }

     public void toDate() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          table.getItems().clear();
          ArrayList<Asistencia> asists = inter.visits();
          ResultSet aux;
          Asistencia asist = null;
          for (Asistencia as : asists) {
               if ((from.getValue().isEqual(LocalDate.parse(as.getFecha()))) || from.getValue().isBefore(LocalDate.parse(as.getFecha()))
                      && ((to.getValue().isEqual(LocalDate.parse(as.getFecha())) || to.getValue().isAfter(LocalDate.parse(as.getFecha()))))) {
                    Cliente client = inter.clientePorContrato(as.getContrato());
                    if (client != null) {
                         asist = new Asistencia(as.getInvitados(), as.getFecha(), as.getHora(),
                                client.getCedula(), client.getNombre(), client.getContrato(), client.getPlan(), as.getInvad());
                    }
                    table.getItems().add(asist);
               }
          }
     }

     public void search() throws RemoteException, NotBoundException {
          String f = combo.getSelectionModel().getSelectedItem();
          switch (f) {
               case "NOMBRE":
                    searchNombre();
                    break;
               case "CEDULA":
                    searchCedula();
                    break;
               case "CONTRATO":
                    searchContrato();
                    break;
          }
     }

     public void searchContrato() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          table.getItems().clear();
          ArrayList<Asistencia> asists = inter.visits();
          asists.removeIf(r -> !r.getContrato().contains(filter.getText().toUpperCase()));
          ResultSet aux;
          Asistencia asist = null;
          for (Asistencia as : asists) {
               Cliente client = inter.clientePorContrato(as.getContrato());
               if (client != null) {
                    asist = new Asistencia(as.getInvitados(), as.getFecha(), as.getHora(),
                           client.getCedula(), client.getNombre(), client.getContrato(), client.getPlan(), as.getInvad());
                    table.getItems().add(asist);
               }

          }
     }

     public void searchCedula() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          table.getItems().clear();
          ArrayList<Asistencia> asists = inter.visits();
          ResultSet aux;
          Asistencia asist = null;
          for (Asistencia as : asists) {
               Cliente client = inter.clientePorContrato(as.getContrato());
               if (client != null && client.getCedula().contains(filter.getText().toUpperCase())) {
                    asist = new Asistencia(as.getInvitados(), as.getFecha(), as.getHora(),
                           client.getCedula(), client.getNombre(), client.getContrato(), client.getPlan(), as.getInvad());
                    table.getItems().add(asist);
               }

          }
     }

     public void searchNombre() throws RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          table.getItems().clear();
          ArrayList<Asistencia> asists = inter.visits();
          ResultSet aux;
          Asistencia asist = null;
          Cliente client;
          for (Asistencia as : asists) {
               client = inter.clientePorContrato(as.getContrato());
               if (client != null && client.getNombre().contains(filter.getText().toUpperCase())) {
                    asist = new Asistencia(as.getInvitados(), as.getFecha(), as.getHora(),
                           client.getCedula(), client.getNombre(), client.getContrato(), client.getPlan(), as.getInvad());
                    table.getItems().add(asist);
               }
          }
     }

     public void exportExcel() {

          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open Resource File");
          fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
          fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS", "*.xls"));
          File f = fileChooser.showSaveDialog(null);

          try {
               String filename = f.getAbsolutePath();
               HSSFWorkbook workbook = new HSSFWorkbook();
               HSSFSheet sheet = workbook.createSheet("FirstSheet");
               int bool = 1;

               InputStream inputStream = getClass().getResourceAsStream("/images/excel-logo.jpg");

               byte[] imageBytes = IOUtils.toByteArray(inputStream);

               int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_JPEG);

               inputStream.close();

               CreationHelper helper = workbook.getCreationHelper();

               Drawing drawing = sheet.createDrawingPatriarch();

               ClientAnchor anchor = helper.createClientAnchor();

               anchor.setCol1(1);
               anchor.setRow1(0);

               Picture pict = drawing.createPicture(anchor, pictureureIdx);

               HSSFRow rowhead = sheet.createRow(8);

               rowhead.createCell(0);
               rowhead.createCell(1).setCellValue("Cedula");
               rowhead.createCell(2).setCellValue("Cliente");
               rowhead.createCell(3).setCellValue("Contrato");
               rowhead.createCell(4).setCellValue("Plan");
               rowhead.createCell(5).setCellValue("Fecha");
               rowhead.createCell(6).setCellValue("Hora");
               rowhead.createCell(7).setCellValue("Invitados");
               makeRowBold(workbook, rowhead);

               for (int i = 0; i < table.getItems().size(); i++) {
                    HSSFRow row = sheet.createRow(i + 9);
                    Asistencia a = (Asistencia) table.getItems().get(i);
                    row.createCell(1).setCellValue(a.getCedula());
                    row.createCell(2).setCellValue(a.getNombre());
                    row.createCell(3).setCellValue(a.getContrato());
                    row.createCell(4).setCellValue(a.getPlan());
                    row.createCell(5).setCellValue(a.getFecha());
                    row.createCell(6).setCellValue(a.getHora());
                    row.createCell(7).setCellValue(Integer.parseInt(a.getInvitados()));
                    centerRow(workbook, row);
               }
               autoSizeColumns(workbook);
               pict.resize();
               FileOutputStream fileOut = new FileOutputStream(filename);
               workbook.write(fileOut);
               fileOut.close();

          } catch (Exception ex) {
               ex.printStackTrace();
          }

     }

     public void makeRowBold(Workbook wb, Row row) {
          CellStyle style = wb.createCellStyle();//Create style
          Font font = wb.createFont();//Create font
          font.setBoldweight(Font.BOLDWEIGHT_BOLD);
          font.setColor(new HSSFColor.WHITE().getIndex());//Make font bold
          style.setFont(font);//set it to bold
          style.setAlignment(HorizontalAlignment.CENTER);
          style.setFillBackgroundColor(new HSSFColor.BLACK().getIndex());
          style.setFillForegroundColor(new HSSFColor.BLACK().getIndex());
          style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

          for (int i = 0; i < row.getLastCellNum(); i++) {
               if (!row.getCell(i).getStringCellValue().equals("")) {
                    row.getCell(i).setCellStyle(style);//Set the sty;e
               }
          }
     }

     public void centerRow(Workbook wb, Row row) {
          CellStyle style = wb.createCellStyle();
          style.setAlignment(HorizontalAlignment.CENTER);
          for (int i = 0; i < row.getLastCellNum(); i++) {
               if (row.getCell(i) != null) {
                    row.getCell(i).setCellStyle(style);//Set the sty;e
               }
          }
     }

     public void autoSizeColumns(Workbook workbook) {
          int numberOfSheets = workbook.getNumberOfSheets();
          for (int i = 0; i < numberOfSheets; i++) {
               Sheet sheet = workbook.getSheetAt(i);
               if (sheet.getPhysicalNumberOfRows() > 0) {
                    Row row = sheet.getRow(8);

                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                         Cell cell = cellIterator.next();
                         int columnIndex = cell.getColumnIndex();
                         sheet.autoSizeColumn(columnIndex);
                    }

               }
          }
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

          com.itextpdf.layout.element.Cell c;
          Table tc;
          Paragraph p = new Paragraph();
          Image img = new Image(ImageDataFactory.create(getClass().getResource("/images/pdf-logo.png")));
          img.setHorizontalAlignment(com.itextpdf.layout.property.HorizontalAlignment.CENTER);
          document.add(img);

          document.add(new Paragraph("\n"));

          generaTitulo(document, "Detalles de asistencias General");
          generaTablaAsistencia(document);
          pdf.addNewPage();
          document.add(new AreaBreak());

          document.close();
          System.out.println(f.getAbsolutePath());
     }

     private void generaTablaAsistencia(Document doc) throws IOException, RemoteException, NotBoundException {
          Registry reg = LocateRegistry.getRegistry(host, 27019);
          oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
          ArrayList<Asistencia> asist = new ArrayList<>(table.getItems());
          ArrayList<Invitado> invad = inter.getInvitados();
          List<String> ls = new ArrayList<>();
          for (Asistencia o : asist) {
               ls.add(o.getContrato());
          }
          invad.removeIf(r -> !(r.getFecha().equals(date.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE)) && ls.contains(r.getContrato())));
          int inv = 0, invadd = invad.size();
          for (Asistencia a : asist) {
               inv += Integer.parseInt(a.getInvitados());
          }

          Table tc = new Table(2);
          com.itextpdf.layout.element.Cell c = new com.itextpdf.layout.element.Cell();
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setBorder(Border.NO_BORDER);
          c.add("\n");
          c.add("Numero de titulares");
          c.add("Numero de invitados");
          c.add("Numero de invitados Adicionales");
          c.add("\n");
          tc.addCell(c);
          c = new com.itextpdf.layout.element.Cell();
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

     public com.itextpdf.layout.element.Cell generaCeldaInvitados(ArrayList<Invitado> l) throws IOException {
          com.itextpdf.layout.element.Cell c = new com.itextpdf.layout.element.Cell();
          c.setBorder(Border.NO_BORDER);
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setFontSize(8);
          c.setTextAlignment(TextAlignment.LEFT);
          for (Invitado in : l) {
               if (in.getFecha().equals(date.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE))) {
                    c.add(in.getNombre() + " " + in.getApellido());
               }
          }
          return c;

     }

     private com.itextpdf.layout.element.Cell generaCeldaNormal(String txt) throws IOException {
          com.itextpdf.layout.element.Cell c = new com.itextpdf.layout.element.Cell();
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

     private com.itextpdf.layout.element.Cell generaCabezera(String string) throws IOException {
          com.itextpdf.layout.element.Cell c = new com.itextpdf.layout.element.Cell();
          c.setHorizontalAlignment(com.itextpdf.layout.property.HorizontalAlignment.CENTER);
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
          com.itextpdf.layout.element.Cell c = new com.itextpdf.layout.element.Cell();
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

     public com.itextpdf.layout.element.Cell generaCeldaNegra(String txt) throws IOException {
          com.itextpdf.layout.element.Cell c = new com.itextpdf.layout.element.Cell();
          c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
          c.setFontSize(8);
          c.setTextAlignment(TextAlignment.CENTER);
          c.setBackgroundColor(Color.BLACK);
          c.setFontColor(Color.WHITE);
          c.add(txt);
          return c;

     }
}
