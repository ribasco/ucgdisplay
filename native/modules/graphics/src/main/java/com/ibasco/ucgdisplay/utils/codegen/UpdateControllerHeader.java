/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: UpdateControllerHeader.java
 *
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 - 2019 Universal Character/Graphics display library
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
package com.ibasco.ucgdisplay.utils.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Simple utility class for generating the latest controller definitions from U8g2 codebuild.c file
 *
 * @author Rafael Ibasco
 */
public class UpdateControllerHeader {
    private static final Logger log = LoggerFactory.getLogger(UpdateControllerHeader.class);

    private static final String START_TAG = "display_controller_list_start";

    private static final String END_TAG = "display_controller_list_end";

    private static final String PROJECT_DIR = System.getProperty("user.dir");

    private static final String DEFAULT_CODEBUILD_URL = "https://raw.githubusercontent.com/olikraus/u8g2/master/tools/codebuild/codebuild.c";

    private static final String DEFAULT_OUTPUT_DIR = PROJECT_DIR + File.separator + "src/main/cpp/utils";

    private static final String DEFAULT_OUTPUT_FILE = "controllers.h";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm:ss a");

    private static String getDefault(int index, String[] args, String defaultArg) {
        if (args.length > 0 && (index <= (args.length - 1))) {
            if (!isBlank(args[index]))
                return args[index];
        }
        return defaultArg;
    }

    public static void main(String[] args) throws Exception {
        String outputDir = getDefault(0, args, DEFAULT_OUTPUT_DIR);
        String outputFile = getDefault(1, args, DEFAULT_OUTPUT_FILE);
        String codeBuildUrl = getDefault(2, args, DEFAULT_CODEBUILD_URL);
        Path outputFilePath = Paths.get(outputDir + File.separator + outputFile);

        log.info("[UPDATE-CONTROLLER-H] Output directory: {}", outputDir);
        log.info("[UPDATE-CONTROLLER-H] Output file: {}", outputFile);
        log.info("[UPDATE-CONTROLLER-H] Output file path: {}", outputFilePath);
        log.info("[UPDATE-CONTROLLER-H] Codebuild URL: {}", codeBuildUrl);

        try (BufferedInputStream in = new BufferedInputStream(new URL(codeBuildUrl).openStream())) {
            log.info("[UPDATE-CONTROLLER-H] Successfully downloaded codebuild.c from master (Bytes: " + in.available() + ")");
            StringBuilder controllerCode = new StringBuilder();
            boolean startCollecting = false;

            //Start scanning for the controllers array section
            try (Scanner scanner = new Scanner(in)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (isBlank(line))
                        continue;

                    if (line.contains(START_TAG)) {
                        startCollecting = true;
                        continue;
                    } else if (line.contains(END_TAG)) {
                        startCollecting = false;
                        continue;
                    }

                    if (startCollecting) {
                        controllerCode.append(line);
                        controllerCode.append("\n");
                    }
                }
            }

            if (controllerCode.length() > 0) {
                log.info("[UPDATE-CONTROLLER-H] Exporting controllers.h to '{}'", outputFilePath);

                if (Files.deleteIfExists(outputFilePath)) {
                    log.info("[UPDATE-CONTROLLER-H] Removing existing file: {}", outputFilePath);
                }
                File file = Files.createFile(outputFilePath).toFile();
                StringBuilder comment = new StringBuilder();
                comment.append("/*\n");
                comment.append("* THIS IS AN AUTO-GENERATED CODE!! PLEASE DO NOT MODIFY (");
                comment.append(formatter.format(ZonedDateTime.now()));
                comment.append(")\n");
                comment.append("*/\n");

                try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(file))) {
                    bWriter.append(comment);
                    bWriter.append(controllerCode);
                    bWriter.flush();
                    log.info("controller.h updated (path: {})", outputFilePath);
                }
            }
        }
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
