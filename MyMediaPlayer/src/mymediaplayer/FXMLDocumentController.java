/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mymediaplayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author Asus
 */
public class FXMLDocumentController implements Initializable {
    
    private String path;
    private MediaPlayer mediaPlayer;
    
    @FXML
    private MediaView mediaView;
    
    @FXML
    private Slider progressBar;
    
    @FXML
    private Slider VolumeSlider;
    
    public void chooseFileMethod(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        path = file.toURI().toString();
        
        if(path != null)
        {
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media); 
            mediaView.setMediaPlayer(mediaPlayer);
            
            //For syncing the mediaView when the window increases or decreases
            DoubleProperty widthProp = mediaView.fitWidthProperty();
            DoubleProperty heightProp = mediaView.fitHeightProperty();
            
            widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
            
            
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>(){
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    progressBar.setValue(newValue.toSeconds());

                }
                });
            
            progressBar.setOnMousePressed(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                }
            }
            
            );
            
             progressBar.setOnMouseDragged(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                }
            }
            
            );
             
             mediaPlayer.setOnReady(new Runnable()
             {
                @Override
                public void run() {
                    Duration total = media.getDuration();
                    progressBar.setMax(total.toSeconds());
                }
                  
             }
             );
             
             
            VolumeSlider.setValue(mediaPlayer.getVolume()*100);
            VolumeSlider.valueProperty().addListener(new InvalidationListener()
            {
                @Override
                public void invalidated(Observable observable) {
                     mediaPlayer.setVolume(VolumeSlider.getValue()/100);
                }
               
            }
            );
            

            
            
            
            
            mediaPlayer.play();
            
        }
        
    }
    
    public void play(ActionEvent event)
    {
        mediaPlayer.play();
        mediaPlayer.setRate(1);
    }
    
    public void pause(ActionEvent event)
    {
        mediaPlayer.pause();
    }
    
    public void stop(ActionEvent event)
    {
        mediaPlayer.stop();
    }
    
    public void slowRate(ActionEvent event)
    {
        mediaPlayer.setRate(0.5);
    }
    public void fastForward(ActionEvent event)
    {
        mediaPlayer.setRate(2);
    }
    
    public void skip10(ActionEvent event)
    {
        //seek takes you to nth second, but as we need to add 10 second, 
        // we will use current second+10 seconds
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
    }
    
    public void back10(ActionEvent event)
    {
        //seek takes you to nth second, but as we need to add 10 second, 
        // we will use current second+10 seconds
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
