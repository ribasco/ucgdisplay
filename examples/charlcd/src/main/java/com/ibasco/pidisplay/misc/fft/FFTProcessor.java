package com.ibasco.pidisplay.misc.fft;

public class FFTProcessor {
    // ===================================================================
    // Variables
    //
    // ===================================================================

    private int numAudioSamplesInBuffer;
    private final AudioSample[] audioSampleBuffer;


    // ===================================================================
    // Methods
    //
    // ===================================================================

    public FFTProcessor() {
        numAudioSamplesInBuffer = 0;
        audioSampleBuffer = new AudioSample[Config.NUM_AUDIO_SAMPLES_IN_FFT_SET];
    }


    public FFTSet[] processAudioSamples(AudioSample[] samples) {
        // create array to store all the FFT sets we will create
        FFTSet[] fftSets = new FFTSet[(numAudioSamplesInBuffer + samples.length) / Config.NUM_AUDIO_SAMPLES_IN_FFT_SET];
        int numFFTSets = 0;

        for (int i = 0; i < samples.length; ++i) {
            // buffer the sample
            audioSampleBuffer[numAudioSamplesInBuffer] = samples[i];
            ++numAudioSamplesInBuffer;

            // if we have enough in our buffer to create an FFT set...
            if (numAudioSamplesInBuffer == Config.NUM_AUDIO_SAMPLES_IN_FFT_SET) {
                // create the FFT set
                fftSets[numFFTSets] = new FFTSet(audioSampleBuffer, Config.getSampleRate());
                ++numFFTSets;

                // reset the buffer
                numAudioSamplesInBuffer = 0;
            }
        }

        return fftSets;
    }
}

