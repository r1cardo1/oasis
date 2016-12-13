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
import java.util.ResourceBundle;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class TomarFotoController implements Initializable {
    @FXML
    AnchorPane cam,pic;
    Stage myStage;
    TomarFotoController myController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.QVGA.getSize());
        webcam.open();
        Dimension size = WebcamResolution.QVGA.getSize();
        WebcamPanel panel = new WebcamPanel(webcam,size,false);
        SwingNode swcam = new SwingNode();
        swcam.setContent(panel);
        cam.getChildren().add(swcam);
    }    
    
}
