package com.ibasco.pidisplay.core.system;

import org.scijava.nativelib.NativeLoader;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Utility for loading native libraries.
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings("WeakerAccess")
public class NativeLibraryLoader {
    public static final Logger log = getLogger(NativeLibraryLoader.class);

    static {
        try {
            NativeLoader.setJniExtractor(new PidispJniExtractor());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void loadLibrary(String libName) throws IOException {
        NativeLoader.loadLibrary(libName);
    }
}
