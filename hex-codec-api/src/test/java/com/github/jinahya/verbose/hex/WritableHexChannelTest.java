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
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
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
public class WritableHexChannelTest extends AbstractHexEncoderTest {

    /**
     * Expects an {@code IllegalArgumentException} when capacity is less than
     * {@code 2}.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void capacityIsLessThan2() {
        final WritableByteChannel channel = null;
        final HexEncoder encoder = null;
        final int capacity = 1 - (current().nextInt() >>> 1);
        final boolean direct = current().nextBoolean();
        new WritableHexChannel(channel, encoder, capacity, direct);
    }

    /**
     * Tests {@link WritableHexChannel#isOpen()}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testIsOpen() throws IOException {
        assertThrows(
                NullPointerException.class,
                () -> new WritableHexChannel(
                        null, encoder(), 2, current().nextBoolean()).isOpen());
        final WritableHexChannel whc = new WritableHexChannel(
                Channels.newChannel(new ByteArrayOutputStream()), encoder(), 2,
                current().nextBoolean());
        assertTrue(whc.isOpen());
        whc.close();
        assertFalse(whc.isOpen());
    }

    /**
     * Tests {@link WritableHexChannel#close()}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testClose() throws IOException {
        {
            final WritableHexChannel whc = new WritableHexChannel(
                    null, encoder(), 2, current().nextBoolean());
            whc.close();
            whc.close();
            whc.close();
        }
        {
            final WritableHexChannel whc = new WritableHexChannel(
                    Channels.newChannel(new ByteArrayOutputStream()), encoder(),
                    2, current().nextBoolean());
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
    @Test(invocationCount = 128)
    public void testWrite() throws IOException {
        final WritableByteChannel channel
                = Channels.newChannel(new ByteArrayOutputStream());
        final HexEncoder encoder = encoder();
        final int capacity = current().nextInt(2, 129);
        final boolean direct = current().nextBoolean();
        try (WritableHexChannel whc
                = new WritableHexChannel(channel, encoder, capacity, direct)) {
            for (int i = 0; i < 128; i++) {
                final ByteBuffer src
                        = ByteBuffer.allocate(current().nextInt(128));
                final int written = whc.write(src);
            }
        }
    }

    /**
     * Tests {@link WritableHexChannel#write(java.nio.ByteBuffer)} with a
     * non-blocking channel.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(invocationCount = 128)
    public void testWriteWithNonBlockingChannel() throws IOException {
        final WritableByteChannel channel = mock(WritableByteChannel.class);
        doNothing().when(channel).close();
        doAnswer(invocation -> {
            final ByteBuffer b = invocation.getArgumentAt(0, ByteBuffer.class);
            final int w = current().nextInt(b.remaining() + 1);
            b.position(b.position() + w);
            return w;
        }).when(channel).write(any(ByteBuffer.class));
        final HexEncoder encoder = encoder();
        final int capacity = current().nextInt(2, 129);
        final boolean direct = current().nextBoolean();
        try (WritableHexChannel whc
                = new WritableHexChannel(channel, encoder, capacity, direct)) {
            for (int i = 0; i < 128; i++) {
                final ByteBuffer src
                        = ByteBuffer.allocate(current().nextInt(128));
                final int written = whc.write(src);
            }
        }
    }

    private transient final Logger logger = getLogger(getClass());

}
