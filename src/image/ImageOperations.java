package image;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dybisz on 25/10/2015.
 */
public class ImageOperations {
    private final static String DEFAULT_IMAGE_URL = "default5.jpg";
    ImageView imageView;

    public ImageOperations(ImageView imageView) {
        this.imageView = imageView;
    }

    public void start() {
        Image image = new Image(DEFAULT_IMAGE_URL, false);
        PixelReader inputReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage outputImage = new WritableImage(width, height);
        PixelWriter outputWriter = outputImage.getPixelWriter();
        PixelReader outputReader = outputImage.getPixelReader();

        List<Double> YChannel = new ArrayList<>();
        for(int x = 0; x < width; x++) {
            for ( int y = 0 ; y < height; y++) {
                Color inputColor = inputReader.getColor(x,y);
//                double Y = 16.0/255.0 + (0.25678824 *((255 * inputColor.getRed())) +
//                        (0.50412941 * (255 * inputColor.getGreen())) +
//                        (0.09790588 * (255 * inputColor.getBlue()))); // Y
                double Y = (0.3 *((255.0 * inputColor.getRed())) +
                        (0.6 * (255.0 * inputColor.getGreen())) +
                        (0.1 * (255.0 * inputColor.getBlue())));
                // !!!!!!!!!! + 16/255  i porownac wyniki !!!!!!!!!!!!!!!!!!!!
                YChannel.add(Y);
//                System.out.println(Y);
            }
        }
//        normalize(YChannel);
        double Yavg = getAverage(YChannel);
        System.out.println(Yavg);

        // GAMMA FACTOR
        double T = 1.0;
        if(Yavg < 64) T = 11.4;
        else if (Yavg > 192) T = 0.6;

//        if( T != 1) {
            for(int x = 0; x < width; x++) {
                for(int y = 0; y < height; y++) {
                    Color inputColor = inputReader.getColor(x,y);
                    double redPrime = Math.pow(inputColor.getRed(), 1.4);
                    double greenPrime = Math.pow(inputColor.getGreen(), 1.4);
                    Color outputColor = new Color(redPrime, greenPrime, inputColor.getBlue(), 1.0);
                    outputWriter.setColor(x, y, outputColor);
                }
            }
//        }


        // ----------------  SKIN EXTRACTION -------------------------
        List<Double> CrChannel = new ArrayList<>();
        for(int x = 0; x < width; x++) {
            for ( int y = 0 ; y < height; y++) {
                Color inputColor = outputReader.getColor(x,y);
                double Cr = (0.43921569 *((255.0 * inputColor.getRed())) +
                        ( -0.36778831 * (255.0 * inputColor.getGreen())) +
                        (-0.07142737 * (255.0 * inputColor.getBlue()))); // Y

//                double Cr = (0.5 *((255.0 * inputColor.getRed())) +
//                        ( -0.419 * (255.0 * inputColor.getGreen())) +
//                        (-0.081 * (255.0 * inputColor.getBlue()))); // Y
//                System.out.println(Cr + 128.0/255.0);
                CrChannel.add(Cr);

                if(10<Cr && Cr<45) {
                    outputWriter.setColor(x, y, new Color(1, 1, 1, 1));
                }
                else {
                    outputWriter.setColor(x, y, new Color(0, 0, 0, 1));
                }

            }
        }

        imageView.setImage(outputImage);

    }


    private double getAverage(List<Double> yChannel) {
        double avg = 0.0;
        for (Double y : yChannel) {
            avg += y;
        }

        avg /= (double)yChannel.size();
        return avg;
    }

    private void normalize(List<Double> yChannel) {
        double min = min(yChannel);
        double max = max(yChannel);
        System.out.println("min" + min + " max: " + max);

        for(int i = 0; i < yChannel.size(); i++) {
            yChannel.set(i, 255.0 * ( (yChannel.get(i) - min) / (max - min) ) );
        }

    }

    private double min(List<Double> yChannel) {
        double min = 100000;
        for(Double y : yChannel) {
            if(y < min) {
                min = y;
            }
        }
        return min;
    }

    private double max(List<Double> yChannel) {
        double max = -1000000;
        for(Double y : yChannel) {
            if(y > max) {
                max = y;
            }
        }
        return max;
    }
}
