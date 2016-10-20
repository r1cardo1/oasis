/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Asistencia;
import classes.Cliente;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

        initCombo();
    }

    public void initTable() throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
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
            ArrayList<Cliente> client = inter.searchClientbyContract(as.getContrato());
            if (!client.isEmpty()) {
                asist = new Asistencia(as.getInvitados(), as.getFecha(), as.getHora(),
                        client.get(0).getCedula(), client.get(0).getNombre(), client.get(0).getContrato(), client.get(0).getPlan(), as.getInvad());
            }
            table.getItems().add(asist);
        }
    }

    public void reloadTable() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        table.getItems().clear();
        ArrayList<Asistencia> asists = inter.visits();
        ResultSet aux;
        Asistencia asist = null;
        for (Asistencia as : asists) {
            ArrayList<Cliente> client = inter.searchClientbyContract(as.getContrato());
            if (!client.isEmpty()) {
                asist = new Asistencia(as.getInvitados(), as.getFecha(), as.getHora(),
                        client.get(0).getCedula(), client.get(0).getNombre(), client.get(0).getContrato(), client.get(0).getPlan(), as.getInvad());
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
        to.setValue(from.getValue().plusDays(1));
    }

    public void date() throws RemoteException, NotBoundException {
Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        table.getItems().clear();
        ArrayList<Asistencia> asists = inter.visits();
        ResultSet aux;
        Asistencia asist = null;
        for (Asistencia as : asists) {
            if (date.getValue().isEqual(LocalDate.parse(as.getFecha()))) {
                ArrayList<Cliente> client = inter.searchClientbyContract(as.getContrato());
                if (!client.isEmpty()) {
                    asist = new Asistencia(as.getInvitados(), as.getFecha(), as.getHora(),
                            client.get(0).getCedula(), client.get(0).getNombre(), client.get(0).getContrato(), client.get(0).getPlan(), as.getInvad());
                }
                table.getItems().add(asist);
            }
        }

    }

    public void toDate() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");
        table.getItems().clear();
        ArrayList<Asistencia> asists =inter.visits();
        ResultSet aux;
        Asistencia asist = null;
        for (Asistencia as : asists) {
            if ((from.getValue().isEqual(LocalDate.parse(as.getFecha()))) || from.getValue().isBefore(LocalDate.parse(as.getFecha()))
                        && ((to.getValue().isEqual(LocalDate.parse(as.getFecha())) || to.getValue().isAfter(LocalDate.parse(as.getFecha()))))){
            ArrayList<Cliente> client = inter.searchClientbyContract(as.getContrato());
            if (!client.isEmpty()) {
                asist = new Asistencia(as.getInvitados(), as.getFecha(), as.getHora(),
                        client.get(0).getCedula(), client.get(0).getNombre(), client.get(0).getContrato(), client.get(0).getPlan(), as.getInvad());
            }
            table.getItems().add(asist);
            }
        }
    }

    public void search() {

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

            InputStream inputStream = new FileInputStream("src/images/excel-logo.jpg");

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

}
