/*
 * Copyright 2016 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jinahya.verbose.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * I/O utilities for {@code java.io} package.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class IoUtils1 {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    // -------------------------------------------------------------------------
    private static long copy1(final InputStream input,
                              final OutputStream output, final byte[] buffer)
            throws IOException {
        // @todo validate arguments!
        long count = 0L;
        for (int length; (length = input.read(buffer)) != -1; count += length) { // <1>
            output.write(buffer, 0, length); // <2>
        }
        return count;
    }

    static long copy(final InputStream input, final OutputStream output,
                     final byte[] buffer)
            throws IOException {
        switch (current().nextInt(1)) {
            default:
                return copy1(input, output, buffer);
        }
    }

    private static void copy1(final File source, final File target,
                              final byte[] buffer)
            throws IOException {
        // @todo: validate arguments!
        try (InputStream input = new FileInputStream(source);
             OutputStream output = new FileOutputStream(target)) {
            copy1(input, output, buffer);
            output.flush(); // <1>
        }
    }

    static void copy(final File source, final File target, final byte[] buffer)
            throws IOException {
        // @todo: validate arguments!
        switch (current().nextInt(1)) {
            default:
                copy1(source, target, buffer);
                break;
        }
    }

    // -------------------------------------------------------------------------
    private IoUtils1() {
        super();
    }
}
