/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class SelectPrinterController implements Initializable {

      @FXML
      ComboBox combo;
      OpenTableController sup;
      byte[] a;
      String printer;

      @Override
      public void initialize(URL url, ResourceBundle rb) {
            // TODO
            initCombo();
      }

      public void initCombo() {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            System.out.println("Number of print services: " + printServices.length);

            for (PrintService printer : printServices) {
                  combo.getItems().add(printer.getName());
            }
            combo.getSelectionModel().selectFirst();
      }

      public void accept(ActionEvent evt) {
            printer = (String) combo.getSelectionModel().getSelectedItem();
            feedPrinter(a);
            Stage stage;
            Button b = (Button) evt.getSource();
            stage = (Stage) b.getScene().getWindow();
            stage.close();
      }
      
      public void cancel(ActionEvent evt){
            Stage stage;
            Button b = (Button) evt.getSource();
            stage = (Stage) b.getScene().getWindow();
            stage.close();
      }
      
      private boolean feedPrinter(byte[] b) {
        try {
              
            AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(printer, null)); //EPSON TM-U220 ReceiptE4

            DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();
            //PrintServiceLookup.lookupDefaultPrintService().createPrintJob();  
            
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(b, flavor, null);
            //PrintJobWatcher pjDone = new PrintJobWatcher(job);
            

            job.print(doc, null);
            

        } catch (javax.print.PrintException pex) {

            System.out.println("Printer Error " + pex.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


}
