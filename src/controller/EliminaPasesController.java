/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class EliminaPasesController implements Initializable {

     @FXML
     DatePicker from, to;

     @Override
     public void initialize(URL url, ResourceBundle rb) {
          initDate();
     }

     public void initDate() {
          from.setValue(LocalDate.now());
          fromDate();
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
     
     public void Delete(){
          
     }
     
     

}
