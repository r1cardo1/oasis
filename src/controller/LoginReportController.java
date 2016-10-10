/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.DataManager;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Ricardo Marcano
 */
public class LoginReportController implements Initializable {

    @FXML
    BarChart<String, Integer> bar;
    @FXML
    CategoryAxis xAxis;
    ArrayList<String> users = new ArrayList<>();
    DataManager dm = new DataManager();
    int[] logins;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initUsers();
        } catch (SQLException ex) {
            Logger.getLogger(LoginReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initUsers() throws SQLException {
        ResultSet rs = dm.getUsuarios();
        while (rs.next()) {
            users.add(rs.getString("usuario"));
        }
        rs.close();
        logins = new int[users.size()];
        for (int i = 0; i < users.size(); i++) {
            rs = dm.getLogins(users.get(i));
            if (rs.next()) {
                logins[i] = rs.getInt("COUNT(usuario)");
            }
        }
        XYChart.Series<String, Integer> series;
        series = new XYChart.Series<>();
        for (int i = 0; i < users.size(); i++) {
            XYChart.Data data = new XYChart.Data<>(users.get(i), logins[i]);
            series.getData().add(data);
            
        }
        bar.getData().add(series);
        for(int i=0;i<bar.getData().size();i++)
            displayLabelForData(bar.getData().get(i).getData().get(i));
    }

    private void displayLabelForData(XYChart.Data<String, Integer> data) {
        final Node node = data.getNode();
        final Text dataText = new Text(data.getYValue() + "");
        node.parentProperty().addListener(new ChangeListener<Parent>() {
            @Override
            public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().add(dataText);
            }
        });

        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                dataText.setLayoutX(
                        Math.round(
                                bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
                        )
                );
                dataText.setLayoutY(
                        Math.round(
                                bounds.getMinY() - dataText.prefHeight(-1) * 0.5
                        )
                );
            }
        });
    }

}
