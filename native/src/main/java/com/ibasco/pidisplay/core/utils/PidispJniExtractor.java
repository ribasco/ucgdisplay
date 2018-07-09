package com.ibasco.pidisplay.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.scijava.nativelib.DefaultJniExtractor;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class PidispJniExtractor extends DefaultJniExtractor {

    public static final Logger log = getLogger(PidispJniExtractor.class);

    public PidispJniExtractor() throws IOException {
        super();
    }

    public PidispJniExtractor(Class<?> libraryJarClass, String tmplib) throws IOException {
        super(libraryJarClass, tmplib);
    }

    @Override
    public File extractJni(String libPath, String libname) throws IOException {
        //Try to extract from the java.library.path first
        String lPath = System.getProperty("java.library.path");

        if (!StringUtils.isBlank(lPath)) {
            String mappedName = System.mapLibraryName(libname);
            String fullPath = lPath + File.separator + mappedName;
            File libFile = new File(fullPath);
            if (libFile.exists() && libFile.canRead()) {
                log.debug("JNI-EXTRACT: Found library: {}", fullPath);
                return libFile;
            }
        }
        return super.extractJni(libPath, libname);
    }
}
