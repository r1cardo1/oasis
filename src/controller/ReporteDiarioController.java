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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RicardoMarcano
 */
public class ReporteDiarioController implements Initializable {

    ReporteDiarioController mycontroller;
    ArrayList<Asistencia> list;
    ArrayList<Invitado> invad;
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
        list = inter.asistenciaPorFecha(date.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
        invad = inter.getInvitados();

        FileChooser file = new FileChooser();
        file.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documento PDF (.PDF)", " *.PDF"));
        File f = file.showSaveDialog(null);
        PdfWriter writer = new PdfWriter(f.getAbsolutePath());

        PdfDocument pdf = new PdfDocument(writer);
        pdf.setDefaultPageSize(PageSize.LETTER);

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

        document.add(new Paragraph(""));

        tc = new Table(2);
        c = new Cell();
        p.add("Numero de invitados\n");
        p.add("Numero de titulares\n");
        p.add("Numero de invitados adicionales");
        p.setFontSize(10);
        p.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
        p.setFontColor(Color.BLACK);
        c.add(p);
        c.setBorder(Border.NO_BORDER);
        tc.addCell(c);

        c = new Cell();
        p = new Paragraph();
        p.add(cantInvitados() + "\n");
        p.add(list.size() + "\n");
        p.add(cantInvitadosAd() + "");
        p.setFontSize(10);
        p.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
        p.setFontColor(Color.BLACK);
        c.add(p);
        c.setBorder(Border.NO_BORDER);
        tc.addCell(c);

        document.add(tc);
        document.add(new Paragraph("\n"));
        tc = new Table(1);
        tc.addCell(generaTitulo("Detalles de Clientes"));
        document.add(tc);
        tc = new Table(6);
        tc.addCell(generaCelda("Cedula"));
        tc.addCell(generaCelda("Cliente"));
        tc.addCell(generaCelda("Contrato"));
        tc.addCell(generaCelda("Plan"));
        tc.addCell(generaCelda("Invitados"));
        tc.addCell(generaCelda("Inv. Adicionales"));
        document.add(tc);
        for (Asistencia a : list) {
            document.add(generaTabla(a));
            white*=-1;
        }

        document.close();
        System.out.println(f.getAbsolutePath());
        close(evt);
    }

    public Cell generaCelda(String text) throws IOException {
        Cell c = new Cell();
        c.setBackgroundColor(Color.BLACK);
        c.setFontColor(Color.WHITE);
        c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
        c.setFontSize(10);
        c.setTextAlignment(TextAlignment.CENTER);
        c.setBorder(Border.NO_BORDER);
        c.add(text);
        c.setPadding(0);
        return c;
    }

    public Cell generaCabezera(String text) throws IOException {
        Cell c = new Cell();
        c.setFontColor(Color.GRAY);
        c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
        c.setFontSize(20);
        c.setTextAlignment(TextAlignment.CENTER);
        c.setBorder(Border.NO_BORDER);
        c.setBorderBottom(new SolidBorder(Color.BLUE, 1));
        c.add(text);
        c.setPadding(0);
        return c;
    }

    public Table generaTabla(Asistencia a) throws IOException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host, 27019);
        oasiscrud.oasisrimbd inter = (oasiscrud.oasisrimbd) reg.lookup("OasisSev");

        Cliente c = inter.clientePorContrato(a.getContrato());
        Table tc = new Table(6);
        if (white > 0) {
            tc.setBackgroundColor(Color.LIGHT_GRAY);
        }
        if(c!=null){
        tc.addCell(generaCeldaCliente(c.getCedula()));
        tc.addCell(generaCeldaCliente(c.getNombre()));
        tc.addCell(generaCeldaCliente(a.getContrato()));
        tc.addCell(generaCeldaCliente(c.getPlan()));
        tc.addCell(generaCeldaCliente(a.getInvitados()));
        tc.addCell(generaInvitadosAdicionales(a.getContrato()));
        }

        return tc;
    }

    public Cell generaCeldaCliente(String text) throws IOException {
        Cell c = new Cell();        
        c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
        c.setFontSize(8);
        c.setFontColor(Color.BLACK);
        c.setTextAlignment(TextAlignment.CENTER);
        c.setBorder(Border.NO_BORDER);
        c.setVerticalAlignment(VerticalAlignment.MIDDLE);
        if (text != null) {
            c.add(text);
        }
        c.setPadding(0);
        return c;
    }

    public Cell generaTitulo(String text) throws IOException {
        Cell c = new Cell();
        c.setFontColor(Color.GRAY);
        c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
        c.setFontSize(15);
        c.setTextAlignment(TextAlignment.CENTER);
        c.setBorder(Border.NO_BORDER);
        c.setBorderBottom(new SolidBorder(Color.LIGHT_GRAY, 1));
        c.add(text);
        c.setPadding(0);
        return c;
    }

    public int cantInvitados() {
        int acum = 0;
        for (Asistencia a : list) {
            acum += Integer.parseInt(a.getInvitados());
        }
        return acum;
    }

    public int cantInvitadosAd() {
        int acum = 0;
        for (Asistencia a : list) {
            acum += Integer.parseInt(a.getInvad());
        }
        return acum;
    }

    public Cell generaInvitadosAdicionales(String con) throws IOException {
        int ci=1;
        Cell c = new Cell();
        c.setFont(PdfFontFactory.createRegisteredFont("times-italic"));
        c.setFontSize(8);
        c.setTextAlignment(TextAlignment.LEFT);
        c.setBorder(Border.NO_BORDER);
        c.setPadding(0);
        for (Invitado i : invad) {
            if (i.getContrato().equals(con)) {
                if (i.getFecha().equals(date.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE))) {
                    c.add(ci +" - "+i.getNombre() + " " + i.getApellido());
                    ci++;
                }
            }
        }
        return c;
    }
}
