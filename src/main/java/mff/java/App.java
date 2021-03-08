package mff.java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mff.java.utils.PathUtils;
import mff.java.utils.UiUtils;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static final int minWidth = 1280;
    private static final int minHeight = 720;
    private static final String windowTitle = "Task manager";
    private static final String iconFileName = "icon.png";

    @Override
    public void start(Stage stage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler(App::handleException);

        scene = new Scene(loadFXML("mainPage"), minWidth, minHeight);
        stage.setScene(scene);

        stage.setMinHeight(minHeight);
        stage.setMinWidth(minWidth);
        stage.setTitle(windowTitle);
        stage.getIcons().add(getIcon());

        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * default exception handler - show error dialog and close the app
     *
     * @param t
     * @param e
     */
    private static void handleException(Thread t, Throwable e) {
        var dialogResult = UiUtils.showErrorDialog("An error occurred", "Please restart the app");
        Platform.exit();
    }

    /**
     * get icon from resources folder
     *
     * @return icon image
     */
    private static Image getIcon() {
        var iconPath = Paths.get(PathUtils.getPackageResourcesFolderPath().toString(), iconFileName);
        return new Image("file:" + iconPath.toString());
    }
}