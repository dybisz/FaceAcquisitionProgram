import controllers.MainWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class Main extends Application {

    MainWindow mainWindow = new MainWindow();

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Face Acquisition Program (FAP)");
        primaryStage.setScene(new Scene(mainWindow, 500, 275));
        primaryStage.show();
    }

    @Override
    public void stop(){
        mainWindow.stopTasks();
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
}
