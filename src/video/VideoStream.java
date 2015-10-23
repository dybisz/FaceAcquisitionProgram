package video;

import javafx.scene.image.ImageView;
import org.opencv.videoio.VideoCapture;
import util.FrameGrabber;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Handles streaming video from available camera.
 * <p></p>
 * Created by dybisz on 23/10/2015.
 */
public class VideoStream {
    private final static double REFRESH_RATE = 33;
    private ScheduledExecutorService timer;
    private VideoCapture videoCapture = new VideoCapture();
    private ImageView videoFrame;

    public VideoStream(ImageView videoFrame) {
        this.videoFrame = videoFrame;
    }

    public void start() {
        startCapturingVideo();

        if (videoStreamOpen())
        {
            startFrameProcessing();
        }
        else
        {
            System.err.println("Impossible to open the camera connection...");
        }
    }

    private void startCapturingVideo() {
        this.videoCapture.open(0);
    }


    private boolean videoStreamOpen() {
        return this.videoCapture.isOpened();
    }

    /**
     * Grabs frame every 33 milliseconds and displays it in {@link #videoFrame}
     */
    private void startFrameProcessing() {
        FrameGrabber frameGrabber =  new FrameGrabber(videoFrame, videoCapture);
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        try
        {
            shutDownTimer();
        }
        catch (InterruptedException e){}
        releaseResources();

    }

    private void shutDownTimer() throws InterruptedException {
        timer.shutdown();
        timer.awaitTermination(33, TimeUnit.MILLISECONDS);
    }

    private void releaseResources() {
        videoCapture.release();
        videoFrame.setImage(null);
    }
}
