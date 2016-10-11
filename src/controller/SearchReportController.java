/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.DataManager;
import classes.Usuario;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Ricardo Marcano
 */
public class SearchReportController implements Initializable {

      @FXML
      AnchorPane main, aux, tUserView, tDateView, tRangeDateView;
      @FXML
      HBox hgDate, hgRangeDate, hgUser;
      @FXML
      ComboBox tcUser, gcMode, mgUser;
      @FXML
      BarChart<String, Integer> barChart;
      @FXML
      CategoryAxis xAxis;
      DataManager dm = new DataManager();
      Usuario usuario;
      ReportMenuController menu;

      @Override
      public void initialize(URL url, ResourceBundle rb) {
            try {
                  initCombo();
            } catch (SQLException ex) {
                  System.out.println(ex.getMessage());
            }
      }

      public void initCombo() throws SQLException {
            tcUser.getItems().addAll("TODOS");
            mgUser.getItems().add("TODOS");
            ResultSet rs;
            rs = dm.getUsuarios();
            while (rs.next()) {
                  String str = rs.getString("usuario");
                  tcUser.getItems().add(str);
                  mgUser.getItems().add(str);
            }
            tcUser.getSelectionModel().selectFirst();
            gcMode.getItems().addAll("Por Usuario", "Por Fecha", "Por rango de fecha");
            gcMode.getSelectionModel().selectFirst();
            gcMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                  @Override
                  public void changed(ObservableValue observableValue, String old, String neww) {
                        changePane(old, neww);
                  }
            });
            gcMode.getSelectionModel().selectFirst();
            hgUser.setVisible(true);
      }

      public void changePane(String old, String neww) {
            switch (old) {
                  case "Por Usuario":
                        hgUser.setVisible(false);
                        break;
                  case "Por Fecha":
                        hgDate.setVisible(false);
                        break;
                  case "Por rango de fecha":
                        hgRangeDate.setVisible(false);
                        break;
            }
            switch (neww) {
                  case "Por Usuario":
                        hgUser.setVisible(true);
                        break;
                  case "Por Fecha":
                        hgDate.setVisible(true);
                        break;
                  case "Por rango de fecha":
                        hgRangeDate.setVisible(true);
                        break;
            }
      }

      @FXML
      public void action() throws SQLException {

            barChart.getData().clear();
            if (!mgUser.getSelectionModel().getSelectedItem().equals("TODOS")) {
                  ResultSet rs = dm.getSearchByUser((String) mgUser.getSelectionModel().getSelectedItem());
                  XYChart.Series<String, Integer> series = new XYChart.Series<>();
                  if (rs.next()) {
                        series.getData().add(new XYChart.Data<>((String) mgUser.getSelectionModel().getSelectedItem(), rs.getInt("COUNT(usuario)")));
                  }
                  barChart.getData().add(series);
            }else{
                  ArrayList<String> list = new ArrayList<>();
                  
                  ResultSet rs = dm.getUsuarios();
                  while(rs.next())
                        list.add(rs.getString("usuario"));
                  rs.close();
                  int[] searchs = new int[list.size()];
                  
                  for(int i = 0;i<list.size();i++){
                        rs=dm.getSearchByUser(list.get(i));
                        if(rs.next())
                              searchs[i] = rs.getInt("COUNT(usuario)");
                  }
                  XYChart.Series<String, Integer> series = new XYChart.Series<>();
                  for(int i=0;i<list.size();i++){
                        series.getData().add(new XYChart.Data<>(list.get(i),searchs[i]));
                  }
                  barChart.getData().add(series);                  
            }
      }
}
