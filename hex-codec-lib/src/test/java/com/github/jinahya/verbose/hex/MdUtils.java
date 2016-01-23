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
package com.github.jinahya.verbose.hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.READ;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Utilities for testing hex codec.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class MdUtils {

    private static byte[] digest1(final InputStream input,
                                  final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest // <1>
                = MessageDigest.getInstance(algorithm);
        final byte[] buffer = new byte[4096];
        for (int read; (read = input.read(buffer)) != -1;) {
            digest.update(buffer, 0, read); // <2>
        }
        return digest.digest(); // <3>
    }

    private static byte[] digest2(InputStream input, final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest = MessageDigest.getInstance(algorithm);
        input = new DigestInputStream(input, digest); // <1>
        final byte[] buffer = new byte[4096];
        while (input.read(buffer) != -1); // <2>
        return digest.digest(); // <3>
    }

    /**
     * Computes a hash from given input stream using specified algorithm name.
     * This method delegates the whole operation to either
     * {@link #digest1(java.io.InputStream, java.lang.String)} or
     * {@link #digest2(java.io.InputStream, java.lang.String)}.
     *
     * @param input the input stream
     * @param algorithm the algorithm name
     * @return the computed hash value.
     * @throws NoSuchAlgorithmException if {@code algorithm} is unknown.
     * @throws IOException if an I/O error occurs.
     */
    static byte[] digest(final InputStream input, final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        switch (current().nextInt(2)) {
            case 0:
                return digest1(input, algorithm);
            default:
                return digest2(input, algorithm);
        }
    }

    /**
     * Computes a hash of given file using specified algorithm name.
     *
     * @param file the file
     * @param algorithm the algorithm name
     * @return the computed hash value
     * @throws IOException if an I/O error occurs
     * @throws NoSuchAlgorithmException if {@code algorithm} is unknown.
     */
    static byte[] digest(final File file, final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        try (InputStream stream = new FileInputStream(file)) {
            return digest(stream, algorithm);
        }
    }

    /**
     * Computes a hash from all bytes read from given channel using specified
     * algorithm name.
     *
     * @param channel the channel
     * @param algorithm the algorithm name
     * @return a computed hash value
     * @throws NoSuchAlgorithmException if {@code algorithm} is unknown
     * @throws IOException if an I/O error occurs.
     */
    static byte[] digest(final ReadableByteChannel channel,
                         final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest = MessageDigest.getInstance(algorithm);
        final ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (channel.read(buffer) != -1) { // <1>
            buffer.flip();
            digest.update(buffer); // <2>
            buffer.compact();
        }
        return digest.digest();
    }

    /**
     * Computes a hash from given path using specified algorithm name.
     *
     * @param path the path
     * @param algorithm the algorithm name
     * @return a computed hash value
     * @throws IOException if an I/O error occurs.
     * @throws NoSuchAlgorithmException if {@code algorithm} is unknown
     */
    static byte[] digest(final Path path, final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        try (ReadableByteChannel channel = open(path, READ)) {
            return digest(channel, algorithm);
        }
    }

    private MdUtils() {
        super();
    }

}
