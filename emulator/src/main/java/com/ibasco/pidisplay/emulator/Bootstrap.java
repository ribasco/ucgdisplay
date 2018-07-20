package com.ibasco.pidisplay.emulator;

import com.ibasco.pidisplay.emulator.ui.controllers.EmulatorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.net.URL;

import static org.slf4j.LoggerFactory.getLogger;

public class Bootstrap extends Application {

    private static final Logger log = getLogger(Bootstrap.class);

    static EmulatorController emulatorController;

    public URL getFxmlResource(String resourceName) {
        return getClass().getClassLoader().getResource(String.format("%s.fxml", resourceName));
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getFxmlResource("emulator"));
        emulatorController = loader.getController();
        Parent root = loader.load();

        Scene scene = new Scene(root, 1024, 512);
        /*stage.setMaxWidth(1024);
        stage.setMaxHeight(630);
        stage.setMinWidth(1024);
        stage.setMinHeight(630);*/

        stage.setMinWidth(688);
        stage.setMinHeight(640);
        stage.setMaxWidth(688);
        stage.setMaxHeight(640);
        stage.setTitle("GLCD Emulator");
        stage.setScene(scene);
        stage.show();

        Canvas emulatorCanvas = (Canvas) scene.lookup("#emulatorCanvas");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
