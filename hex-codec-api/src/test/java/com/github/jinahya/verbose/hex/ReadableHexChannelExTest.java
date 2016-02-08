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

import static com.github.jinahya.verbose.hex.ReadableHexChannelTest.nonBlocking;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
 * A class testing {@link WritableHexChannelEx}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ReadableHexChannelExTest extends AbstractHexDecoderTest {

    /**
     * Expects an {@code IllegalArgumentException} when capacity is less than
     * {@code 2}.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void capacityIsLessThan2() {
        final ReadableByteChannel channel
                = newChannel(new ByteArrayInputStream(new byte[0]));
        final int capacity = 1 - (current().nextInt() >>> 1);
        accept(d -> new ReadableHexChannelEx<>(channel, d, capacity));
    }

    /**
     * Tests {@link ReadableHexChannelEx#isOpen()}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testIsOpen() throws IOException {
        {
            final int capacity = current().nextInt(2, 128);
            accept(d -> assertThrows(
                    NullPointerException.class,
                    () -> new ReadableHexChannelEx<>(null, d, capacity)
                    .isOpen()));
        }
        {
            final ReadableByteChannel channel
                    = newChannel(new ByteArrayInputStream(new byte[0]));
            final ReadableByteChannel rhc = apply(
                    d -> new ReadableHexChannelEx<>(channel, d, 2));
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
            final ReadableByteChannel channel = null;

            final ReadableByteChannel rhc = apply(
                    d -> new ReadableHexChannelEx<>(channel, d, 2));
            rhc.close();
            rhc.close();
            rhc.close();
        }
        {
            final ReadableByteChannel channel
                    = newChannel(new ByteArrayInputStream(new byte[0]));
            final ReadableByteChannel rhc
                    = apply(d -> new ReadableHexChannelEx<>(channel, d, 2));
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
    @Test
    public void testRead() throws IOException {
        final ReadableByteChannel channel = newChannel(
                new ByteArrayInputStream(new byte[16384]));
        final int capacity = current().nextInt(2, 128);
        try (ReadableByteChannel rhc = apply(
                d -> new ReadableHexChannelEx<>(channel, d, capacity))) {
            final ByteBuffer dst = allocate(current().nextInt(128));
            final int read = rhc.read(dst);
        }
    }

    /**
     * Tests {@link ReadableHexChannelEx#read(java.nio.ByteBuffer)} with a
     * non-blocking channel.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testReadWithNonBlockingChannel() throws IOException {
        final ReadableByteChannel channel = nonBlocking(
                ReadableByteChannel.class,
                newChannel(new ByteArrayInputStream(new byte[16384])));
        final int capacity = current().nextInt(2, 128);
        try (ReadableByteChannel rhc = apply(d -> nonBlocking(
                ReadableByteChannel.class,
                new ReadableHexChannelEx<>(channel, d, capacity)))) {
            final ByteBuffer dst = allocate(current().nextInt(128));
            final int read = rhc.read(dst);
        }
    }
}
