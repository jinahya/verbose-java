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
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Utilities for I/O.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class IoUtils {

    /**
     * Copies all bytes from given input stream to specified output stream using
     * specified buffer.
     *
     * @param input the input stream from which bytes are copied
     * @param output the output stream to which bytes are copied
     * @param buffer a byte array to be used as a buffer
     * @return the number of byte copied
     * @throws IOException if an I/O error occurs.
     */
    public static long copy(final InputStream input, final OutputStream output,
                            final byte[] buffer)
            throws IOException {
        // @todo: validate arguments!
        switch (current().nextInt(1)) {
            default:
                return IoUtils1.copy(input, output, buffer);
        }
    }

    /**
     * Copies all bytes from given source file to target file using specified
     * buffer.
     *
     * @param source the source file
     * @param target the target file
     * @param buffer the buffer
     * @throws IOException if an I/O error occurs.
     */
    public static void copy(final File source, final File target,
                            final byte[] buffer)
            throws IOException {
        switch (current().nextInt(1)) {
            default:
                IoUtils1.copy(source, target, buffer);
        }
    }

    /**
     * Copies all bytes from given readable byte channel to specified writable
     * byte channel using given buffer.
     *
     * @param readable the readable byte channel
     * @param writable the writable byte channel
     * @param buffer the buffer
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs.
     */
    public static long copy(final ReadableByteChannel readable,
                            final WritableByteChannel writable,
                            final ByteBuffer buffer)
            throws IOException {
        switch (current().nextInt(1)) {
            default:
                return IoUtils2.copy(readable, writable, buffer);
        }
    }

    /**
     * Copies all bytes from given source path to specified target path using
     * given buffer.
     *
     * @param source the source path
     * @param target the target path
     * @param buffer the buffer
     * @throws IOException if an I/O error occurs.
     */
    public static void copy(final Path source, final Path target,
                            final ByteBuffer buffer)
            throws IOException {
        switch (current().nextInt(1)) {
            default:
                IoUtils2.copy(source, target, buffer);
                break;
        }
    }

    private IoUtils() {
        super();
    }
}
