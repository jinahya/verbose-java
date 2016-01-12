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
package com.github.jinahya.verbose.hello;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import org.testng.annotations.Test;

/**
 * A class for testing {@code HelloWorld}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldTest {

    /**
     * Actual value.
     */
    private static final String VALUE = "hello, world";

    /**
     * Tests whether {@link HelloWorld#BYTES} is equals to actual number of
     * bytes.
     */
    @Test
    public static void bytes() {
        assertEquals(HelloWorld.BYTES, VALUE.getBytes(US_ASCII).length);
    }

    /**
     * Returns an instance of {@link HelloWorld}.
     *
     * @return an instance of {@link HelloWorld}.
     */
    private static HelloWorld impl() {
        final byte[] src = VALUE.getBytes(US_ASCII);
        return (a, o) -> System.arraycopy(src, 0, a, o, src.length);
    }

    /**
     * Tests {@link HelloWorld#set(byte[], int)}.
     */
    @Test
    public void set() {
        assertThrows(NullPointerException.class, () -> impl().set(null, 0));
        assertThrows(IndexOutOfBoundsException.class,
                     () -> impl().set(new byte[HelloWorld.BYTES], -1));
        assertThrows(IndexOutOfBoundsException.class,
                     () -> impl().set(new byte[HelloWorld.BYTES], 1));
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = 0;
        impl().set(array, offset);
        assertEquals(array, VALUE.getBytes(US_ASCII));
    }

    /**
     * Tests {@link HelloWorld#put(java.nio.ByteBuffer)}.
     */
    @Test
    public void put() {
        assertThrows(NullPointerException.class, () -> impl().put(null));
        assertThrows(
                IllegalArgumentException.class,
                () -> impl().put(ByteBuffer.allocate(HelloWorld.BYTES - 1)));
        final ByteBuffer buffer = ByteBuffer.allocate(HelloWorld.BYTES);
        impl().put(buffer);
        buffer.flip();
        assertEquals(buffer, ByteBuffer.wrap(VALUE.getBytes(US_ASCII)));
    }

    /**
     * Tests {@link HelloWorld#put(java.nio.ByteBuffer)} with an array wrapping
     * buffer.
     */
    @Test
    public void putWithArrayWrappingBuffer() {
        final byte[] array = new byte[HelloWorld.BYTES];
        impl().put(ByteBuffer.wrap(array));
        assertEquals(array, VALUE.getBytes(US_ASCII));
    }

    /**
     * Tests {@link HelloWorld#write(java.io.OutputStream)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void writeWithStream() throws IOException {
        assertThrows(NullPointerException.class,
                     () -> impl().write((OutputStream) null));
        final ByteArrayOutputStream stream
                = new ByteArrayOutputStream(HelloWorld.BYTES);
        impl().write(stream);
        assertEquals(stream.toByteArray(), VALUE.getBytes(US_ASCII));
    }

    /**
     * Tests {@link HelloWorld#write(java.nio.channels.WritableByteChannel)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void writeWithChannel() throws IOException {
        assertThrows(NullPointerException.class,
                     () -> impl().write((WritableByteChannel) null));
        final ByteArrayOutputStream stream
                = new ByteArrayOutputStream(HelloWorld.BYTES);
        final WritableByteChannel channel = Channels.newChannel(stream);
        impl().write(channel);
        assertEquals(stream.toByteArray(), VALUE.getBytes(US_ASCII));
    }
}
