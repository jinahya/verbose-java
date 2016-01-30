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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import static java.lang.reflect.Proxy.newProxyInstance;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.Channels.newChannel;
import java.nio.channels.ReadableByteChannel;
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
public class ReadableHexChannelTest extends AbstractHexDecoderTest {

    static <T extends ReadableByteChannel> T nonBlocking(
            final Class<T> type, final T channel) {
        final Method METHOD;
        try {
            METHOD = ReadableByteChannel.class.getMethod(
                    "read", ByteBuffer.class);
        } catch (final NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
        final InvocationHandler handler = (proxy, method, args) -> {
            if (METHOD.equals(method)) {
                final ByteBuffer dst = (ByteBuffer) args[0];
                final int limit = dst.limit();
                if (dst.hasRemaining()) {
                    dst.limit(limit - current().nextInt(dst.remaining()));
                }
                final int read = channel.read(dst);
                dst.limit(limit);
                return read;
            }
            return method.invoke(channel, args);
        };
        final Object proxy = newProxyInstance(
                type.getClassLoader(), new Class<?>[]{type}, handler);
        return type.cast(proxy);
    }

    private static <T extends ReadableByteChannel> T nonBlockingHelper(
            final Class<T> type, final ReadableByteChannel channel) {
        return nonBlocking(type, type.cast(channel));
    }

    @SuppressWarnings("unchecked")
    static <T extends ReadableByteChannel> T nonBlocking(final T channel) {
        return (T) nonBlockingHelper(channel.getClass(), channel);
    }

    /**
     * Tests {@link ReadableHexChannelEx#isOpen()}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testIsOpen() throws IOException {
        accept(d -> assertThrows(
                NullPointerException.class,
                () -> new ReadableHexChannel<>(null, d).isOpen()));
        try (final ReadableByteChannel rhc = apply(
                d -> new ReadableHexChannel<>(newChannel(
                        new ByteArrayInputStream(new byte[0])), d))) {
            assertTrue(rhc.isOpen());
            rhc.close();
            assertFalse(rhc.isOpen());
        }
    }

    /**
     * Tests {@link ReadableHexChannelEx#close()}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testClose() throws IOException {
        {
            final ReadableByteChannel rhc
                    = new ReadableHexChannel<>(null, null);
            rhc.close();
            rhc.close();
            rhc.close();
        }
        {
            final ReadableByteChannel channel
                    = newChannel(new ByteArrayInputStream(new byte[0]));
            final HexDecoder decoder = null;
            final ReadableByteChannel rhc
                    = new ReadableHexChannel<>(channel, decoder);
            rhc.close();
            rhc.close();
            rhc.close();
        }
    }

    /**
     * Tests {@link ReadableHexChannelEx#read(java.nio.ByteBuffer)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(invocationCount = 128)
    public void testRead() throws IOException {
        final ReadableByteChannel channel = newChannel(
                new ByteArrayInputStream(new byte[16384]));
        try (ReadableHexChannel<ReadableByteChannel> whc
                = apply(d -> new ReadableHexChannel<>(channel, d))) {
            for (int i = 0; i < 128; i++) {
                final ByteBuffer dst = allocate(current().nextInt(128));
                final int read = whc.read(dst);
            }
        }
    }

    /**
     * Tests {@link ReadableHexChannelEx#read(java.nio.ByteBuffer)} with a
     * non-blocking channel.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(invocationCount = 128)
    public void testReadWithNonBlockingChannel() throws IOException {
        final ReadableByteChannel channel
                = newChannel(new ByteArrayInputStream(new byte[16384]));
        try (ReadableByteChannel whc
                = apply(d -> nonBlocking(ReadableByteChannel.class, new ReadableHexChannel<>(channel, d)))) {
            for (int i = 0; i < 128; i++) {
                final ByteBuffer dst = allocate(current().nextInt(128));
                final int read = whc.read(dst);
            }
        }
    }

}
