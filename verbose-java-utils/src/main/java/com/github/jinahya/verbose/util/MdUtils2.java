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

import java.io.IOException;
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.READ;
import java.security.MessageDigest;
import static java.security.MessageDigest.getInstance;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * Utilities for {@link MessageDigest} with {@code java.nio.channels} package.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class MdUtils2 {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    // -------------------------------------------------------------------------
    private static byte[] digest1(final String algorithm,
                                  final ReadableByteChannel channel,
                                  final ByteBuffer buffer)
            throws NoSuchAlgorithmException, IOException {
        // @todo: validate arguments!
        final MessageDigest digest = getInstance(algorithm);
        while (channel.read(buffer) != -1) {
            buffer.flip();
            digest.update(buffer); // <1>
            buffer.clear();
        }
        return digest.digest();
    }

    static byte[] digest(final String algorithm,
                         final ReadableByteChannel channel,
                         final ByteBuffer buffer)
            throws NoSuchAlgorithmException, IOException {
        // @todo: validate arguments!
        switch (current().nextInt(1)) {
            default:
                return digest1(algorithm, channel, buffer);
        }
    }

    private static byte[] digest1(final String algorithm, final Path path,
                                  final ByteBuffer buffer)
            throws IOException, NoSuchAlgorithmException {
        // @todo: validate arguments!
        try (ReadableByteChannel channel = open(path, READ)) {
            return digest(algorithm, channel, buffer); // <1>
        }
    }

    static byte[] digest(final String algorithm, final Path path,
                         final ByteBuffer buffer)
            throws IOException, NoSuchAlgorithmException {
        // @todo: validate arguments!
        switch (current().nextInt(1)) {
            default:
                return digest1(algorithm, path, buffer);
        }
    }

    // -------------------------------------------------------------------------
    private MdUtils2() {
        super();
    }
}
