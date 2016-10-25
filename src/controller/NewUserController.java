/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Usuario;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import oasiscrud.oasisrimbd;

public class NewUserController implements Initializable {

    @FXML
    TextField nombre, apellido, usuario;
    @FXML
    PasswordField pass, cpass;
    @FXML
    Label topPane;
    @FXML
    ComboBox lvl;
    UserPanelController panel;

    Stage primStage;
    double xs, ys;
    String host;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        keyNombre();
        initCombo();
    }

    @FXML
    public void close(ActionEvent evt) {
        Stage stage;
        Button b = (Button) evt.getSource();
        stage = (Stage) b.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onKeyPressed(KeyEvent evt) {
        if (!cpass.getText().equals(pass.getText())) {
            cpass.setStyle("-fx-effect:innershadow( three-pass-box , red , 12, 0.2 , 0 , 0);");
            pass.setStyle("-fx-effect:innershadow( three-pass-box , red , 12, 0.2 , 0 , 0);");
        } else {
            cpass.setStyle("-fx-effect:innershadow( three-pass-box , green , 12, 0.2 , 0 , 0);");
            pass.setStyle("-fx-effect:innershadow( three-pass-box , green , 12, 0.2 , 0 , 0);");
        }
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

    public void keyNombre() {
        nombre.textProperty().addListener((ov, oldValue, newValue) -> {
            nombre.setText(newValue.toUpperCase());
        });
        apellido.textProperty().addListener((ov, oldValue, newValue) -> {
            apellido.setText(newValue.toUpperCase());
        });
        usuario.textProperty().addListener((ov, oldValue, newValue) -> {
            usuario.setText(newValue.toUpperCase());
        });
        pass.textProperty().addListener((ov, oldValue, newValue) -> {
            pass.setText(newValue.toUpperCase());
        });
        cpass.textProperty().addListener((ov, oldValue, newValue) -> {
            cpass.setText(newValue.toUpperCase());
        });
    }

    @FXML
    public void register(ActionEvent evt) throws SQLException, RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(host,27019);
        oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");
        ArrayList<Usuario> user = inter.searchUserbyUsername(usuario.getText());
        if (!user.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alerta");
            alert.setHeaderText(null);
            alert.setContentText("El usuario ya existe");
            alert.show();
        } else {
            if (!nombre.getText().isEmpty()) {
                if (!apellido.getText().isEmpty()) {
                    if (!usuario.getText().isEmpty()) {
                        if (pass.getText().equals(cpass.getText()) & !pass.getText().isEmpty()) {
                            inter.newUser(new Usuario(nombre.getText(), apellido.getText(), usuario.getText(), pass.getText(), lvl.getSelectionModel().getSelectedItem().equals("USUARIO") ? 1
                                    : lvl.getSelectionModel().getSelectedItem().equals("ADMINISTRADOR") ? 2 : 3));
                            panel.initTable();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Alerta");
                            alert.setHeaderText(null);
                            alert.setContentText("Usuario Registrado con Exito");
                            alert.show();
                            Stage stage;
                            Button b = (Button) evt.getSource();
                            stage = (Stage) b.getScene().getWindow();
                            stage.close();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Alerta");
                            alert.setHeaderText(null);
                            alert.setContentText("Las contraseñas no coinciden");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Alerta");
                        alert.setHeaderText(null);
                        alert.setContentText("El campo usuario no debe estar vacio");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Alerta");
                    alert.setHeaderText(null);
                    alert.setContentText("El campo apellido no debe estar vacio");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alerta");
                alert.setHeaderText(null);
                alert.setContentText("El campo nombre no debe estar vacio");
                alert.show();
            }
        }
    }

    public void initCombo() {
        lvl.getItems().addAll("USUARIO", "ADMINISTRADOR", "MASTER");
        lvl.getSelectionModel().selectFirst();

    }
}
