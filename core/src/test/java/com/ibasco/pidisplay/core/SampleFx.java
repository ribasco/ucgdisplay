package com.ibasco.pidisplay.core;


import javafx.application.Application;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class SampleFx extends Application {

    private static MediaPlayer mp;

    private AudioSpectrumListener spectrumListener;

    public SampleFx() {
        this.spectrumListener = new AudioSpectrumListener() {
            @Override
            public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                /*for (int i = 0; i < series1Data.length; i++) {
                    series1Data[i].setYValue(magnitudes[i] + 60);
                }*/
            }
        };
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL mediaUrl = getClass().getClassLoader().getResource("Girl.mp3");
        Media media = new Media(mediaUrl.toURI().toString());

        CompletableFuture<Void> player = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                mp = new MediaPlayer(media);
                mp.setVolume(0.3);
                mp.play();
                System.out.println("Playing music: " + media.getSource());
            }
        });
        player.get();
        System.out.println("WOOT");
    }
}
