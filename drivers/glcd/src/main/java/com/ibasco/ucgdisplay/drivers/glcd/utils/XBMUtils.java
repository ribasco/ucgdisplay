package com.ibasco.ucgdisplay.drivers.glcd.utils;

import com.ibasco.ucgdisplay.drivers.glcd.exceptions.XBMDecodeException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.nio.ByteBuffer;

public class XBMUtils {

    public static XBMData decodeXbmFile(File file) throws XBMDecodeException {

        try {
            if (file == null)
                throw new IllegalArgumentException("File cannot be null");
            if (!file.exists())
                throw new FileNotFoundException("Could not find file: " + file.getName());

            StringBuilder data = new StringBuilder();

            BufferedReader br = new BufferedReader(new FileReader(file));//Files.newReader(file, Charset.defaultCharset());
            int skipLines = 3, curLine = 1;

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                if (curLine > skipLines) {
                    data.append(sCurrentLine).append("\n");
                }
                curLine++;
            }

            //Strip the last two remaining characters
            data = data.replace(data.length() - 3, data.length() - 1, "");

            String[] str = data.toString().trim().split(",");
            ByteBuffer bb = ByteBuffer.allocate(str.length);
            for (String d : str) {
                String tmp = d.replace("0x", "").trim();
                byte decodedByte = Hex.decodeHex(tmp)[0];
                bb.put(decodedByte);
            }
            bb.rewind();

            if (bb.hasRemaining()) {
                byte[] tmp = new byte[bb.remaining()];
                bb.get(tmp);
                return new XBMData(0, 0, tmp);
            }
            return null;
        } catch (IOException | DecoderException e) {
            throw new XBMDecodeException("Problem encountered while trying to decode file", e);
        }
    }
}
