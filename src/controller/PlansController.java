/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import classes.Plan;
import classes.Usuario;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import oasiscrud.oasisrimbd;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class PlansController implements Initializable {

      AdminMenuController menu;
      PlansController myController;
      Usuario usuario;
      @FXML
      TableView table;
      @FXML
      TableColumn<Plan, String> plan;
      @FXML
      TableColumn<Plan, Integer> cant;
      @FXML
      TextField txtplan, txtcant;
      Boolean editMode = false;
    String host;

      @Override
      public void initialize(URL url, ResourceBundle rb) {

      }

      public void initTable() throws SQLException, RemoteException, NotBoundException {
            plan.setCellValueFactory(new PropertyValueFactory<>("plan"));
            cant.setCellValueFactory(new PropertyValueFactory<>("cant"));
            Registry reg = LocateRegistry.getRegistry(host,27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
            ArrayList<Plan> plans = inter.getPlans();
           for(Plan p:plans) {
                  table.getItems().add(p);
            }
      }

      public void updateTable() throws SQLException, RemoteException, NotBoundException {
            table.getItems().clear();
            Registry reg = LocateRegistry.getRegistry(host,27019);
            oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
            ArrayList<Plan> plans = inter.getPlans();
           for(Plan p:plans) {
                  table.getItems().add(p);
            }
      }

      @FXML
      public void addAction() throws SQLException, RemoteException, NotBoundException {
            if (!editMode) {
                  if (!txtplan.getText().isEmpty()) {
                        if (!txtcant.getText().isEmpty()) {
                            Registry reg = LocateRegistry.getRegistry(host,27019);
                            oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
                              inter.addPlan(new Plan(txtplan.getText(), Integer.parseInt(txtcant.getText())));
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
                            Registry reg = LocateRegistry.getRegistry(host,27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
                              inter.updatePlan(new Plan(txtplan.getText(), Integer.parseInt(txtcant.getText())), (Plan) table.getSelectionModel().getSelectedItem());
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
      public void deleteAction() throws SQLException, RemoteException, NotBoundException {
            if (!table.getSelectionModel().isEmpty()) {
                Registry reg = LocateRegistry.getRegistry(host,27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
                  inter.deletePlan((Plan) table.getSelectionModel().getSelectedItem());
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
