package controllers;

import image.ImageOperations;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import util.FxmlLoader;
import video.FaceDetection;
import video.VideoStream;

/**
 * <p></p>
 * Created by dybisz on 23/10/2015.
 */
public class MainWindow extends AnchorPane {

    private VideoStream videoStream;

    @FXML
    ImageView videoFrame;

    public MainWindow() {
        FxmlLoader.loadFxml(this, "/fxml/main_window.fxml");
//        this.videoStream = new VideoStream(videoFrame);
        VideoStream imageOperations = new VideoStream(videoFrame);
        videoFrame.setPreserveRatio(true);
        bindProperties();
//        videoStream.start();
        imageOperations.start();
    }

    private void bindProperties() {
        videoFrame.fitHeightProperty().bind(heightProperty());
        videoFrame.fitWidthProperty().bind(widthProperty());
    }

    public void stopTasks() {
//        videoStream.stop();
    }
}
