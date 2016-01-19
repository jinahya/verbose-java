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
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
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
        assertThrows(
                NullPointerException.class,
                () -> new ReadableHexChannel(null, null, 2, false).isOpen());
        final ReadableByteChannel rhc = new ReadableHexChannel(
                Channels.newChannel(new ByteArrayInputStream(new byte[0])), null, 2, true);
        assertTrue(rhc.isOpen());
        rhc.close();
        assertFalse(rhc.isOpen());
    }

    /**
     * Tests {@link ReadableHexChannel#close()}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testClose() throws IOException {
        {
            final ReadableByteChannel rhc = new ReadableHexChannel(
                    null, decoder(), 2, current().nextBoolean());
            rhc.close();
            rhc.close();
            rhc.close();
        }
        {
            final ReadableByteChannel rhc = new ReadableHexChannel(
                    Channels.newChannel(new ByteArrayInputStream(new byte[0])),
                    decoder(), 2, current().nextBoolean());
            rhc.close();
            rhc.close();
            rhc.close();
        }
    }

    /**
     * Test {@link ReadableHexChannel#read(java.nio.ByteBuffer)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(invocationCount = 128)
    public void testRead() throws IOException {
        final ReadableByteChannel channel = null;
        final HexDecoder decoder = null;
        final int capacity = current().nextInt(2, 129);
        final boolean direct = current().nextBoolean();
        try (ReadableHexChannel whc = new ReadableHexChannel(
                channel, decoder, capacity, direct) {
            @Override
            public int read(final ByteBuffer dst) throws IOException {
                if (channel == null) {
                    channel = Channels.newChannel(
                            new ByteArrayInputStream(new byte[1024]));
                }
                if (decoder == null) {
                    decoder = decoder();
                }
                return super.read(dst);
            }
        }) {
            for (int i = 0; i < 128; i++) {
                final ByteBuffer dst
                        = ByteBuffer.allocate(current().nextInt(128));
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
        doAnswer(invocation -> {
            final ByteBuffer b = invocation.getArgumentAt(0, ByteBuffer.class);
            final int r = current().nextInt(b.remaining() + 1);
            b.position(b.position() + r);
            return r;
        }).when(channel).read(any(ByteBuffer.class));
        doNothing().when(channel).close();
        final HexDecoder decoder = decoder();
        final int capacity = current().nextInt(2, 129);
        final boolean direct = current().nextBoolean();
        try (ReadableHexChannel whc = new ReadableHexChannel(
                channel, decoder, capacity, direct)) {
            for (int i = 0; i < 128; i++) {
                final ByteBuffer dst
                        = ByteBuffer.allocate(current().nextInt(128));
                final int read = whc.read(dst);
            }
        }
    }

    private transient final Logger logger = getLogger(getClass());

}
