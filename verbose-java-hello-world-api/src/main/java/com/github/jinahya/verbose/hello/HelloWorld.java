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
 * An interface generating {@code hello, world}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface HelloWorld {

    /**
     * Number of bytes for representing {@code hello, world} in
     * {@code US-ASCII}.
     */
    static final int BYTES = 12; // "hello, world".getBytes(US_ASCII).length;

    /**
     * Sets {@code hello, world} on given byte array starting at specified
     * offset.
     *
     * @param array the byte array
     * @param offset the starting offset
     */
    void set(byte[] array, int offset);

    /**
     * Put bytes representing {@code hello, world} on given byte buffer.
     *
     * @param buffer the byte buffer
     * @return given byte buffer
     */
    default ByteBuffer put(final ByteBuffer buffer) {
        final byte[] array = new byte[BYTES];
        final int offset = 0;
        set(array, offset);
        buffer.put(array);
        return buffer;
    }
}
