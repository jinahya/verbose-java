/*
 * Copyright 2014 Jin Kwon.
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
package com.github.jinahya.verbose.hello;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * An interface for generating {@code "hello, world"} bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@FunctionalInterface
public interface HelloWorld {

    /**
     * Number of required bytes for representing {@code "hello, world"} in
     * {@code US-ASCII} charset.
     */
    int BYTES = 12;

    /**
     * Sets {@value #BYTES} bytes representing {@code "hello, world"} in
     * {@code US-ASCII} character set on given byte array starting at specified
     * offset.
     *
     * @param array the byte array
     * @param offset the start offset in {@code array}
     *
     * @throws NullPointerException if {@code array} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code offset} is negative or
     * {@code offset} + {@link #BYTES}({@value #BYTES}) is greater than
     * {@code array.length}.
     */
    void set(byte[] array, int offset);

    /**
     * Puts {@value #BYTES} bytes representing {@code "hello, world"} in
     * {@code US-ASCII} charset on given byte buffer. Upon return, the buffer's
     * position will be incremented by {@value HelloWorld#BYTES}.
     *
     * @param buffer the byte buffer
     * @throws NullPointerException if {@code buffer} is {@code null}.
     * @throws IllegalArgumentException if {@code buffer.remaining} is less than
     * {@value #BYTES}
     *
     * @see #set(byte[], int)
     * @see ByteBuffer#put(byte[])
     */
    default void put(final ByteBuffer buffer) {
        if (buffer == null) { // <1>
            throw new NullPointerException("null buffer");
        }
        if (buffer.remaining() < BYTES) { // <2>
            throw new IllegalArgumentException(
                    "buffer.remaining(" + buffer.remaining() + ") < " + BYTES);
        }
        if (buffer.hasArray()) { // <3>
            set(buffer.array(), buffer.arrayOffset() + buffer.position());
            buffer.position(buffer.position() + BYTES);
            return;
        }
        final byte[] array = new byte[BYTES]; // <4>
        final int offset = 0;
        set(array, offset);
        buffer.put(array);
    }

    /**
     * Writes {@value #BYTES} bytes representing {@code "hello, world"} on given
     * output stream.
     *
     * @param stream the output stream
     * @throws IOException if an I/O error occurs.
     */
    default void write(final OutputStream stream) throws IOException {
        if (stream == null) { // <1>
            throw new NullPointerException("null stream");
        }
        final byte[] array = new byte[BYTES]; // <2>
        final int offset = 0;
        set(array, offset);
        stream.write(array);
    }

    /**
     * Writes {@value #BYTES} bytes representing {@code "hello, world"} on given
     * byte channel.
     *
     * @param channel the byte channel
     * @throws IOException if an I/O error occurs.
     *
     * @see #put(java.nio.ByteBuffer)
     * @see WritableByteChannel#write(java.nio.ByteBuffer)
     */
    default void write(final WritableByteChannel channel) throws IOException {
        if (channel == null) { // <1>
            throw new NullPointerException("null channel");
        }
        final ByteBuffer buffer = ByteBuffer.allocate(BYTES); // <2>
        put(buffer);
        for (buffer.flip(); buffer.hasRemaining();) { // <3>
            System.out.println("writing...");
            channel.write(buffer);
        }
    }
}
