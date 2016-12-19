/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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
    Slider tam;
    @FXML
    ComboBox<WebCamInfo> camaras;
    Stage myStage;
    TomarFotoController myController;
    CarnetController carnet;
    Rectangle rect;
    Double x0, y0, xf, yf;
    private FlowPane bottomCameraControlPane;
    private FlowPane topPane;
    private BorderPane root;
    private String cameraListPromptText = "Choose Camera";
    @FXML
    ImageView imgWebCamCapturedImage;
    private Webcam webCam = null;
    private boolean stopCamera = false;
    private BufferedImage grabbedImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
    private BorderPane webCamPane;
    ImageView img;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCombo();
    }

    public void initCombo() {
        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();
        int webCamCounter = 0;
        for (Webcam webcam : Webcam.getWebcams()) {
            WebCamInfo webCamInfo = new WebCamInfo();
            webCamInfo.setWebCamIndex(webCamCounter);
            webCamInfo.setWebCamName(webcam.getName());
            options.add(webCamInfo);
            webCamCounter++;
        }
        camaras.setItems(options);
        camaras.setPromptText(cameraListPromptText);
        camaras.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<WebCamInfo>() {

            @Override
            public void changed(ObservableValue<? extends WebCamInfo> arg0, WebCamInfo arg1, WebCamInfo arg2) {
                if (arg2 != null) {
                    System.out.println("WebCam Index: " + arg2.getWebCamIndex() + ": WebCam Name:" + arg2.getWebCamName());
                    initializeWebCam(arg2.getWebCamIndex());
                }
            }
        });
    }
    
    protected void initializeWebCam(final int webCamIndex) {

		Task<Void> webCamTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				if (webCam != null) {
					disposeWebCamCamera();
				}

				webCam = Webcam.getWebcams().get(webCamIndex);
				webCam.open();

				startWebCamStream();

				return null;
			}
		};

		Thread webCamThread = new Thread(webCamTask);
		webCamThread.setDaemon(true);
		webCamThread.start();
	}

    public void tomaFoto() {
        pic.getChildren().clear();
        img = new ImageView();
        Image im = SwingFXUtils.toFXImage(webCam.getImage(), null);
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
                if(rect.getLayoutX()+rect.getWidth()>320)
                    rect.setLayoutX(rect.getLayoutX()-(rect.getLayoutX()+rect.getWidth()-320));
                if(rect.getLayoutY()+rect.getHeight()>240)
                    rect.setLayoutY(rect.getLayoutY()-(rect.getLayoutY()+rect.getHeight()-240));
            }
        });

    }

    protected void disposeWebCamCamera() {
        stopCamera = true;
        webCam.close();
    }

    protected void startWebCamCamera() {
        stopCamera = false;
        startWebCamStream();
    }

    protected void stopWebCamCamera() {
        stopCamera = true;
    }

    protected void startWebCamStream() {

        stopCamera = false;

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                final AtomicReference<WritableImage> ref = new AtomicReference<>();
                BufferedImage img = null;

                while (!stopCamera) {
                    try {
                        if ((img = webCam.getImage()) != null) {
                            ref.set(SwingFXUtils.toFXImage(img, ref.get()));
                            img.flush();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    imageProperty.set(ref.get());
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        imgWebCamCapturedImage.imageProperty().bind(imageProperty);

    }

    private class WebCamInfo {

        private String webCamName;
        private int webCamIndex;

        public String getWebCamName() {
            return webCamName;
        }

        public void setWebCamName(String webCamName) {
            this.webCamName = webCamName;
        }

        public int getWebCamIndex() {
            return webCamIndex;
        }

        public void setWebCamIndex(int webCamIndex) {
            this.webCamIndex = webCamIndex;
        }

        @Override
        public String toString() {
            return webCamName;
        }
    }
    
    public void confirmar(){
        carnet.foto.setImage(img.getImage());
    }

}
