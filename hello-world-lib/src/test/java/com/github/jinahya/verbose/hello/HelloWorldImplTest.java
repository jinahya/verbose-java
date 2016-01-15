/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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
import static java.nio.charset.StandardCharsets.US_ASCII;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import org.testng.annotations.Test;

/**
 * A test class testing {@link HelloWorldImpl}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldImplTest {

    /**
     * Expects {@link HelloWorldImpl#set(byte[], int)} throws a
     * {@code NullPointerException} when {@code array} is {@code null}.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void expectNullPointerExceptionWhenArrayIsNull() {
        final byte[] array = null;
        final int offset = 0;
        new HelloWorldImpl().set(array, offset);
    }

    /**
     * Expects {@link HelloWorldImpl#set(byte[], int)} throws an
     * {@code IndexOutOfBoundsException} when {@code offset} is negative.
     */
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void expectIndexOutOfBoundsWhenOffsetIsNegative() {
        final byte[] array = new byte[0];
        final int offset = -1;
        new HelloWorldImpl().set(array, offset);
    }

    /**
     * Expects {@link HelloWorldImpl#set(byte[], int)} throws an
     * {@code IndexOutOfBoundsException} when
     * {@code array.length + offset > HelloWorld.BYTES}.
     */
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void expectIndexOutOfBoundsExceptionWhenCapacityIsNotEnough() {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = 1;
        new HelloWorldImpl().set(array, offset);
    }

    /**
     * Expects {@code hello, world} is set on {@code array} starting at
     * {@code offset}.
     */
    @Test
    public void expectHelloWorldBytesSetOnArrayStartingAtOffset() {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = 0;
        new HelloWorldImpl().set(array, offset);
        assertEquals(array[offset + 0x0], 'h');
        assertEquals(array[offset + 0x1], 'e');
        assertEquals(array[offset + 0x2], 'l');
        assertEquals(array[offset + 0x3], 'l');
        assertEquals(array[offset + 0x4], 'o');
        assertEquals(array[offset + 0x5], ',');
        assertEquals(array[offset + 0x6], ' ');
        assertEquals(array[offset + 0x7], 'w');
        assertEquals(array[offset + 0x8], 'o');
        assertEquals(array[offset + 0x9], 'r');
        assertEquals(array[offset + 0xA], 'l');
        assertEquals(array[offset + 0xB], 'd');
    }

    /**
     * Tests {@link HelloWorld#put(java.nio.ByteBuffer)} works as expected.
     */
    @Test
    public void put() {
        assertThrows(NullPointerException.class,
                     () -> new HelloWorldImpl().put(null));
        {
            final ByteBuffer actual = ByteBuffer.allocate(HelloWorld.BYTES);
            new HelloWorldImpl().put(actual);
            actual.flip();
            final ByteBuffer expected
                    = ByteBuffer.wrap("hello, world".getBytes(US_ASCII));
            assertEquals(actual, expected);
        }
        {
            final ByteBuffer actual
                    = ByteBuffer.allocateDirect(HelloWorld.BYTES);
            new HelloWorldImpl().put(actual);
            actual.flip();
            final ByteBuffer expected
                    = ByteBuffer.wrap("hello, world".getBytes(US_ASCII));
            assertEquals(actual, expected);
        }
    }

    /**
     * Tests {@link HelloWorldImpl#write(java.io.OutputStream)} works as
     * expected.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void writeWithStream() throws IOException {
        assertThrows(NullPointerException.class,
                     () -> new HelloWorldImpl().write((OutputStream) null));
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new HelloWorldImpl().write(baos);
        final byte[] actual = baos.toByteArray();
        final byte[] expected = "hello, world".getBytes(US_ASCII);
        assertEquals(actual, expected);
    }

    /**
     * Tests {@link HelloWorldImpl#write(java.nio.channels.WritableByteChannel)}
     * works as expected.
     *
     * @throws IOException
     */
    @Test
    public void writeWithChannel() throws IOException {
        assertThrows(NullPointerException.class,
                     () -> new HelloWorldImpl().write((OutputStream) null));
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new HelloWorldImpl().write(Channels.newChannel(baos));
        final byte[] actual = baos.toByteArray();
        final byte[] expected = "hello, world".getBytes(US_ASCII);
        assertEquals(actual, expected);
    }

    private transient final Logger logger = getLogger(getClass());
}
