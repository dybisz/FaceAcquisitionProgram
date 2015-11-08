package video;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 * Created by dybisz on 25/10/2015.
 */
public class FaceDetection extends FrameGrabber {
    public FaceDetection(ImageView videoFrame, VideoCapture capture) {
        super(videoFrame, capture);
    }

    protected Image processFrame(Mat frame) {
        int averageLuminance = getAverageLuminance(frame);
        System.out.println(averageLuminance);
        Mat compensatedImage = getSkinRegions(frame, averageLuminance);
        return mat2Image(compensatedImage);
    }

    private int getAverageLuminance(Mat frame) {
        double averageLuminance = 0;
        int channels = frame.channels();
        int size = (int) (frame.total() * channels);
        double[] buff = new double[size];

        frame.convertTo(frame, CvType.CV_64FC3);
        frame.get(0, 0, buff);

        for (int i = 0; i < size; i += frame.channels()) {
            averageLuminance += 0.3 * buff[i + 2] + 0.6 * buff[i + 1] + 0.1 * buff[i];
        }
        averageLuminance /= frame.total();
        return (int) averageLuminance;
    }

    private Mat getSkinRegions(Mat frame, int averageLuminance) {
        double gammaFactor = determineGammaFactor(averageLuminance);
//        Mat compensatedImage = frame.clone();
        int channels = frame.channels();
        int size = (int) (frame.total() * channels);
        double[] buff = new double[size];

        frame.convertTo(frame, CvType.CV_64FC3);
        frame.get(0, 0, buff);

        for (int i = 0; i < size; i += frame.channels()) {
            double rPrime = Math.pow(buff[i+2], gammaFactor);
            double gPrime = Math.pow(buff[i+1], gammaFactor);
            double chrominance = 0.5 * rPrime - 0.419 * gPrime - 0.081 * buff[i];
            double val = (chrominance > 10.0 && chrominance < 45.0) ? 0.0 : 255.0;
            buff[i] = buff[i+1] = buff[i+2] = val;
        }
        frame.put(0, 0, buff);

        return frame;
    }

    private double determineGammaFactor(int averageLuminance) {
        if(averageLuminance < 64) return 1.4;
        else if(averageLuminance > 192) return 0.6;
        else return 1.0;
    }

}
