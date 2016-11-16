/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Cliente;
import classes.Reserva;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import oasiscrud.oasisrimbd;

/**
 * FXML Controller class
 *
 * @author Ricardo Marcano
 */
public class ReservaController implements Initializable {

    @FXML
    TableColumn<Reserva, String> titular, cedula, telefono, plan, invitados, fecha;
    @FXML
    TableView<Reserva> table;
    @FXML
    HBox pfilter, pdate, rangeDate;
    @FXML
    ComboBox combo;
    @FXML
    TextField filter;
    @FXML
    DatePicker from, to, date;
    @FXML
    AnchorPane main, aux;
    ReservaController myController;
    MainMenuController menu;
    Usuario usuario;
    String host;
    int white = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            initTable();
            initCombo();
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        date.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    searchByDate();
                } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        to.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    searchByRangeDate();
                } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void initTable() throws RemoteException, NotBoundException {
        titular.setCellValueFactory(new PropertyValueFactory<>("titular"));
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
        invitados.setCellValueFactory(new PropertyValueFactory<>("invitados"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

    }

    public void initCombo() throws RemoteException, NotBoundException {

        combo.getItems().addAll("TODAS", "TITULAR", "CEDULA", "FECHA", "RANGO DE FECHA");
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

    public void reloadTable() throws RemoteException, NotBoundException {
        System.out.println("HOLA");
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        table.getItems().clear();
        ArrayList<Reserva> reservas = inter.getReservas();
        for (Reserva r : reservas) {
            table.getItems().add(r);
        }
    }

    public void changePane(String old, String neww) throws RemoteException, NotBoundException {
        switch (old) {
            case "TITULAR":
                if (!neww.equals("CONTRATO") || !neww.equals("CEDULA")) {
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
            case "TITULAR":
                if (!old.equals("CONTRATO") || !old.equals("CEDULA")) {
                    pfilter.setVisible(true);
                }
                filter.setOnKeyReleased((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        try {
                            searchByName();
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                break;
            case "CEDULA":
                if (!old.equals("CONTRATO") || !old.equals("NOMBRE")) {
                    pfilter.setVisible(true);
                }
                filter.setOnKeyReleased((KeyEvent event) -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        try {
                            searchByCI();
                        } catch (RemoteException | NotBoundException ex) {
                            Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                break;
            case "FECHA":
                pdate.setVisible(true);
                break;
            case "RANGO DE FECHA":
                rangeDate.setVisible(true);
                break;
            case "TODAS":
                reloadTable();
                break;

        }
    }

    public void filterSearch() throws RemoteException, NotBoundException {
        switch ((String) combo.getSelectionModel().getSelectedItem()) {
            case "TITULAR":
                searchByName();

                break;
            case "CEDULA":
                searchByCI();
                break;
        }
    }

    public void searchByName() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        table.getItems().clear();
        ArrayList<Reserva> reservas = inter.getReservasByName(filter.getText().toUpperCase());
        reservas.stream().forEach((r) -> {
            table.getItems().add(r);
        });
    }

    public void searchByCI() throws RemoteException, NotBoundException {
        table.getItems().clear();
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<Reserva> reservas = inter.getReservasByCI(filter.getText().toUpperCase());
        reservas.stream().forEach((r) -> {
            table.getItems().add(r);
        });

    }

    public void searchByDate() throws RemoteException, NotBoundException {
        table.getItems().clear();
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<Reserva> res = inter.getReservas();
        for (Reserva rsv : res) {
            if (date.getValue().isEqual(LocalDate.parse(rsv.getFecha()))) {
                table.getItems().add(rsv);
            }
        }
    }

    public void searchByRangeDate() throws RemoteException, NotBoundException {
        table.getItems().clear();
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<Reserva> res = inter.getReservas();
        for (Reserva rsv : res) {
            if ((from.getValue().isEqual(LocalDate.parse(rsv.getFecha())) || from.getValue().isBefore(LocalDate.parse(rsv.getFecha())))
                    && (to.getValue().isEqual(LocalDate.parse(rsv.getFecha())) || to.getValue().isAfter(LocalDate.parse(rsv.getFecha())))) {
                table.getItems().add(rsv);
            }
        }
    }

    public void back() {
        menu.aux.getChildren().clear();
        menu.main.setVisible(true);
        menu.main.toFront();
    }

    public void nuevaReserva() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaReserva.fxml"));
        Parent root = loader.load();
        NuevaReservaController controller = loader.getController();
        controller.primStage = stage;
        Scene scene = new Scene(root);
        controller.menu = myController;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/task.png")));
        stage.setTitle("Nueva reserva");
        stage.show();
    }

    public void editaReserva() throws IOException {
        if (!table.getSelectionModel().isEmpty()) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaReserva.fxml"));
            Parent root = loader.load();
            NuevaReservaController controller = loader.getController();
            controller.primStage = stage;
            controller.menu = myController;
            controller.save = false;
            controller.r = (Reserva) table.getSelectionModel().getSelectedItem();
            controller.initData();
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/task.png")));
            stage.setTitle("Editar Reserva");
            stage.show();
        }
    }

    public void eliminaReserva() throws RemoteException, NotBoundException {
        if (!table.getSelectionModel().isEmpty()) {
            Registry reg = LocateRegistry.getRegistry(host, 27019);
            oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
            inter.eliminaReserva((Reserva) table.getSelectionModel().getSelectedItem());
            reloadTable();
        }
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
        generaTitulo(document, "Lista de reservaciones");
        document.add(new Paragraph("\n"));
        Table t = generaCabezera(Arrays.asList("Titular", "Cedula", "Telefono", "Plan", "Invitados", "Fecha", "Observaciones"));
        document.add(t);
        for (int i = 0; i < table.getItems().size(); i++) {
            generaReservas(document, (Reserva) table.getItems().get(i));
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

    public void generaReservas(Document doc, Reserva res) throws IOException {
        Table t = new Table(7);
        if (white < 0) {
            t.setBackgroundColor(Color.LIGHT_GRAY);
        }
        t.addCell(generaCeldaNormal(res.getTitular()));
        t.addCell(generaCeldaNormal(res.getCedula()));
        t.addCell(generaCeldaNormal(res.getTelefono()));
        t.addCell(generaCeldaNormal(res.getPlan()));
        t.addCell(generaCeldaNormal(res.getInvitados()));
        t.addCell(generaCeldaNormal(res.getFecha()));
        t.addCell(generaCeldaNormal(res.getObservaciones()));
        doc.add(t);
    }

    public void eliminaResultados() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        for (Reserva r : table.getItems()) {
            inter.eliminaReserva(r);
        }
        reloadTable();

    }

    public void abrirMesa() throws IOException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        Reserva r = table.getSelectionModel().getSelectedItem();
        ArrayList<Cliente> c = inter.clientePorCedula(r.getCedula());

        if (c.size() == 1) {
            if (!inter.abrioReserva(r.getCedula())) {
                aux.setVisible(false);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/openTable.fxml"));
                OpenTableController controller;
                AnchorPane pan = loader.load();
                aux.getChildren().add(pan);
                controller = loader.getController();
                controller.reserva = myController;
                controller.client = c.get(0);
                controller.user = this.usuario;
                controller.host = this.host;
                controller.myController = controller;
                try {
                    controller.initData();
                } catch (NotBoundException | RemoteException | SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                aux.toFront();
                aux.setVisible(true);
                main.setVisible(false);
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setContentText("Ya el cliente ingreso al club hoy");
                a.show();
            }
        }
        if (c.size() > 1) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SeleccionaCliente.fxml"));
            Parent root = loader.load();
            SeleccionaClienteController con = loader.getController();
            con.list = c;
            con.controller = myController;
            con.opc="OPN";
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No se encontro el cliente");
            alert.setHeaderText("¿Desea buscar el cliente manualmente?");
            alert.setContentText("La informacion de la reserva no coincide con ningun cliente en la base de datos, desea buscar"
                    + " el cliente manual mente?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                menu.clientes(new ActionEvent());
            }

        }

    }

    public void generaPase() throws IOException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        Reserva r = table.getSelectionModel().getSelectedItem();
        ArrayList<Cliente> c = inter.clientePorCedula(r.getCedula());
        if (c.size() == 1) {
            if (!inter.abrioReserva(r.getCedula())) {
                aux.setVisible(false);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GenerarPase.fxml"));
                GenerarPaseController controller;
                AnchorPane pan = loader.load();
                aux.getChildren().add(pan);
                controller = loader.getController();
                controller.reserva = myController;
                controller.client = c.get(0);
                controller.user = this.usuario;
                controller.host = this.host;
                controller.myController = controller;
                try {
                    controller.initData();
                } catch (NotBoundException | RemoteException | SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                aux.toFront();
                aux.setVisible(true);
                main.setVisible(false);
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setContentText("Ya el cliente ingreso al club hoy");
                a.show();
            }
        }
        if (c.size() > 1) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SeleccionaCliente.fxml"));
            Parent root = loader.load();
            SeleccionaClienteController con = loader.getController();
            con.list = c;
            con.controller = myController;
            con.opc ="PAS";
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No se encontro el cliente");
            alert.setHeaderText("¿Desea buscar el cliente manualmente?");
            alert.setContentText("La informacion de la reserva no coincide con ningun cliente en la base de datos, desea buscar"
                    + " el cliente manual mente?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                menu.clientes(new ActionEvent());
            }
        }
    }

    public void generaAutorizado() throws RemoteException, NotBoundException, IOException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        Reserva r = table.getSelectionModel().getSelectedItem();
        ArrayList<Cliente> c = inter.clientePorCedula(r.getCedula());
        if (c.size()==1) {
            if (!inter.abrioReserva(r.getCedula())) {
                aux.setVisible(false);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/autorizar.fxml"));
                AutorizarController controller;
                AnchorPane pan = loader.load();
                aux.getChildren().add(pan);
                controller = loader.getController();
                controller.reserva = myController;
                controller.client = c.get(0);
                controller.user = this.usuario;
                controller.host = this.host;
                controller.myController = controller;
                try {
                    controller.initData();
                } catch (NotBoundException | RemoteException | SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                aux.toFront();
                aux.setVisible(true);
                main.setVisible(false);
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setContentText("Ya el cliente ingreso al club hoy");
                a.show();
            }
        } if(c.size()>1) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SeleccionaCliente.fxml"));
            Parent root = loader.load();
            SeleccionaClienteController con = loader.getController();
            con.list = c;
            con.controller = myController;
            con.opc="AUT";
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No se encontro el cliente");
            alert.setHeaderText("¿Desea buscar el cliente manualmente?");
            alert.setContentText("La informacion de la reserva no coincide con ningun cliente en la base de datos, desea buscar"
                    + " el cliente manual mente?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                menu.clientes(new ActionEvent());
            }
        }
    }
}
