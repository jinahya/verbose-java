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
import java.io.IOException;
import java.io.InputStream;
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class MdUtils {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    // -------------------------------------------------------------------------
    /**
     * Computes a hash value for all bytes from given input stream using
     * specified algorithm name.
     *
     * @param stream the input stream
     * @param algorithm the algorithm
     * @param buffer a buffer to use
     * @return message digest value
     * @throws NoSuchAlgorithmException if {@code algorithm} is unknown
     * @throws IOException if an I/O error occurs.
     */
    public static byte[] digest(final String algorithm,
                                final InputStream stream, final byte[] buffer)
            throws NoSuchAlgorithmException, IOException {
        // @todo: validate arguments!
        switch (current().nextInt(1)) {
            default:
                return MdUtils1.digest(algorithm, stream, buffer);
        }
    }

    public static byte[] digest(final String algorithm, final File file,
                                final byte[] buffer)
            throws IOException, NoSuchAlgorithmException {
        // @todo: validate arguments!
        switch (current().nextInt(1)) {
            default:
                return MdUtils1.digest(algorithm, file, buffer);
        }
    }

    public static byte[] digest(final String algorithm,
                                final ReadableByteChannel channel,
                                final ByteBuffer buffer)
            throws NoSuchAlgorithmException, IOException {
        // @todo validate arguments!
        switch (current().nextInt(1)) {
            default:
                return MdUtils2.digest(algorithm, channel, buffer);
        }
    }

    public static byte[] digest(final String algorithm, final Path path,
                                final ByteBuffer buffer)
            throws IOException, NoSuchAlgorithmException {
        // @todo: validate arguments!
        switch (current().nextInt(1)) {
            default:
                return MdUtils2.digest(algorithm, path, buffer);
        }
    }

    // -------------------------------------------------------------------------
    private MdUtils() {
        super();
    }
}
