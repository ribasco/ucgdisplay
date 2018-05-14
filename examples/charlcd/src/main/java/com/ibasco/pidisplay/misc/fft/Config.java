package com.ibasco.pidisplay.misc.fft;

public final class Config {
    public static final int NUM_AUDIO_SAMPLES_IN_FFT_SET = 256;

    private static int sampleRate;

    public static void setSampleRate(int _sampleRate) {
        sampleRate = _sampleRate;
    }

    public static int getSampleRate() {
        return sampleRate;
    }
}
