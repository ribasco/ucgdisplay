package com.ibasco.pidisplay.examples.lcd;

import com.ibasco.pidisplay.misc.fft.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class SoundPlaybackTest {
    private static final Logger log = LoggerFactory.getLogger(SoundPlaybackTest.class);

    private void testPlay(String filename) {
        try {
            File file = new File(filename);
            showAudioProps(file);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioInputStream din;
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);

            log.info("Setting Equalizer Settings");
            // DecodedMpegAudioInputStream properties
            if (din instanceof javazoom.spi.PropertiesContainer) {
                Map properties = ((javazoom.spi.PropertiesContainer) din).properties();
                float[] equalizer = (float[]) properties.get("mp3.equalizer");
                equalizer[0] = 0.5f;
                equalizer[31] = 0.25f;
            }
            Config.setSampleRate((int) baseFormat.getSampleRate());
            // create the FFT processor
            FFTProcessor fftProcessor = new FFTProcessor();
            // create the audio processor
            AudioProcessor audioProcessor = new AudioProcessor();
            //FFTVisualizer2 fftVisualizer2 = new FFTVisualizer2();
            // Play now.
            rawplay(decodedFormat, din, fftProcessor, audioProcessor, null);
            in.close();
        } catch (Exception e) {
            //Handle exception.
        }
    }

    private void showAudioProps(File file) throws IOException, UnsupportedAudioFileException {
        AudioFileFormat baseFileFormat;
        AudioFormat baseFormat;
        baseFileFormat = AudioSystem.getAudioFileFormat(file);
        baseFormat = baseFileFormat.getFormat();
        String author = null;
        Integer bitrate = null;
        Long duration = null;

        // TAudioFileFormat properties
        if (baseFileFormat instanceof TAudioFileFormat) {
            Map properties = baseFileFormat.properties();
            author = (String) properties.get("author");
            duration = (Long) properties.get("duration");
            /*key = "mp3.id3tag.v2";
            InputStream tag = (InputStream) properties.get(key);*/
        }
        // TAudioFormat properties
        if (baseFormat instanceof TAudioFormat) {
            Map properties = baseFormat.properties();
            bitrate = (Integer) properties.get("bitrate");
        }
        log.info("Author: {}, Bitrate: {}, Duration: {}", author, bitrate, duration);
    }

    private void rawplay(AudioFormat targetFormat, AudioInputStream din, FFTProcessor fftProcessor, AudioProcessor audioProcessor, FFTVisualizer2 fftVisualizer2) throws IOException, LineUnavailableException {
        byte[] buffer = new byte[4096];
        SourceDataLine line = getLine(targetFormat);
        line.addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                log.info("Line Event: {} (Pos: {})", event.toString(), event.getFramePosition());
            }
        });
        // Start
        line.start();
        int nBytesRead = 0, nBytesWritten = 0;

        while (nBytesRead != -1) {
            nBytesRead = din.read(buffer, 0, buffer.length);
            if (nBytesRead != -1) {
                nBytesWritten = line.write(buffer, 0, nBytesRead);
                FFTSet[] fftSets = fftProcessor.processAudioSamples(audioProcessor.processAudioData(buffer, 0, nBytesRead, targetFormat.isBigEndian()));
                //fftVisualizer2.displayFFTSets(fftSets);
                //log.info("Set Length: {}, Samples: {}, Frequency: {}, Amplitude: {}", fftSets.length, fftSets[0].fftSamples.length, fftSets[0].fftSamples[0].frequency, fftSets[0].fftSamples[0].amplitude);
                for (int f = 0; f < fftSets.length; f++) {
                    for (int i = 0; i < fftSets[f].fftSamples.length; i++) {
                        FFTSample sample = fftSets[f].fftSamples[i];
                        log.info("SET: {} > Sample {}, Frequency: {}, Amplitude: {}", f, i, sample.frequency, sample.amplitude);
                    }
                    //log.info("\n");
                }

            }
        }
        // Stop
        line.drain();
        line.stop();
        line.close();
        din.close();
    }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
        SourceDataLine res;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }

    public void run() {
        String soundPath = getClass().getClassLoader().getResource("Music1.mp3").getPath();
        log.info("Playing MP3 Sound: {}", soundPath);
        testPlay(soundPath);
    }

    public static void main(String[] args) throws Exception {
        new SoundPlaybackTest().run();
    }
}
