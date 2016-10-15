/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Asistencia;
import classes.DataManager;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;
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
import javafx.util.Callback;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

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
      DataManager dm = new DataManager();
      DataManager dmaux = new DataManager();
      MainMenuController menu;

      @Override
      public void initialize(URL url, ResourceBundle rb) {
            try {
                  initTable();
            } catch (SQLException ex) {
                  Logger.getLogger(VisitasController.class.getName()).log(Level.SEVERE, null, ex);
            }

            initCombo();
      }

      public void initTable() throws SQLException {
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

            ResultSet rs = dm.visits();
            ResultSet aux;
            Asistencia asist = null;
            while (rs.next()) {
                  aux = dmaux.searchClientbyContract(rs.getString("contrato"));
                  if (aux.next()) {
                        asist = new Asistencia(rs.getString("mesa"), rs.getString("num_inv"), rs.getString("fecha"), rs.getString("hora"),
                                aux.getString("cedula"), aux.getString("nombre"), aux.getString("contrato"), aux.getString("plan"));
                  }
                  table.getItems().add(asist);

            }
      }

      public void reloadTable() {
            table.getItems().clear();
            ResultSet rs = dm.visits();
            ResultSet aux;
            Asistencia asist = null;
            try {
                  while (rs.next()) {
                        aux = dmaux.searchClientbyContract(rs.getString("contrato"));
                        if (aux.next()) {
                              asist = new Asistencia(rs.getString("mesa"), rs.getString("num_inv"), rs.getString("fecha"), rs.getString("hora"),
                                      aux.getString("cedula"), aux.getString("nombre"), aux.getString("contrato"), aux.getString("plan"));
                        }
                        table.getItems().add(asist);

                  }
            } catch (Exception ex) {
                  System.out.println(ex.getMessage());
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
            to.setValue(from.getValue().plusDays(1));
      }

      public void date() {

            table.getItems().clear();
            ResultSet rs = dm.visits();
            ResultSet aux;
            Asistencia asist = null;
            try {
                  while (rs.next()) {
                        if (date.getValue().isEqual(LocalDate.parse(rs.getString("fecha")))) {
                              aux = dmaux.searchClientbyContract(rs.getString("contrato"));
                              if (aux.next()) {
                                    asist = new Asistencia(rs.getString("mesa"), rs.getString("num_inv"), rs.getString("fecha"), rs.getString("hora"),
                                            aux.getString("cedula"), aux.getString("nombre"), aux.getString("contrato"), aux.getString("plan"));
                              }
                              table.getItems().add(asist);
                        }
                  }
            } catch (Exception ex) {
                  System.out.println(ex.getMessage());
            }
      }

      public void toDate() {
            table.getItems().clear();
            ResultSet rs = dm.visits();
            ResultSet aux;
            Asistencia asist = null;
            try {
                  while (rs.next()) {
                        if ((from.getValue().isEqual(LocalDate.parse(rs.getString("fecha"))) || from.getValue().isBefore(LocalDate.parse(rs.getString("fecha"))))
                                && (to.getValue().isEqual(LocalDate.parse(rs.getString("fecha"))) || to.getValue().isAfter(LocalDate.parse(rs.getString("fecha"))))) {
                              aux = dmaux.searchClientbyContract(rs.getString("contrato"));
                              if (aux.next()) {
                                    asist = new Asistencia(rs.getString("mesa"), rs.getString("num_inv"), rs.getString("fecha"), rs.getString("hora"),
                                            aux.getString("cedula"), aux.getString("nombre"), aux.getString("contrato"), aux.getString("plan"));
                              }
                              table.getItems().add(asist);
                        }
                  }
            } catch (Exception e) {
                  System.out.println(e.getMessage());
            }
      }

      public void search() {

      }

      public void exportExcel() {
            try {
                  String filename = "NewExcelFile.xls";
                  HSSFWorkbook workbook = new HSSFWorkbook();
                  HSSFSheet sheet = workbook.createSheet("FirstSheet");
                  int bool = 1;

                  HSSFRow rowhead = sheet.createRow(2);

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
                        HSSFRow row = sheet.createRow(i + 3);
                        Asistencia a = (Asistencia) table.getItems().get(i);
                        row.createCell(1).setCellValue(a.getCedula());
                        row.createCell(2).setCellValue(a.getNombre());
                        row.createCell(3).setCellValue(a.getContrato());
                        row.createCell(4).setCellValue(a.getPlan());
                        row.createCell(5).setCellValue(a.getFecha());
                        row.createCell(6).setCellValue(a.getHora());
                        row.createCell(7).setCellValue(a.getInvitados());
                        centerRow(workbook,row);
                  }
                  
                  autoSizeColumns(workbook);
                  FileOutputStream fileOut = new FileOutputStream(filename);
                  workbook.write(fileOut);
                  fileOut.close();
                  System.out.println("Your excel file has been generated!");

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
      
      public void centerRow(Workbook wb, Row row){
            CellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            for (int i = 0; i < row.getLastCellNum(); i++) { 
                  if(row.getCell(i)!=null)
                               row.getCell(i).setCellStyle(style);//Set the sty;e
            }
      }

      public void autoSizeColumns(Workbook workbook) {
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                  Sheet sheet = workbook.getSheetAt(i);
                  if (sheet.getPhysicalNumberOfRows() > 0) {
                        Row row = sheet.getRow(2);
                        
                        Iterator<Cell> cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {
                              Cell cell = cellIterator.next();
                              int columnIndex = cell.getColumnIndex();
                              sheet.autoSizeColumn(columnIndex);
                        }
                        
                  }
            }
      }

}
