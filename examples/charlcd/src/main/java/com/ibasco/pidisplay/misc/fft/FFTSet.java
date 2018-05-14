package com.ibasco.pidisplay.misc.fft;

/**
 * Represents an FFT audio sample.
 */
public class FFTSet {
    public final FFTSample[] fftSamples;
    public final long startTime;

    public FFTSet() {
        fftSamples = new FFTSample[0];
        startTime = 0;
    }

    public FFTSet(AudioSample[] samples, int sampleRate) {
        if (samples.length == 0) {
            fftSamples = new FFTSample[0];
            startTime = 0;
            return;
        }


        fftSamples = FFT.compute(samples, sampleRate);
        startTime = samples[0].time;
    }
}
