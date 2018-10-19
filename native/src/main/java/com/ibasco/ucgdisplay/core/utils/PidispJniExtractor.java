/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native Library
 * Filename: PidispJniExtractor.java
 *
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 Universal Character/Graphics display library
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * =========================END==================================
 */
package com.ibasco.ucgdisplay.core.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
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

    private String getOSFamily() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "windows";
        } else if (SystemUtils.IS_OS_LINUX) {
            return "linux";
        } else if (SystemUtils.IS_OS_MAC) {
            return "mac";
        }
        log.warn("Unhandled OS name = {}", SystemUtils.OS_NAME);
        return SystemUtils.OS_NAME;
    }

    private File findLibrary(String libPath, String libName) throws IOException {
        //Try to extract from the java.library.path first
        String lPath = System.getProperty("java.library.path");

        String mappedName = System.mapLibraryName(libName);

        if (!StringUtils.isBlank(lPath)) {
            String fullPath = lPath + File.separator + mappedName;
            log.debug("[JNI-EXTRACT #1]: Searching in path: {}", fullPath);
            File libFile = new File(fullPath);
            if (libFile.exists() && libFile.canRead()) {
                return libFile;
            }
        }

        //Search by OS/ARCH
        String osName = getOSFamily();
        String osArch = System.getProperty("os.arch");
        String osVer = System.getProperty("os.version");

        log.debug("[JNI-EXTRACT] Os Name = {}, Os Arch = {}, Os version = {}", osName, osArch, osVer);
        String osPath = String.format("lib/%s%s%s/", osName, File.separator, osArch).toLowerCase();
        osPath = FilenameUtils.separatorsToUnix(osPath);

        log.debug("[JNI-EXTRACT #2] Searching in path: {}", osPath);
        File file = super.extractJni(osPath, libName.trim());
        if (file != null)
            return file;
        else
            log.warn("Could not locate library '{}' from '{}'", mappedName, osPath);
        return super.extractJni("", libName);
    }
}
