package com.ibasco.pidisplay.core.util;

import com.google.common.io.Files;
import com.ibasco.pidisplay.core.exceptions.XBMDecodeException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class XBMUtils {
    public static byte[] decodeXbmFile(File file) throws XBMDecodeException {
        try {
            if (file == null)
                throw new IllegalArgumentException("File cannot be null");
            if (!file.exists())
                throw new FileNotFoundException("Could not find file: " + file.getName());

            StringBuilder data = new StringBuilder();
            BufferedReader br = Files.newReader(file, Charset.defaultCharset());
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
            byte[] tmp = new byte[bb.remaining()];
            bb.get(tmp);

            return tmp;
        } catch (IOException | DecoderException e) {
            throw new XBMDecodeException("Problem encountered while trying to decode file", e);
        }
    }
}
