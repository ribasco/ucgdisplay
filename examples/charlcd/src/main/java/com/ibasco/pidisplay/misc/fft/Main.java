package com.ibasco.pidisplay.misc.fft;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

// TODO: memory leak
public class Main {
    public static void main(String[] args) throws Exception {
        // create the audio processor
        AudioProcessor audioProcessor = new AudioProcessor();

        // create the FFT processor
        FFTProcessor fftProcessor = new FFTProcessor();

        // create the MorseCodeDetector
        MorseCodeDetector morseCodeDetector = new MorseCodeDetector();

        // create the visualizer
        FFTVisualizer fftVisualizer = new FFTVisualizer();
        FFTVisualizer2 fftVisualizer2 = new FFTVisualizer2();

        streamFromFile("silent.wav", audioProcessor, fftProcessor, morseCodeDetector, fftVisualizer, fftVisualizer2);
        //streamFromMic(audioProcessor, fftProcessor, morseCodeDetector, fftVisualizer, fftVisualizer2);
    }

    private static void streamFromMic(AudioProcessor audioProcessor, FFTProcessor fftProcessor, MorseCodeDetector morseCodeDetector, FFTVisualizer fftVisualizer, FFTVisualizer2 fftVisualizer2) throws Exception {
        // create our format
        AudioFormat audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, // encoding
                88200.0f,                        // sample rate. NOTE: Changing this may throw off several algorithms since we assume 1 unit of time between each sample
                16,                              // sample size in bits. NOTE: If you change this, you will have to change the amplitude data type.
                1,                               // channels
                2,                               // frame size
                88200.0f,                        // frame rate
                true);                           // big-endian


        Config.setSampleRate((int) audioFormat.getSampleRate());

        // create the data line info
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        if (!AudioSystem.isLineSupported(info))
            throw new Exception("Line is not supported");

        // get the data line
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(audioFormat);

        // start listening
        line.start();

        byte[] audioByteBuffer = new byte[line.getBufferSize()];
        while (true) {
            // read bytes from the line
            int numBytesRead = line.read(audioByteBuffer, 0, 180);

            //if (line.available() > line.getBufferSize() / 2)
            //	System.err.println("Getting behind! " + line.available());

            if (numBytesRead > 0) {
                FFTSet[] fftSets = fftProcessor.processAudioSamples(audioProcessor.processAudioData(audioByteBuffer, 0, numBytesRead, audioFormat.isBigEndian()));

                //morseCodeDetector.processFFTSets(fftSets);

                //if (fftSets.length > 0)
                //	fftVisualizer.displayFFTSet(fftSets[0]);

                fftVisualizer2.displayFFTSets(fftSets);
            }

            Thread.sleep(0, 100);
        }
    }

    private static void streamFromFile(String filename, AudioProcessor audioProcessor, FFTProcessor fftProcessor, MorseCodeDetector morseCodeDetector, FFTVisualizer fftVisualizer, FFTVisualizer2 fftVisualizer2) throws Exception {
        // get test file
        File testFile = new File(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + filename);

        // load file as audio stream
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(testFile);

        // get the audio format from the steam
        AudioFormat audioFormat = audioInputStream.getFormat();

        System.out.println("--- Audio Format ---");
        System.out.println("Encoding   : " + audioFormat.getEncoding());
        System.out.println("Sample Rate: " + audioFormat.getSampleRate());
        System.out.println("Sample Size: " + audioFormat.getSampleSizeInBits());
        System.out.println("Channels   : " + audioFormat.getChannels());
        System.out.println("Frame Size : " + audioFormat.getFrameSize());
        System.out.println("Frame Rate : " + audioFormat.getFrameRate());
        System.out.println("--------------------");

        Config.setSampleRate((int) audioFormat.getSampleRate());

        // get additional info from the audio format
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

        // get the data line from the info
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(audioFormat);

        // open the audio stream into the clip
        int numBytesRead = 0;
        int totalNumBytesRead = 0;
        byte[] audioByteBuffer = new byte[sourceDataLine.getBufferSize()];

        sourceDataLine.start();

        do {
            // read the bytes into the buffer
            try {
                numBytesRead = audioInputStream.read(audioByteBuffer, 0, audioByteBuffer.length);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            // process the data we have read so far
            if (numBytesRead > 0) {
                FFTSet[] fftSets = fftProcessor.processAudioSamples(audioProcessor.processAudioData(audioByteBuffer, 0, numBytesRead, audioFormat.isBigEndian()));

                //morseCodeDetector.processFFTSets(fftSets);

                //if (fftSets.length > 0)
                //	fftVisualizer.displayFFTSet(fftSets[0]);

                fftVisualizer2.displayFFTSets(fftSets);

                totalNumBytesRead += numBytesRead;
            }

            Thread.sleep(1);
        }
        while (numBytesRead > -1);

        sourceDataLine.close();

        System.out.println("Done Reading");
        System.out.println("Read " + totalNumBytesRead + " bytes total");
    }
}
