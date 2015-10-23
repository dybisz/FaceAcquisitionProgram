package util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;

/**
 * Class based on introduction tutorial from:
 * http://opencv-java-tutorials.readthedocs.org/en/latest
 * /03%20-%20First%20JavaFX%20Application%20with%20OpenCV.html
 * <p></p>
 * Created by dybisz on 23/10/2015.
 */
public class FrameGrabber implements Runnable {
    private ImageView videoFrame;
    private VideoCapture capture;
    
    public FrameGrabber(ImageView videoFrame, VideoCapture capture) {
        this.videoFrame = videoFrame;
        this.capture = capture;
    }

    @Override
    public void run() {
        Image imageToShow = grabFrame();
        videoFrame.setImage(imageToShow);
    }

    /**
     * https://github.com/opencv-java/getting-started/blob/master
     * /FXHelloCV/src/it/polito/elite/teaching/cv/FXHelloCVController.java
     */
    private Image grabFrame() {
        // init everything
        Image imageToShow = null;
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened())
        {
            try
            {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty())
                {
                    // convert the image to gray scale
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                    // convert the Mat object (OpenCV) to Image (JavaFX)
                    imageToShow = mat2Image(frame);
                }

            }
            catch (Exception e)
            {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return imageToShow;
    }

    /**
     * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
     *
     * https://github.com/opencv-java/getting-started/blob/master
     * /FXHelloCV/src/it/polito/elite/teaching/cv/FXHelloCVController.java
     *
     * @param frame
     *            the {@link Mat} representing the current frame
     * @return the {@link Image} to show
     */
    private Image mat2Image(Mat frame)
    {
        // create a temporary buffer
        MatOfByte buffer = new MatOfByte();
        // encode the frame in the buffer
        Imgcodecs.imencode(".png", frame, buffer);
        // build and return an Image created from the image encoded in the
        // buffer
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
