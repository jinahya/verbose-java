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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Utilities for testing hex codec.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class HexCodecTests {

    /**
     * Fills some bytes to given file.
     *
     * @param file the file be filled.
     * @return given file
     * @throws IOException if an I/O error occurs.
     */
    static File fill(final File file) throws IOException {
        try (OutputStream stream = new FileOutputStream(file)) {
            final byte[] array = new byte[current().nextInt(128)];
            final int count = current().nextInt(128);
            for (int i = 0; i < count; i++) {
                current().nextBytes(array);
                stream.write(array);
            }
            stream.flush();
        }
        return file;
    }

    /**
     * Fills some bytes to given path.
     *
     * @param path the path to be filled.
     * @return given path
     * @throws IOException if an I/O error occurs.
     */
    static Path fill(final Path path) throws IOException {
        try (FileChannel channel
                = FileChannel.open(path, StandardOpenOption.WRITE)) {
            final byte[] array = new byte[current().nextInt(128)];
            final ByteBuffer buffer = ByteBuffer.wrap(array);
            final int count = current().nextInt(128);
            for (int i = 0; i < count; i++) {
                current().nextBytes(array);
                while (buffer.hasRemaining()) { // <1>
                    channel.write(buffer);
                }
                buffer.compact(); // <2>
            }
            channel.force(false); // <3>
        }
        return path;
    }

    /**
     * Copies all available bytes from given input stream to specified output
     * stream.
     *
     * @param input the input stream
     * @param output the output stream
     * @return the number of copied bytes.
     * @throws IOException if an I/O error occurs.
     */
    static long copy(final InputStream input, final OutputStream output)
            throws IOException {
        long count = 0L;
        final byte[] buffer = new byte[4096];
        for (int read; (read = input.read(buffer)) != -1; count += read) {
            output.write(buffer, 0, read);
        }
        return count;
    }

    /**
     * Computes a hash from all available bytes in given input stream using
     * specified algorithm.
     *
     * @param input the input stream
     * @param algorithm the algorithm name
     * @return digested value.
     * @throws NoSuchAlgorithmException if {@code algorithm} is unknown.
     * @throws IOException if an I/O error occurs.
     */
    static byte[] digest1(final InputStream input, final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest // <1>
                = MessageDigest.getInstance(algorithm);
        final byte[] buffer = new byte[4096];
        for (int read; (read = input.read(buffer)) != -1;) { // <2>
            digest.update(buffer, 0, read);
        }
        return digest.digest(); // <3>
    }

    /**
     * Computes a hash of all available bytes in given input stream using
     * specified algorithm.
     *
     * @param input the input stream
     * @param algorithm the algorithm name
     * @return the computed hash value.
     * @throws NoSuchAlgorithmException if {@code algorithm} is unknown
     * @throws IOException if an I/O error occurs.
     */
    static byte[] digest2(InputStream input, final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest = MessageDigest.getInstance(algorithm);
        input = new DigestInputStream(input, digest); // <1>
        final byte[] buffer = new byte[4096];
        while (input.read(buffer) != -1); // <2>
        return digest.digest(); // <3>
    }

    /**
     * Computes a hash of all available bytes in given input stream using
     * specified algorithm name. This method forwards given arguments to either
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
        return current().nextBoolean()
               ? digest1(input, algorithm) : digest2(input, algorithm);
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
     * Copies all available bytes from given input channel to specified output
     * channel.
     *
     * @param readable the input channel
     * @param writable the output channel
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs.
     */
    static long copy1(final ReadableByteChannel readable,
                      final WritableByteChannel writable)
            throws IOException {
        long count = 0L;
        final ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (readable.read(buffer) != -1) {
            buffer.flip();
            count += writable.write(buffer); // <1>
            buffer.compact(); // <2>
        }
        for (buffer.flip(); buffer.hasRemaining();) { // <3>
            count += writable.write(buffer);
        }
        return count;
    }

    /**
     * Copies all available bytes from given input channel to specified output
     * channel.
     *
     * @param readable the input channel
     * @param writable the output channel
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs.
     */
    static long copy2(final ReadableByteChannel readable,
                      final WritableByteChannel writable)
            throws IOException {
        long count = 0L;
        final ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (readable.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) { // <1>
                count += writable.write(buffer);
            }
            buffer.clear(); // <2>
        }
        return count;
    }

    /**
     * Copies all available bytes from given input channel to specified output
     * channel.
     *
     * @param readable the input channel
     * @param writable the output channel
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs.
     */
    static long copy(final ReadableByteChannel readable,
                     final WritableByteChannel writable)
            throws IOException {

        return current().nextBoolean()
               ? copy1(readable, writable) : copy2(readable, writable);
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
        while (channel.read(buffer) != -1) {
            buffer.flip(); // <1>
            digest.update(buffer); // <2>
            buffer.clear(); // <3>
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
        try (ReadableByteChannel channel
                = FileChannel.open(path, StandardOpenOption.READ)) {
            return digest(channel, algorithm);
        }
    }

    private HexCodecTests() {
        super();
    }

}
