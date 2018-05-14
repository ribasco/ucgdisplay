package com.ibasco.pidisplay.misc.fft;

/**
 * Represents an audio sample.<br />
 * <br />
 * An audio sample represents the amplitude (Y-position) of an audio signal at a given time (X-position).
 * When an audio signal is graphed as a wave, time is the X-axis and amplitude is the Y-axis.
 */
public class AudioSample {
    // I used long for time so I don't have to worry about overflow. long can store 2^64 values and if my audio sample rate is 44100Hz,
    // we can run for ((2^64) / 44100) seconds = 13+ million years! An int (2^32) would only be able to last 1 day.

    // I used short for amplitude because we assume an audio sample size of 16 bits (2 bytes)

    public static final long TIME_MIN_VALUE = Long.MIN_VALUE;
    public static final long TIME_MAX_VALUE = Long.MAX_VALUE;

    public static final short AMPLITUDE_MIN_VALUE = Short.MIN_VALUE;
    public static final short AMPLITUDE_MAX_VALUE = Short.MAX_VALUE;


    public final long time;
    public final short amplitude;

    public AudioSample(long time, short amplitude) {
        this.time = time;
        this.amplitude = amplitude;
    }
}
