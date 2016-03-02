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
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.channels.Channels.newChannel;
import java.nio.channels.WritableByteChannel;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertThrows;
import org.testng.annotations.Test;

/**
 * A class for testing {@code HelloWorld}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldTest {

    /**
     * Tests whether {@link HelloWorld#BYTES} is equals to actual number of
     * bytes.
     */
    @Test
    public static void BYTES() {
        assertEquals(HelloWorld.BYTES,
                     "hello, world".getBytes(US_ASCII).length);
    }

    /**
     * Returns an implementation of {@link HelloWorld} whose
     * {@link HelloWorld#set(byte[], int)} does nothing.
     *
     * @return an implementation of {@link HelloWorld}
     */
    private HelloWorld mock() {
        return (a, o) -> { // <1>
        };
    }

    /**
     * Tests {@link HelloWorld#put(java.nio.ByteBuffer)}.
     */
    @Test
    public void putOnBuffer() {
        assertThrows(NullPointerException.class, () -> mock().put(null)); // <1>
        assertThrows(BufferOverflowException.class, // <2>
                     () -> mock().put(allocate(
                             current().nextInt(HelloWorld.BYTES))));
        { // <3>
            final ByteBuffer expected = allocate(HelloWorld.BYTES);
            final ByteBuffer actual = mock().put(expected);
            assertSame(actual, expected);
        }
        { // <4>
            final ByteBuffer expected = allocateDirect(HelloWorld.BYTES);
            final ByteBuffer actual = mock().put(expected);
            assertSame(actual, expected);
        }
    }

    /**
     * Tests {@link HelloWorld#write(java.io.OutputStream)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void writeToStream() throws IOException {
        assertThrows(NullPointerException.class, // <1>
                     () -> mock().write((OutputStream) null));
        final OutputStream expected = new ByteArrayOutputStream();
        final OutputStream actual = mock().write(expected); // <2>
        assertSame(actual, expected);
    }

    /**
     * Tests {@link HelloWorld#write(java.nio.channels.WritableByteChannel)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void writeToChannel() throws IOException {
        assertThrows(NullPointerException.class, // <1>
                     () -> mock().write((WritableByteChannel) null));
        final WritableByteChannel expected
                = newChannel(new ByteArrayOutputStream());
        final WritableByteChannel actual = mock().write(expected); // <2>
        assertSame(actual, expected);
    }
}
