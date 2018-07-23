package com.ibasco.pidisplay.emulator.ui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlcdScreen extends Canvas {

    private static final Logger log = LoggerFactory.getLogger(GlcdScreen.class);

    private IntegerProperty pixelWidth = new SimpleIntegerProperty(5);

    private IntegerProperty pixelHeight = new SimpleIntegerProperty(5);

    public GlcdScreen() {
        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    private void draw() {


    }

    public int getPixelWidth() {
        return pixelWidth.get();
    }

    public IntegerProperty pixelWidthProperty() {
        return pixelWidth;
    }

    public void setPixelWidth(int pixelWidth) {
        this.pixelWidth.set(pixelWidth);
    }

    public int getPixelHeight() {
        return pixelHeight.get();
    }

    public IntegerProperty pixelHeightProperty() {
        return pixelHeight;
    }

    public void setPixelHeight(int pixelHeight) {
        this.pixelHeight.set(pixelHeight);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double minWidth(double height) {
        return super.minWidth(height);
    }

    @Override
    public double minHeight(double width) {
        return super.minHeight(width);
    }

    @Override
    public double prefWidth(double height) {
        return super.prefWidth(height);
    }

    @Override
    public double prefHeight(double width) {
        return super.prefHeight(width);
    }

    @Override
    public double maxWidth(double height) {
        return super.maxWidth(height);
    }

    @Override
    public double maxHeight(double width) {
        return super.maxHeight(width);
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        //setLayoutX(width);
        //super.setWidth(width);
        //super.setHeight(height);
    }
}
