package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.display.lcd.components.LcdDisplayContainer;
import com.ibasco.pidisplay.display.lcd.components.LcdDisplayMenu;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class TestApp extends Application {

    private Map<String, Scene> sceneMap = new HashMap<>();
    private Scene scene1, scene2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {

        LcdDisplayContainer mainMenu = new LcdDisplayContainer();
        mainMenu.addComponent(new LcdDisplayMenu());


        primaryStage.setTitle("My First JavaFX GUI");
        Group g = new Group();

        //Scene 1
        Label label1 = new Label("This is the first scene");
        Button button1 = new Button("Go to scene 2");
        button1.setOnAction(e -> {
            fadeOut(primaryStage, scene2);
            System.out.println("Goto Scene 2");
        });
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label1, button1);
        scene1 = new Scene(layout1, 300, 250);

        //Scene 2
        VBox layout2 = new VBox(20);
        Label label2 = new Label("This is the second scene");
        Button button2 = new Button("Go to scene 1");
        button2.setOnAction(e -> {
            fadeOut(primaryStage, scene1);
            System.out.println("Goto Scene 1");
        });


        layout2.getChildren().addAll(label2, button2);
        scene2 = new Scene(layout2, 100, 50);

        Stage stage2 = new Stage();
        stage2.setScene(scene2);
        stage2.show();

        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    public void fadeOut(Stage primary, Scene destination) {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), primary.getScene().getRoot());
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(event -> primary.setScene(destination));
        ft.play();
    }

    public Scene getScene(String key) {
        return sceneMap.computeIfAbsent(key, s -> {
            if ("primary".equals(s)) {
                return new Scene(new Group(), 500, 500, Color.BLACK);
            }
            return new Scene(new Group(), 500, 500, Color.RED);
        });
    }
}
