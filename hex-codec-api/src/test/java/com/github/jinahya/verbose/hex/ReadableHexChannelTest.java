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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.Channels.newChannel;
import java.nio.channels.ReadableByteChannel;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;
import static com.github.jinahya.verbose.util.BcUtils.nonBlocking;
import static com.github.jinahya.verbose.util.BcUtils.nonBlocking;

/**
 * A class testing {@link WritableHexChannel}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ReadableHexChannelTest extends AbstractHexDecoderTest {

    /**
     * Tests {@link ReadableHexChannelEx#isOpen()}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testIsOpen() throws IOException {
        try (final ReadableByteChannel rhc = apply(
                d -> new ReadableHexChannel(() -> newChannel(
                new ByteArrayInputStream(new byte[0])), () -> d))) {
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
                    = new ReadableHexChannel(() -> null, () -> null);
            rhc.close();
            rhc.close();
            rhc.close();
        }
        {
            final ReadableByteChannel rhc = new ReadableHexChannel(
                    () -> newChannel(new ByteArrayInputStream(new byte[0])),
                    () -> null);
            rhc.close();
            rhc.close();
            rhc.close();
        }
        {
            final ReadableByteChannel rhc = apply(
                    d -> new ReadableHexChannel(() -> null, () -> d));
            rhc.close();
            rhc.close();
            rhc.close();
        }
        {
            final ReadableByteChannel rhc = apply(d -> new ReadableHexChannel(
                    () -> newChannel(new ByteArrayInputStream(new byte[0])),
                    () -> d));
            rhc.close();
            rhc.close();
            rhc.close();
        }
    }

    /**
     * Tests {@link ReadableHexChannel#read(java.nio.ByteBuffer)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testRead() throws IOException {
        try (ReadableByteChannel whc = apply(d -> new ReadableHexChannel(
                () -> {
                    final byte[] bytes = new byte[current().nextInt(64) << 1];
                    return newChannel(new ByteArrayInputStream(bytes));
                },
                () -> d))) {
            for (final ByteBuffer dst = allocate(current().nextInt(1, 128));
                 whc.read(dst) != -1; dst.clear());
        }
    }

    /**
     * Tests {@link ReadableHexChannel#read(java.nio.ByteBuffer)} with an input
     * source of odd number of bytes.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(expectedExceptions = EOFException.class)
    public void testReadOddBytes() throws IOException {
        try (ReadableByteChannel whc = apply(d -> new ReadableHexChannel(
                () -> {
                    final int length = current().nextInt(128) | 0b1;
                    final byte[] bytes = new byte[length];
                    return newChannel(new ByteArrayInputStream(bytes));
                }, () -> d))) {
            for (final ByteBuffer dst = allocate(current().nextInt(1, 128));
                 whc.read(dst) != -1; dst.clear());
        }
    }

    /**
     * Tests {@link ReadableHexChannel#read(java.nio.ByteBuffer)} with a
     * non-blocking channel.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testReadNonBlocking() throws IOException {
        try (ReadableByteChannel whc = apply(d -> new ReadableHexChannel(
                () -> {
                    final int length = current().nextInt(64) << 1;
                    final byte[] bytes = new byte[length];
                    final InputStream stream = new ByteArrayInputStream(bytes);
                    return nonBlocking(
                            ReadableByteChannel.class, newChannel(stream));
                },
                () -> d))) {
            for (final ByteBuffer dst = allocate(current().nextInt(1, 128));
                 whc.read(dst) != -1; dst.clear());
        }
    }

    /**
     * Tests {@link ReadableHexChannel#read(java.nio.ByteBuffer)} with an input
     * source of odd number of bytes in non-blocking mode.
     *
     * @throws IOException if an error occurs.
     */
    @Test(expectedExceptions = EOFException.class)
    public void testReadNonBlockingOddBytes() throws IOException {
        try (ReadableByteChannel whc = apply(d -> new ReadableHexChannel(
                () -> {
                    final int length = current().nextInt(128) | 0b1;
                    final byte[] bytes = new byte[length];
                    final InputStream stream = new ByteArrayInputStream(bytes);
                    return nonBlocking(
                            ReadableByteChannel.class, newChannel(stream));
                },
                () -> d))) {
            for (final ByteBuffer dst = allocate(current().nextInt(1, 128));
                 whc.read(dst) != -1; dst.clear());
        }
    }
}
