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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import static java.lang.reflect.Proxy.newProxyInstance;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.Channels.newChannel;
import java.nio.channels.WritableByteChannel;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 * A class testing {@link WritableHexChannel}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class WritableHexChannelTest extends AbstractHexEncoderTest {

    static <T extends WritableByteChannel> T nonBlocking(
            final Class<T> type, final T channel) {
        final Method writeMethod; // <1>
        try {
            writeMethod = WritableByteChannel.class.getMethod(
                    "write", ByteBuffer.class);
        } catch (final NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
        final InvocationHandler handler = (proxy, method, args) -> {
            if (writeMethod.equals(method)) {
                final ByteBuffer src = (ByteBuffer) args[0]; // <1>
                final int limit = src.limit(); // <2>
                final int remaining = src.remaining();
                if (remaining > 1) { // <3>
                    src.limit(src.position() + current().nextInt(remaining));
                }
                final int written = channel.write(src); // <4>
                src.limit(limit); // <5>
                return written; // <6>
            }
            return method.invoke(channel, args); // <7>
        };
        final Object proxy = newProxyInstance( // <1>
                type.getClassLoader(), new Class<?>[]{type}, handler);
        return type.cast(proxy); // <2>
    }

    private static <T extends WritableByteChannel> T nonBlockingHelper(
            final Class<T> type, final WritableByteChannel channel) {

        return nonBlocking(type, type.cast(channel));
    }

    @SuppressWarnings("unchecked")
    static <T extends WritableByteChannel> T nonBlocking(final T channel) {

        return (T) nonBlockingHelper(channel.getClass(), channel);
    }

    /**
     * Tests {@link WritableHexChannelEx#isOpen()}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testIsOpen() throws IOException {
        accept(e -> assertThrows(
                NullPointerException.class,
                () -> new WritableHexChannel(null, e).isOpen()));
        {
            final WritableByteChannel whc = apply(e -> new WritableHexChannel(
                    newChannel(new ByteArrayOutputStream()), e));
            assertTrue(whc.isOpen());
            whc.close();
            assertFalse(whc.isOpen());
        }
    }

    /**
     * Tests {@link WritableHexChannelEx#close()}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testClose() throws IOException {
        {
            final WritableHexChannel whc
                    = apply(e -> new WritableHexChannel(null, e));
            whc.close();
            whc.close();
            whc.close();
        }
        {
            final WritableByteChannel whc = apply(e -> new WritableHexChannel(
                    newChannel(new ByteArrayOutputStream()), e));
            whc.close();
            whc.close();
            whc.close();
        }
    }

    /**
     * Tests {@link WritableHexChannel#write(java.nio.ByteBuffer)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWrite() throws IOException {
        final WritableByteChannel channel
                = newChannel(new ByteArrayOutputStream());
        try (WritableByteChannel whc = apply(
                e -> new WritableHexChannel(channel, e))) {
            final ByteBuffer src = allocate(current().nextInt(1, 128));
            final int written = whc.write(src);
        }
    }

    /**
     * Tests {@link WritableHexChannel#write(java.nio.ByteBuffer)} with a
     * non-blocking channel.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWriteNonBlocking() throws IOException {
        final WritableByteChannel channel = nonBlocking(
                WritableByteChannel.class,
                newChannel(new ByteArrayOutputStream()));
        try (WritableByteChannel whc = apply(
                e -> new WritableHexChannel(channel, e))) {
            final ByteBuffer src = allocate(current().nextInt(1, 128));
            final int written = whc.write(src);
        }
    }
}
