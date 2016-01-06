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

import java.nio.ByteBuffer;

/**
 * An interface generating {@code "hello, world"} bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface HelloWorld {

    /**
     * Number of required bytes for representing {@code "hello, world"} in
     * {@code US-ASCII}.
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
     * @throws IllegalArgumentException if {@code offset} is negative or
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
     * @return given byte buffer
     *
     * @see #set(byte[], int)
     * @see ByteBuffer#put(byte[])
     */
    default ByteBuffer put(final ByteBuffer buffer) {
        if (buffer == null) {
            throw new NullPointerException("null buffer");
        }
        if (buffer.remaining() < BYTES) {
            throw new IllegalArgumentException(
                    "buffer.remaining(" + buffer.remaining() + ") < " + BYTES);
        }
        final byte[] array = new byte[BYTES];
        final int offset = 0;
        set(array, offset);
        if (buffer.hasArray()) {
            System.arraycopy(array, 0, buffer.array(),
                             buffer.arrayOffset() + buffer.position(),
                             array.length);
            return (ByteBuffer) buffer.position(buffer.position() + BYTES);
        }
        return buffer.put(array);
    }
}
