/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class SeleccionaFotoController implements Initializable {

     @FXML
     Group group;
     @FXML
     ImageView foto;
     Rectangle rect;
     @FXML
     Slider tam;
     double x0, y0, xf, yf;
     double width;
     double height;
     Image pic;
     CarnetController carnet;
     Stage myStage;

     @Override
     public void initialize(URL url, ResourceBundle rb) {

     }

     public void confirmar(ActionEvent evt) {
          Image photo = new WritableImage(pic.getPixelReader(), (int) rect.getLayoutX(), (int) rect.getLayoutY(), (int) rect.getWidth(), (int) rect.getHeight());
          carnet.setFoto(photo);
          myStage.close();
     }

     public void addRectangle(Group pic) {
          rect = new Rectangle(0, 0, 0, 0);
          rect.setStroke(Color.BLUE);
          rect.setStrokeWidth(1);
          rect.setStrokeLineCap(StrokeLineCap.ROUND);
          rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));
          rect.setWidth(10);
          rect.setHeight(14);
          group.getChildren().add(rect);
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
                    if (rect.getWidth() + x0 < width && x0 >= 0) {
                         rect.setLayoutX(x0);
                    }
                    if (rect.getHeight() + y0 < height && y0 >= 0) {
                         rect.setLayoutY(y0);
                    }
                    xf = evt.getSceneX();
                    yf = evt.getSceneY();
               }
          });
     }

     public void setFoto(Image im) {
          if(im.getWidth() >700 || im.getHeight()>410){
               im=scale(im);
          }
          width = im.getWidth();
          height = im.getHeight();
          foto.setImage(im);
          if (width <= 700) {
               foto.setFitWidth(width);
          } else {
               foto.setFitWidth(700);
          }
          if (height <= 410) {
               foto.setFitHeight(height);
          } else {
               foto.setFitHeight(410);
          }
          width = foto.getFitWidth();
          height = foto.getFitHeight();
          addRectangle(group);
          setSlider();
          this.pic = im;
     }

     public void setSlider() {
          tam.setMin(10);
          tam.setMax(height);
          tam.setValue(105);
          tam.valueProperty().addListener(new ChangeListener<Number>() {
               public void changed(ObservableValue<? extends Number> ov,
                      Number old_val, Number new_val) {

                    rect.setHeight((double) new_val);
                    rect.setWidth((rect.getHeight() / 1.4));
                    if (rect.getLayoutX() + rect.getWidth() > width) {
                         rect.setLayoutX(rect.getLayoutX() - (rect.getLayoutX() + rect.getWidth() - width));
                    }
                    if (rect.getLayoutY() + rect.getHeight() > height) {
                         rect.setLayoutY(rect.getLayoutY() - (rect.getLayoutY() + rect.getHeight() - height));
                    }
               }
          });

     }
     
     public Image scale(Image source) {
    ImageView imageView = new ImageView(source);
    imageView.setPreserveRatio(true);
    imageView.setFitWidth(700);
    imageView.setFitHeight(410);
    return imageView.snapshot(null, null);
}

}
