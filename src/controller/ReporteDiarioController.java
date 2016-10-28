/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;


import static com.itextpdf.kernel.pdf.PdfName.Image;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
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

    @FXML
    DatePicker date;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDate();
    }
    
    public void initDate(){
        date.setValue(LocalDate.now());
    }

    public void close(ActionEvent evt) {
        Stage stage;
        Button b = (Button) evt.getSource();
        stage = (Stage) b.getScene().getWindow();
        stage.close();
    }
    
    public void generaPDF(ActionEvent evt) throws FileNotFoundException, MalformedURLException{
        FileChooser file = new FileChooser();
        file.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documento PDF (.PDF)"," *.PDF"));
        File f = file.showSaveDialog(null);
        PdfWriter writer = new PdfWriter(f.getAbsolutePath());
        PdfDocument pdf = new PdfDocument(writer);
        pdf.setDefaultPageSize(PageSize.LETTER);
        
        Document document = new Document(pdf);
        Image img = new Image(ImageDataFactory.create("src/images/pdf-logo.png"));
        img.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(img);
        document.add(new Paragraph("\n\n\n"));
         img = new Image(ImageDataFactory.create("src/images/banner.png"));
        img.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(img);
        
        
        document.close();
        System.out.println(f.getAbsolutePath());
        
        close(evt);
    }
    
public Image getWatermarkedImage(PdfDocument pdfDoc, Image img, String watermark) {
        float width = img.getImageScaledWidth();
        float height = img.getImageScaledHeight();
        PdfFormXObject template = new PdfFormXObject(new Rectangle(width, height));
        new Canvas(template, pdfDoc).
                add(img).
                setFontColor(DeviceGray.BLACK).
                setFontSize(20).
                showTextAligned(watermark, width/2 , height/2 , TextAlignment.CENTER);
        Image im = new Image(template);
        im.setHorizontalAlignment(HorizontalAlignment.CENTER);
        return im;
    }

}
