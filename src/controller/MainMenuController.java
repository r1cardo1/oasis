/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.DigitalClock;
import classes.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class MainMenuController implements Initializable {

    @FXML
    Label date;
    @FXML
    Label hour;
    @FXML
    AnchorPane topPane;
    public Stage primStage;
    Double xs, ys;
    boolean maximized = false;
    boolean minimized = false;
    MainMenuController myController;
    Usuario user;
    @FXML
    AnchorPane main;
    @FXML
    AnchorPane aux;
    @FXML
    Label luser;
    @FXML
    Label lname;
    @FXML
    GridPane buttons;
    String host;
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        new DigitalClock(date, hour);
        drag();
        translatey(buttons, 0.0);
    }

    @FXML
    public void close(ActionEvent evt) {
        Stage stage;
        Button b = (Button) evt.getSource();
        stage = (Stage) b.getScene().getWindow();
        stage.close();
    }

    public void setStage(Stage s) {
        primStage = s;
    }

    @FXML
    public void maximize(ActionEvent evt) {

        Stage stage;
        Button b = (Button) evt.getSource();
        stage = (Stage) b.getScene().getWindow();
        if (maximized) {
            stage.setMaximized(false);
            maximized = false;
        } else {
            stage.setMaximized(true);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight());
            maximized = true;
        }

    }

    @FXML
    public void minimize(ActionEvent evt) {
        Stage stage;
        Button b = (Button) evt.getSource();
        stage = (Stage) b.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void clientes(ActionEvent evt) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/search.fxml"));
        SearchController searchController;
        Parent root = loader.load();
        searchController = loader.getController();
        searchController.setMenuController(myController);
        searchController.user = this.user;
        searchController.myController = searchController;
        searchController.host = this.host;
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        searchController.primStage = stage;
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/task.png")));
        stage.setTitle("Busqueda de clientes Oasis");
        stage.show();
        fadein(root);

    }

    @FXML
    public void logout(ActionEvent evt) throws IOException {
        Stage stage = new Stage();
        FXMLLoader load = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        LoginController controller;
        Parent root = load.load();
        controller = load.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        controller.setStage(stage);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        primStage.close();
    }

    @FXML
    public void visitas(ActionEvent evt) throws IOException {
        if (user.getNivel() >= 2) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/visitas.fxml"));
            AnchorPane pan = loader.load();
            aux.getChildren().add(pan);
            VisitasController controller = loader.getController();
            controller.menu = myController;
            controller.host=this.host;
            aux.toFront();
            main.setVisible(false);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alerta");
            alert.setHeaderText(null);
            alert.setContentText("No tiene los permisos suficientes para acceder a esta seccion");
            alert.show();
        }
    }

    @FXML
    public void restringidos(ActionEvent evt) throws IOException {
        if (user.getNivel() >= 2) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blackList.fxml"));
            Pane pan = loader.load();
            aux.getChildren().add(pan);
            BlackListController controller = loader.getController();
            controller.menu = myController;
            controller.host=this.host;
            aux.toFront();

            main.setVisible(false);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alerta");
            alert.setHeaderText(null);
            alert.setContentText("No tiene los permisos suficientes para acceder a esta seccion");
            alert.show();
        }
    }

    @FXML
    public void adminMenu(ActionEvent evt) throws IOException {
        if (user.getNivel() == 3) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminMenu.fxml"));
            Pane pan = loader.load();
            aux.getChildren().add(pan);
            AdminMenuController controller = loader.getController();
            controller.myController = controller;
            controller.menu = myController;
            controller.usuario = user;
            controller.host=this.host;
            aux.toFront();
            main.setVisible(false);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alerta");
            alert.setHeaderText(null);
            alert.setContentText("No tiene los permisos suficientes para acceder a esta seccion");
            alert.show();
        }
    }

    @FXML
    public void reportMenu() throws IOException {
        if (user.getNivel() >= 2) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reportMenu.fxml"));
            Pane pan = loader.load();
            aux.getChildren().add(pan);
            ReportMenuController controller = loader.getController();
            controller.myController = controller;
            controller.menu = myController;
            controller.usuario = user;
            controller.host=this.host;
            aux.toFront();
            main.setVisible(false);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alerta");
            alert.setHeaderText(null);
            alert.setContentText("No tiene los permisos suficientes para acceder a esta seccion");
            alert.show();
        }
    }

    public void viewReservas() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reserva.fxml"));
        AnchorPane pan = loader.load();
        aux.getChildren().add(pan);
        ReservaController controller = loader.getController();
        controller.myController = controller;
        controller.menu = myController;
        controller.host=this.host;
        controller.usuario = user;
        aux.toFront();
        main.setVisible(false);
    }

    public void setController(MainMenuController c) {
        this.myController = c;
    }

    public void setUser(Usuario u) {
        this.user = u;
        lname.setText(user.getNombre() + " " + user.getApellido());
        luser.setText(user.getUsuario());
    }

    public void drag() {

        topPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                xs = evt.getSceneX();
                ys = evt.getSceneY();
            }
        });

        topPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primStage.setX(event.getScreenX() - xs);
                primStage.setY(event.getScreenY() - ys);
            }
        });

    }

    public void translatey(Node n, Double delay) {
        TranslateTransition tt = new TranslateTransition();
        tt.setNode(n);
        tt.setDelay(Duration.millis(delay));
        tt.setDuration(Duration.millis(600));
        tt.setFromY(main.getPrefHeight());
        tt.setToY(n.getTranslateY());
        tt.play();
    }

    public void translatex(Node n, Double delay) {
        TranslateTransition tt = new TranslateTransition();
        tt.setNode(n);
        tt.setDelay(Duration.millis(delay));
        tt.setDuration(Duration.millis(600));
        tt.setFromX(main.getPrefWidth());
        tt.setToX(n.getTranslateX());
        tt.play();
    }

    public void scaleout(Node n) {
        ScaleTransition st = new ScaleTransition();
        st.setNode(n);
        st.setDuration(Duration.millis(500));
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(0);
        st.setToY(0);
        st.play();
    }

    public void scalein(Node n) {
        ScaleTransition st = new ScaleTransition();
        st.setNode(n);
        st.setDuration(Duration.millis(500));
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    public void fadein(Node n) {
        FadeTransition ft = new FadeTransition();
        ft.setNode(n);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDuration(Duration.millis(250));
        ft.play();
    }
}
