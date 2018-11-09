/*
 * Copyright (C) 2018 Universal Character/Graphics display library
 *
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
 */
package com.ibasco.ucgdisplay.common.utils;

import org.scijava.nativelib.NativeLoader;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;

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
            NativeLoader.setJniExtractor(new UCGDJniExtractor());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void loadLibrary(String libName) throws IOException {
        NativeLoader.loadLibrary(libName);
    }
}
