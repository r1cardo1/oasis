/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.Dimension;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class TomarFotoController implements Initializable {

    @FXML
    Pane pic;
    @FXML
    Pane cam;
    @FXML
    Slider tam;
    @FXML
    ComboBox<Webcam> camaras;
    Stage myStage;
    TomarFotoController myController;
    static Webcam webcam;
    static WebcamPanel panel;
    Rectangle rect;
    Double x0, y0, xf, yf;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tam.setDisable(true);
        setSlider();
        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.QVGA.getSize());
        Dimension size = WebcamResolution.QVGA.getSize();
        panel = new WebcamPanel(webcam, size, true);        
        SwingNode swcam = new SwingNode();
        swcam.setContent(panel);
        cam.getChildren().add(swcam);
        initCombo();
    }

    public void initCombo() {
        List<Webcam> list = Webcam.getWebcams();
        for(Webcam w:list){
            camaras.getItems().add(w);
        }
        camaras.getSelectionModel().select(webcam);
        camaras.valueProperty().addListener(new ChangeListener<Webcam>() {
            @Override
            public void changed(ObservableValue<? extends Webcam> observable, Webcam oldValue, Webcam newValue) {
                webcam.close();
                webcam=newValue;
                webcam.close();
                webcam.setViewSize(WebcamResolution.QVGA.getSize());
                panel.start();
                
            }
    });
    }

    public void tomaFoto() {
        pic.getChildren().clear();
        ImageView img = new ImageView();
        Image im = SwingFXUtils.toFXImage(webcam.getImage(), null);
        img.setFitHeight(240);
        img.setFitWidth(320);
        img.setImage(im);

        pic.getChildren().add(img);
        addRectangle(pic);
        setSlider();
        tam.setDisable(false);
    }

    public void addRectangle(Pane pic) {
        rect = new Rectangle(0, 0, 0, 0);
        rect.setStroke(Color.BLUE);
        rect.setStrokeWidth(1);
        rect.setStrokeLineCap(StrokeLineCap.ROUND);
        rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));
        rect.setWidth(75);
        rect.setHeight(105);
        pic.getChildren().add(rect);
        rect.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                x0 = rect.getLayoutX();
                y0 = rect.getLayoutY();
                xf = evt.getSceneX();
                yf = evt.getSceneY();
            }
        });

        rect.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                x0 += evt.getSceneX() - xf;
                y0 += evt.getSceneY() - yf;
                if (rect.getWidth() + x0 < 320 && x0 >= 0) {
                    rect.setLayoutX(x0);
                }
                if (rect.getHeight() + y0 < 240 && y0 >= 0) {
                    rect.setLayoutY(y0);
                }
                xf = evt.getSceneX();
                yf = evt.getSceneY();
            }
        });
    }

    public void setSlider() {
        tam.setMin(105);
        tam.setMax(240);
        tam.setValue(105);
        tam.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                rect.setHeight((double) new_val);
                rect.setWidth((rect.getHeight() / 1.4));
            }
        });

    }
    
}
    

