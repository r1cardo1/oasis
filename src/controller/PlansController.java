/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.DataManager;
import classes.Plan;
import classes.Usuario;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class PlansController implements Initializable {

      AdminMenuController menu;
      PlansController myController;
      Usuario usuario;
      DataManager dm = new DataManager();
      @FXML
      TableView table;
      @FXML
      TableColumn<Plan, String> plan;
      @FXML
      TableColumn<Plan, Integer> cant;
      @FXML
      TextField txtplan, txtcant;
      Boolean editMode = false;

      @Override
      public void initialize(URL url, ResourceBundle rb) {
            try {
                  initTable();
            } catch (SQLException ex) {
                  System.out.println(ex.getMessage());
            }
      }

      public void initTable() throws SQLException {
            plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
            cant.setCellValueFactory(new PropertyValueFactory<>("cant"));
            ArrayList<Plan> plans = dm.getPlans();
           for(Plan p:plans) {
                  table.getItems().add(p);
            }
      }

      public void updateTable() throws SQLException {
            table.getItems().clear();
             ArrayList<Plan> plans = dm.getPlans();
           for(Plan p:plans) {
                  table.getItems().add(p);
            }
      }

      @FXML
      public void addAction() throws SQLException {
            if (!editMode) {
                  if (!txtplan.getText().isEmpty()) {
                        if (!txtcant.getText().isEmpty()) {
                              dm.addPlan(new Plan(txtplan.getText(), Integer.parseInt(txtcant.getText())));
                              updateTable();
                              table.getSelectionModel().clearSelection();
                              txtcant.clear();
                              txtplan.clear();
                        }
                  }
            }
            if (editMode) {
                  if (!txtplan.getText().isEmpty()) {
                        if (!txtcant.getText().isEmpty()) {                              
                              dm.updatePlan(new Plan(txtplan.getText(), Integer.parseInt(txtcant.getText())), (Plan) table.getSelectionModel().getSelectedItem());
                              updateTable();
                              table.getSelectionModel().clearSelection();
                              txtcant.clear();
                              txtplan.clear();
                              editMode = false;
                        }
                  }
            }
      }

      @FXML
      public void deleteAction() throws SQLException {
            if (!table.getSelectionModel().isEmpty()) {
                  dm.deletePlan((Plan) table.getSelectionModel().getSelectedItem());
                  updateTable();
                  table.getSelectionModel().clearSelection();
            }
      }

      @FXML
      public void editAction() {
            if (!table.getSelectionModel().isEmpty()) {
                  Plan p = (Plan) table.getSelectionModel().getSelectedItem();
                  txtplan.setText(p.getPlan());
                  txtcant.setText(Integer.toString(p.getCant()));
                  editMode = true;
            }
      }

      @FXML
      public void back(ActionEvent evt) {
            menu.aux.getChildren().clear();
            menu.main.setVisible(true);
            menu.main.toFront();
      }
}
