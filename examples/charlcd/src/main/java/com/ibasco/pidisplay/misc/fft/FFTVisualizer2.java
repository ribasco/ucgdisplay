package com.ibasco.pidisplay.misc.fft;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class FFTVisualizer2 {
    // ===================================================================
    // Constants
    //
    // ===================================================================

    private static final int INIT_PANEL_WIDTH = 1200;
    private static final int INIT_PANEL_HEIGHT = 500;

    private static final int DATA_DURATION_MS = 5000;

    private static final int VISUALIZATION_AMPLITUDE_ORIGIN = 0;
    private static final int VISUALIZATION_AMPLITUDE_RANGE = 2000000;

    private static final int VISUALIZATION_TIME_RANGE_MS = 5000;
    private static final int VISUALIZATION_TIME_MARKING_INTERVAL_MS = 500;

    private static final int VISUALIZATION_MIN_FREQUENCY = 533;
    private static final int VISUALIZATION_MAX_FREQUENCY = 1533;

    private static final int VISUALIZATION_NUM_TIME_MARKINGS = DATA_DURATION_MS / VISUALIZATION_TIME_MARKING_INTERVAL_MS + 1;


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

    private long visualizationTimeOrigin;
    private long visualizationTimeRange;
    private long visualizationTimeMarkingInterval;

    private long dataDuration;
    private int dataMaxNumPoints;

    private FrequencyPlot[] frequencyPlots;


    // ===================================================================
    // Sub-Classes
    //
    // ===================================================================

    private static final class FrequencyPoint {
        public final long time;
        public final double amplitude;

        public FrequencyPoint(long time, double amplitude) {
            this.time = time;
            this.amplitude = amplitude;
        }
    }

    private static final class FrequencyPlot {
        public final double frequency;
        public final int fftSetSampleIndex;
        public final Color color;

        public final ArrayList<FrequencyPoint> points = new ArrayList<FrequencyPoint>();

        public FrequencyPlot(double frequency, int fftSetSampleIndex, Color color) {
            this.frequency = frequency;
            this.fftSetSampleIndex = fftSetSampleIndex;
            this.color = color;
        }
    }


    // ===================================================================
    // Methods
    //
    // ===================================================================

    /**
     * Creates a window for visualizing a FFT set.
     */
    public FFTVisualizer2() {
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

    public void displayFFTSets(FFTSet[] fftSets) {
        for (int i = 0; i < fftSets.length; ++i)
            displayFFTSet(fftSets[i]);
    }

    public void displayFFTSet(FFTSet fftSet) {
        if (frequencyPlots == null) {
            visualizationTimeRange = Util.msToTime(VISUALIZATION_TIME_RANGE_MS);
            dataDuration = Util.msToTime(DATA_DURATION_MS);
            visualizationTimeMarkingInterval = Util.msToTime(VISUALIZATION_TIME_MARKING_INTERVAL_MS);
            dataMaxNumPoints = (int) Math.ceil(((double) dataDuration) / Config.NUM_AUDIO_SAMPLES_IN_FFT_SET);

            Color[] colors = {
                    Color.RED,
                    Color.BLUE,
                    Color.GREEN,
                    Color.ORANGE,
                    Color.MAGENTA,
                    Color.PINK
            };

            int startFFTSetSampleIndex = 1;
            int endFFTSetSampleIndex = fftSet.fftSamples.length / 2;

            int fftSetSampleStartIndex = -1;
            int fftSetSampleEndingIndex = -1;

            for (int i = startFFTSetSampleIndex; i < endFFTSetSampleIndex; ++i) {
                if (fftSetSampleStartIndex == -1 && fftSet.fftSamples[i].frequency >= VISUALIZATION_MIN_FREQUENCY)
                    fftSetSampleStartIndex = i;

                if (fftSet.fftSamples[i].frequency > VISUALIZATION_MAX_FREQUENCY) {
                    fftSetSampleEndingIndex = i;
                    break;
                }
            }

            if (fftSetSampleStartIndex == -1 && fftSetSampleEndingIndex != -1)
                fftSetSampleStartIndex = startFFTSetSampleIndex;

            if (fftSetSampleEndingIndex == -1 && fftSetSampleStartIndex != -1)
                fftSetSampleEndingIndex = endFFTSetSampleIndex;

            if (fftSetSampleStartIndex == -1) {
                System.err.println("VISUALIZATION_MIN_FREQUENCY too high!");
                System.exit(1);
                return;
            }

            frequencyPlots = new FrequencyPlot[fftSetSampleEndingIndex - fftSetSampleStartIndex];

            for (int i = fftSetSampleStartIndex; i < fftSetSampleEndingIndex; ++i)
                frequencyPlots[i - fftSetSampleStartIndex] = new FrequencyPlot(fftSet.fftSamples[i].frequency, i, colors[(i - fftSetSampleStartIndex) % colors.length]);
        }


        // check for overflow
        if (fftSet.startTime < AudioSample.TIME_MIN_VALUE + visualizationTimeRange)
            visualizationTimeOrigin = AudioSample.TIME_MIN_VALUE;
        else
            visualizationTimeOrigin = fftSet.startTime - visualizationTimeRange;

        for (int i = 0; i < frequencyPlots.length; ++i) {
            frequencyPlots[i].points.add(new FrequencyPoint(fftSet.startTime, fftSet.fftSamples[frequencyPlots[i].fftSetSampleIndex].amplitude));

            if (frequencyPlots[i].points.size() > dataMaxNumPoints)
                frequencyPlots[i].points.remove(0);
        }

        visualizerFrame.repaint();
    }


    /**
     * Custom panel for drawing our visualization.
     */
    private class FFTVisualizerPanel extends JPanel {
        private static final long serialVersionUID = 1l;

        public void paint(Graphics g) {
            if (frequencyPlots == null)
                return;

            g.setColor(Color.GRAY);

            // make the time relative to 0 and floor to the nearest interval
            // warning! This will cause an overflow when visualizationTimeOrigin > -1
            long numMarkingIntervals = (long) Math.floor(((double) visualizationTimeOrigin - AudioSample.TIME_MIN_VALUE) / visualizationTimeMarkingInterval);

            // get the time of the number of intervals and make relative to min time
            long relativeTimeMarkingsTime = numMarkingIntervals * visualizationTimeMarkingInterval + AudioSample.TIME_MIN_VALUE;
            long relativeTimeMarkingsTimeMS = numMarkingIntervals * VISUALIZATION_TIME_MARKING_INTERVAL_MS;

            for (long i = 0; i < VISUALIZATION_NUM_TIME_MARKINGS; ++i) {
                long markingTime = relativeTimeMarkingsTime + i * visualizationTimeMarkingInterval;
                long markingTimeMS = relativeTimeMarkingsTimeMS + i * VISUALIZATION_TIME_MARKING_INTERVAL_MS;

                int x = getXForTime(markingTime);

                g.drawLine(
                        x, 0,
                        x, visualizerPanel.getHeight());

                g.drawString((markingTimeMS / 1000.0f) + "s", x + 5, visualizerPanel.getHeight() - 10);
            }


            for (int i = VISUALIZATION_AMPLITUDE_ORIGIN; i < VISUALIZATION_AMPLITUDE_RANGE; i += 100000) {
                int y = getYForAmplitude(i);

                g.drawLine(
                        0, y,
                        visualizerPanel.getWidth(), y);

                g.drawString(i / 100000 + " kdB", 1, y - 1);
            }


            g.setColor(Color.WHITE);
            g.fillRect(visualizerPanel.getWidth() - 90, 0, 90, 15 * frequencyPlots.length + 20);

            g.setColor(Color.GRAY);
            g.drawRect(visualizerPanel.getWidth() - 90, -1, 91, 15 * frequencyPlots.length + 21);

            for (int i = 0; i < frequencyPlots.length; ++i) {
                g.setColor(frequencyPlots[i].color);
                g.drawString((int) frequencyPlots[i].frequency + " Hz", visualizerPanel.getWidth() - 80, 20 + 15 * i);

                for (int j = 1; j < frequencyPlots[i].points.size(); ++j) {
                    g.drawLine(
                            getXForTime(frequencyPlots[i].points.get(j - 1).time),
                            getYForAmplitude(frequencyPlots[i].points.get(j - 1).amplitude),
                            getXForTime(frequencyPlots[i].points.get(j).time),
                            getYForAmplitude(frequencyPlots[i].points.get(j).amplitude));

                    //g.drawLine(getXForTime(frequencyPlots[i].points.get(j).time), getYForAmplitude(frequencyPlots[i].points.get(j).amplitude) - 10, getXForTime(frequencyPlots[i].points.get(j).time), getYForAmplitude(frequencyPlots[i].points.get(j).amplitude) + 10);
                }
            }
        }
    }

    /**
     * Translates the given frequency into an X position on the visualization.
     *
     * @param frequency - Frequency to translate.
     * @return X position on the visualization.
     */
    private int getXForTime(long time) {
        return (int) ((time - visualizationTimeOrigin) * (visualizerPanel.getWidth() / ((double) visualizationTimeRange)));
    }

    /**
     * Translates the given amplitude into an Y position on the visualization.
     *
     * @param amplitude - Amplitude to translate.
     * @return Y position on the visualization.
     */
    private int getYForAmplitude(double amplitude) {
        return visualizerPanel.getHeight() - 50 - (int) ((amplitude - VISUALIZATION_AMPLITUDE_ORIGIN) * ((visualizerPanel.getHeight() - 50) / ((float) VISUALIZATION_AMPLITUDE_RANGE)));
    }
}

