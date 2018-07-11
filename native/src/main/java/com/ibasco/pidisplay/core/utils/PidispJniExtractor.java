package com.ibasco.pidisplay.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.scijava.nativelib.DefaultJniExtractor;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class PidispJniExtractor extends DefaultJniExtractor {

    public static final Logger log = getLogger(PidispJniExtractor.class);

    private static File cachedFile;

    PidispJniExtractor() throws IOException {
        super();
    }

    @Override
    public File extractJni(String libPath, String libname) throws IOException {
        if (cachedFile == null) {
            cachedFile = findLibrary(libPath, libname);
        }
        return cachedFile;
    }

    private File findLibrary(String libPath, String libName) throws IOException {
        //Try to extract from the java.library.path first
        String lPath = System.getProperty("java.library.path");

        if (!StringUtils.isBlank(lPath)) {
            String mappedName = System.mapLibraryName(libName);
            String fullPath = lPath + File.separator + mappedName;
            log.debug("[JNI-EXTRACT #1]: Searching in path: {}", fullPath);
            File libFile = new File(fullPath);
            if (libFile.exists() && libFile.canRead()) {
                return libFile;
            }
        }

        //Search by OS/ARCH
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osPath = String.format("lib/%s%s%s/", osName, File.separator, osArch).toLowerCase();
        log.info("[JNI-EXTRACT #2] Searching in path: {}", osPath);
        File file = super.extractJni(osPath, libName);
        if (file != null)
            return file;
        return super.extractJni("", libName);
    }
}
