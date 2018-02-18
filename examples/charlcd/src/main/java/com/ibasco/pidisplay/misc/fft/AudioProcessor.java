package com.ibasco.pidisplay.misc.fft;

public class AudioProcessor {
    // ===================================================================
    // Variables
    //
    // ===================================================================

    // current relative time unit
    // if we use a sample rate of 44100Hz, one time unit is 1/44100 seconds
    private long time = AudioSample.TIME_MIN_VALUE;


    // -------------------------------------------------------------------
    // intermediate processAudioData variables

    // if we are given an odd number of bytes to read, remove the last one and use it at the start of the next processAudioData
    private byte leftoverAudioDataByte;
    private boolean useLeftoverAudioDataByte = false;

    /**
     * Processes the given audio data into audio samples.<br />
     * <br />
     * Audio is assumed to have a sample size of 16 bits (2 bytes).
     * <br />
     * Data passed to this method over multiple executions is treated as one data set. Meaning, data from
     * previous executions of this function may be used during subsequent executions. This is intended for
     * uses such as streaming where small data sets can be passed to this function without having to worry
     * about audio samples being cut off. Only necessary data is kept in memory so excessive memory build
     * up will not occur with multiple executions of this function.
     *
     * @param data
     *         - Audio data.
     * @param dataOffset
     *         - Offset to start from in bytes.
     * @param dataLength
     *         - Number of bytes to read.
     * @param bigEndian
     *         - If the data is big-endian (true) or little-endian (false).
     *
     * @return Array of audio samples.
     */
    public AudioSample[] processAudioData(byte[] data, int dataOffset, int dataLength, boolean bigEndian) {
        // because we need two bytes for every sample, the data length must be kept even
        int evenDataLength = dataLength;

        if (useLeftoverAudioDataByte) // account for the offset of using the leftover byte
            ++evenDataLength;

        if (evenDataLength % 2 == 1)
            --evenDataLength;


        // create an array for the samples
        int numSamples = 0;
        AudioSample[] samples = new AudioSample[evenDataLength / 2];


        // iterate through the data bytes and create the samples
        for (int i = 0; i < evenDataLength; i += 2) {
            // select the bytes to use
            byte byte1;
            byte byte2;

            // check if we should use the leftover byte
            if (useLeftoverAudioDataByte) {
                // use the leftover byte and the first byte
                byte1 = leftoverAudioDataByte;
                byte2 = data[dataOffset];

                // don't use the leftover byte anymore
                useLeftoverAudioDataByte = false;

                // decrement the index to offset using the leftover byte
                --i;
            }

            // use the normal bytes based on the index
            else {
                byte1 = data[i + dataOffset];
                byte2 = data[i + dataOffset + 1];
            }


            // convert the two bytes to a single short
            // Remember! The data types are signed! (and no way to use unsigned... grumble grumble)

            // For the examples bellow I will be using the following bytes:
            // 00110010 10110101 (12981)

            // first we convert the signed bytes, to signed shorts
            // short1: 00000000 00110010 (50)
            // short2: 11111111 10110101 (-75)

            // data
            short short1;
            short short2;

            if (bigEndian) {
                short1 = byte1;
                short2 = byte2;
            } else {
                // if bytes are in little-endian, the most significant bit is last and we must reverse the order
                short2 = byte1;
                short1 = byte2;
            }

            // shift the most significant byte over 8 bits
            short1 <<= 8;
            //    00000000 00110010
            // << 8
            //    -----------------
            //    00110010 00000000

            // mask the least significant byte so all bits are 0 except the last 8. This is necessary because the value is negative.
            // Therefore, when converting from signed byte to signed short, the extra 8 bits that are added to the beginning are 1's in order to keep the value the same and negative.
            short2 &= 0b00000000_11111111;
            //   11111111 10110101
            // & 00000000 11111111
            //   -----------------
            //   00000000 10110101

            // finally, combine the two shorts with a bitwise OR
            short dataVal = (short) (short1 | short2);
            //   00110010 00000000
            // | 00000000 10110101
            //   -----------------
            //   00110010 10110101


            // create the raw audio sample
            samples[numSamples] = new AudioSample(time++, dataVal); // we assume 1 unit of time between each sample, so we just increment time
            ++numSamples;
        }

        // check if we have a leftover byte
        if (evenDataLength < dataLength) {
            leftoverAudioDataByte = data[dataOffset + dataLength - 1];
            useLeftoverAudioDataByte = true;
        }

        return samples;
    }
}
