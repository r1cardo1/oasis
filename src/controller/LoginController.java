/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import classes.Usuario;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import oasiscrud.oasisrimbd;

/**
 * FXML Controller class
 *
 * @author Ricardo
 */
public class LoginController implements Initializable {

    @FXML
    Pane topPane;
    private double xs, ys = 0;
    public Stage primStage;
    @FXML
    Button login;
    @FXML
    TextField user;
    @FXML
    PasswordField pass;
    String host;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        drag();
        translate(user, 0.0);
        translate(pass, 0.0);
        translatey(login, 0.0);

    }

    @FXML
    public void focusin(MouseEvent evt) {
        ScaleTransition st = new ScaleTransition();
        st.setNode((Node) evt.getSource());
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setDuration(Duration.millis(60));
        st.play();
    }

    @FXML
    public void focusout(MouseEvent evt) {
        ScaleTransition st = new ScaleTransition();
        st.setNode((Node) evt.getSource());
        st.setFromX(1.2);
        st.setFromY(1.2);
        st.setToX(1);
        st.setToY(1);
        st.setDuration(Duration.millis(60));
        st.play();
    }

    @FXML
    public void close(ActionEvent evt) {
        Stage stage;
        Button b = (Button) evt.getSource();
        stage = (Stage) b.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void login() throws SQLException, IOException {
        try {
            Registry reg = LocateRegistry.getRegistry(host, 27019);
            oasisrimbd inter = (oasisrimbd) reg.lookup("OasisSev");

            Usuario u = null;
            u = inter.login(new Usuario(user.getText(),pass.getText()));
            Usuario userLogin = null;
            if (u != null) {
                userLogin = u;
                Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
                String ampm = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
                inter.logLogin(userLogin.getNombre(), userLogin.getApellido(), userLogin.getUsuario(), LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                        Integer.toString(time.get(Calendar.HOUR) == 0 ? 12 : time.get(Calendar.HOUR))
                        + ":" + Integer.toString(time.get(Calendar.MINUTE))
                        + ":" + Integer.toString(time.get(Calendar.SECOND))
                        + ampm);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml"));
                MainMenuController controller;
                Parent root = loader.load();
                controller = loader.getController();
                controller.setController(controller);
                controller.host = this.host;
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/task.png")));
                stage.setTitle("Oasis Club Manager");
                stage.show();
                fadein(root);
                controller.setStage(stage);
                controller.setUser(userLogin);
                primStage.close();
            } else {
                Calendar time = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
                inter.logfailLogin(user.getText(), pass.getText(), InetAddress.getLocalHost().getHostName() + " - " + System.getProperty("user.name"), LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                        Integer.toString(time.get(Calendar.HOUR) == 0 ? 12 : time.get(Calendar.HOUR))
                        + ":" + Integer.toString(time.get(Calendar.MINUTE))
                        + ":" + Integer.toString(time.get(Calendar.SECOND)));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Alerta");
                alert.setHeaderText(null);
                alert.setContentText("Usuario o Contraseña incorrectos");
                alert.show();
            }
        } catch (RemoteException | NotBoundException ex) {
            ex.printStackTrace();
        }
    }

    public void drag() {
        pass.setOnKeyReleased((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    login();
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        topPane.setOnMousePressed((MouseEvent evt) -> {
            xs = evt.getSceneX();
            ys = evt.getSceneY();
        });

        topPane.setOnMouseDragged((MouseEvent event) -> {
            primStage.setX(event.getScreenX() - xs);
            primStage.setY(event.getScreenY() - ys);
        });

        user.textProperty().addListener((ov, oldValue, newValue) -> {
            user.setText(newValue.toUpperCase());
        });
        pass.textProperty().addListener((ov, oldValue, newValue) -> {
            pass.setText(newValue.toUpperCase());
        });

    }

    public void setStage(Stage s) {
        primStage = s;
    }

    public void fadein(Node n) {
        FadeTransition ft = new FadeTransition();
        ft.setNode(n);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDuration(Duration.millis(250));
        ft.play();
    }

    public void translate(Node n, Double delay) {
        TranslateTransition tt = new TranslateTransition();
        tt.setNode(n);
        tt.setDelay(Duration.millis(delay));
        tt.setDuration(Duration.millis(800));
        tt.setFromX(800);
        tt.setToX(n.getTranslateX());
        tt.play();
    }

    public void translatey(Node n, Double delay) {
        TranslateTransition tt = new TranslateTransition();
        tt.setNode(n);
        tt.setDelay(Duration.millis(delay));
        tt.setDuration(Duration.millis(800));
        tt.setFromY(800);
        tt.setToY(n.getTranslateX());
        tt.play();
    }

    public String getHost() {
        DatagramSocket c;
        try {
            c = new DatagramSocket();
            c.setBroadcast(true);
            c.setSoTimeout(10000);
            byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8888);
                c.send(sendPacket);
            } catch (Exception e) {
            }
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
                        c.send(sendPacket);
                    } catch (Exception e) {
                    }
                }
            }
            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);
            String message = new String(receivePacket.getData()).trim();
            c.close();
            if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {
                return receivePacket.getAddress().getHostAddress();
            } else {
                return null;
            }

        } catch (IOException ex) {
            return null;
        }
    }

    public void initHost() throws IOException, SQLException {
        login.setDisable(true);
        host = getHost();
        if (host == null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Alerta");
            alert.setHeaderText("Error de conexion con el servidor");
            alert.setContentText("¿Desea intentar de nuevo la conexion?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                initHost();
            } else {

            }
        } else {
            login.setDisable(false);
        }
    }

}
