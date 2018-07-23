package com.ibasco.pidisplay.emulator.ui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.concurrent.atomic.AtomicReference;

abstract public class GlcdRedrawTask<T> extends AnimationTimer {
    private final AtomicReference<T> data = new AtomicReference<T>(null);
    private final Canvas canvas;

    public GlcdRedrawTask(Canvas canvas) {
        this.canvas = canvas;
    }

    public void redraw(T dataToDraw) {
        data.set(dataToDraw);
        start();
    }

    public void handle(long now) {
        // check if new data is available
        T dataToDraw = data.getAndSet(null);
        if (dataToDraw != null) {
            redraw(canvas.getGraphicsContext2D(), dataToDraw);
        }
    }

    protected abstract void redraw(GraphicsContext context, T data);
}
