package com.ibasco.pidisplay.misc.fft;

import javax.swing.*;
import java.awt.*;


public class FFTVisualizer {
    // ===================================================================
    // Constants
    //
    // ===================================================================

    private static final int INIT_PANEL_WIDTH = 1200;
    private static final int INIT_PANEL_HEIGHT = 500;

    private static final int VISUALIZATION_MIN_FREQUENCY = 0; // in Hz
    private static final int VISUALIZATION_MAX_FREQUENCY = 8000;

    private static final int VISUALIZATION_MIN_AMPLITUDE = 0; // in dB
    private static final int VISUALIZATION_MAX_AMPLITUDE = 2000000;

    // ===================================================================
    // Variables
    //
    // ===================================================================

    // -------------------------------------------------------------------
    // window elements

    private final JFrame visualizerFrame;
    private FFTVisualizerPanel visualizerPanel;


    // -------------------------------------------------------------------
    // visualization data

    private FFTSet fftSet;


    // ===================================================================
    // Methods
    //
    // ===================================================================

    /**
     * Creates a window for visualizing a FFT set.
     */
    public FFTVisualizer() {
        fftSet = new FFTSet();

        // I know very little about Java windows, so the bellow code is probably terrible...
        // please let me know how I can improve it.

        // create the frame
        visualizerFrame = new JFrame("AudioVisualizer");

        visualizerFrame.setBackground(Color.WHITE);
        visualizerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // create the panel
        visualizerPanel = new FFTVisualizerPanel();
        visualizerPanel.setPreferredSize(new Dimension(INIT_PANEL_WIDTH, INIT_PANEL_HEIGHT));
        visualizerFrame.getContentPane().add(visualizerPanel, java.awt.BorderLayout.CENTER);

        // finalize and display
        visualizerFrame.pack();
        visualizerFrame.setVisible(true);
    }


    public void displayFFTSet(FFTSet fftSet) {
        this.fftSet = fftSet;
        visualizerFrame.repaint();
    }


    /**
     * Custom panel for drawing our visualization.
     */
    private class FFTVisualizerPanel extends JPanel {
        private static final long serialVersionUID = 1l;

        public void paint(Graphics g) {
            g.setColor(Color.BLACK);
            for (int i = 1; i < fftSet.fftSamples.length; ++i) {
                g.drawLine(
                        getXForFrequency(fftSet.fftSamples[i - 1].frequency),
                        getYForAmplitude(fftSet.fftSamples[i - 1].amplitude),
                        getXForFrequency(fftSet.fftSamples[i].frequency),
                        getYForAmplitude(fftSet.fftSamples[i].amplitude));
            }

            g.setColor(Color.GRAY);
            for (int i = VISUALIZATION_MIN_FREQUENCY; i < VISUALIZATION_MAX_FREQUENCY; i += 1000) {
                int x = getXForFrequency(i);

                g.drawLine(
                        x, 0,
                        x, visualizerPanel.getHeight());

                g.drawString(i + "Hz", x + 5, visualizerPanel.getHeight() - 10);
            }


            for (int i = VISUALIZATION_MIN_AMPLITUDE; i < VISUALIZATION_MAX_AMPLITUDE; i += 100000) {
                int y = getYForAmplitude(i);

                g.drawLine(
                        0, y,
                        visualizerPanel.getWidth(), y);

                g.drawString(i + "dB", 0, y);
            }

        }
    }

    /**
     * Translates the given frequency into an X position on the visualization.
     *
     * @param frequency
     *         - Frequency to translate.
     *
     * @return X position on the visualization.
     */
    private int getXForFrequency(double frequency) {
        return (int) ((frequency - VISUALIZATION_MIN_FREQUENCY) * (visualizerPanel.getWidth() / ((float) VISUALIZATION_MAX_FREQUENCY - VISUALIZATION_MIN_FREQUENCY)));
    }

    /**
     * Translates the given amplitude into an Y position on the visualization.
     *
     * @param amplitude
     *         - Amplitude to translate.
     *
     * @return Y position on the visualization.
     */
    private int getYForAmplitude(double amplitude) {
        return visualizerPanel.getHeight() - 50 - (int) ((amplitude - VISUALIZATION_MIN_AMPLITUDE) * ((visualizerPanel.getHeight() - 50) / ((float) VISUALIZATION_MAX_AMPLITUDE - VISUALIZATION_MIN_AMPLITUDE)));
    }
}

