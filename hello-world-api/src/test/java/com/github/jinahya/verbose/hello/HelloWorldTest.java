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
     * Tests whether {@link HelloWorld#BYTES} is equals to actual number of
     * bytes.
     */
    @Test
    public static void BYTES() {
        assertEquals(HelloWorld.BYTES,
                     "hello, world".getBytes(US_ASCII).length);
    }

    private HelloWorld mock() {
        return (a, o) -> {
        };
    }

    /**
     * Tests {@link HelloWorld#put(java.nio.ByteBuffer)}.
     */
    @Test
    public void put() {
        assertThrows(NullPointerException.class, () -> mock().put(null)); // <1>
        assertThrows( // <2>
                IllegalArgumentException.class,
                () -> mock().put(ByteBuffer.allocate(HelloWorld.BYTES - 1)));
        { // <3>
            final ByteBuffer buffer = ByteBuffer.allocate(HelloWorld.BYTES);
            mock().put(buffer);
        }
        { // <4>
            final ByteBuffer buffer
                    = ByteBuffer.allocateDirect(HelloWorld.BYTES);
            mock().put(buffer);
        }
    }

    /**
     * Tests {@link HelloWorld#write(java.io.OutputStream)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void writeWithStream() throws IOException {
        assertThrows(NullPointerException.class, // <1>
                     () -> mock().write((OutputStream) null));
        final ByteArrayOutputStream stream = new ByteArrayOutputStream(); // <2>
        mock().write(stream);
    }

    /**
     * Tests {@link HelloWorld#write(java.nio.channels.WritableByteChannel)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void writeWithChannel() throws IOException {
        assertThrows(NullPointerException.class, // <1>
                     () -> mock().write((WritableByteChannel) null));
        final WritableByteChannel channel // <2>
                = Channels.newChannel(new ByteArrayOutputStream());
        mock().write(channel);
    }
}
