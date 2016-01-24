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
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.Channels.newChannel;
import java.nio.channels.ReadableByteChannel;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
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

    /**
     * Expects an {@code IllegalArgumentException} capacity is less than
     * {@code 2}.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void capacityIsLessThan2() {
        final ReadableByteChannel channel = null;
        final HexDecoder decoder = null;
        final int capacity = 1 - (current().nextInt() >>> 1);
        final boolean direct = current().nextBoolean();
        new ReadableHexChannel(channel, decoder, capacity, direct);
    }

    /**
     * Tests {@link ReadableHexChannel#isOpen()}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testIsOpen() throws IOException {
        {
            final ReadableByteChannel channel = null;
            final HexDecoder decoder = decoder();
            final int capacity = current().nextInt(2, 128);
            final boolean direct = current().nextBoolean();
            assertThrows(
                    NullPointerException.class,
                    () -> new ReadableHexChannel(
                            channel, decoder, capacity, direct)
                    .isOpen());
        }
        {
            final ReadableByteChannel channel
                    = newChannel(new ByteArrayInputStream(new byte[0]));
            final HexDecoder decoder = null;
            final int capacity = 2;
            final boolean direct = true;
            final ReadableByteChannel rhc = new ReadableHexChannel(
                    channel, decoder, capacity, direct);
            assertTrue(rhc.isOpen());
            rhc.close();
            assertFalse(rhc.isOpen());
        }
    }

    /**
     * Tests {@link ReadableHexChannel#close()}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testClose() throws IOException {
        {
            final ReadableByteChannel channel = null;
            final HexDecoder decoder = null;
            final int capacity = current().nextInt(2, 128);
            final boolean direct = current().nextBoolean();
            final ReadableByteChannel rhc = new ReadableHexChannel(
                    channel, decoder, capacity, direct);
            rhc.close();
            rhc.close();
            rhc.close();
        }
        {
            final ReadableByteChannel channel
                    = newChannel(new ByteArrayInputStream(new byte[0]));
            final HexDecoder decoder = null;
            final int capacity = current().nextInt(2, 128);
            final boolean direct = current().nextBoolean();
            final ReadableByteChannel rhc = new ReadableHexChannel(
                    channel, decoder, capacity, direct);
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
    @Test(invocationCount = 128)
    public void testRead() throws IOException {
        final ReadableByteChannel channel = newChannel(
                new ByteArrayInputStream(new byte[1024]));
        final HexDecoder decoder = decoder();
        final int capacity = current().nextInt(2, 128);
        final boolean direct = current().nextBoolean();
        try (ReadableHexChannel whc = new ReadableHexChannel(
                channel, decoder, capacity, direct)) {
            for (int i = 0; i < 128; i++) {
                final ByteBuffer dst = allocate(current().nextInt(128));
                final int read = whc.read(dst);
            }
        }
    }

    /**
     * Tests {@link ReadableHexChannel#read(java.nio.ByteBuffer)} with a
     * non-blocking channel.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(invocationCount = 128)
    public void testReadWithNonBlockingChannel() throws IOException {
        final ReadableByteChannel channel = mock(ReadableByteChannel.class);
        doAnswer(i -> {
            final ByteBuffer b = i.getArgumentAt(0, ByteBuffer.class);
            final int r = current().nextInt(b.remaining() + 1);
            b.position(b.position() + r);
            return r;
        }).when(channel).read(any(ByteBuffer.class));
        doNothing().when(channel).close();
        final HexDecoder decoder = decoder();
        final int capacity = current().nextInt(2, 128);
        final boolean direct = current().nextBoolean();
        try (ReadableHexChannel whc = new ReadableHexChannel(
                channel, decoder, capacity, direct)) {
            for (int i = 0; i < 128; i++) {
                final ByteBuffer dst = allocate(current().nextInt(128));
                final int read = whc.read(dst);
            }
        }
    }

}
