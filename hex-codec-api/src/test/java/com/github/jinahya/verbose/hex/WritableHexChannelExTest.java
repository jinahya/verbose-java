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

import static com.github.jinahya.verbose.hex.WritableHexChannelTest.nonBlocking;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
 * A class testing {@link WritableHexChannelEx}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class WritableHexChannelExTest extends AbstractHexEncoderTest {

    /**
     * Expects an {@code IllegalArgumentException} when capacity is less than
     * {@code 2}.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void capacityIsLessThan2() {
        final WritableByteChannel channel
                = newChannel(new ByteArrayOutputStream());
        final int capacity = 1 - (current().nextInt() >>> 1);
        accept(e -> new WritableHexChannelEx<>(channel, e, capacity));
    }

    /**
     * Tests {@link WritableHexChannelEx#isOpen()}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testIsOpen() throws IOException {
        {
            final WritableByteChannel channel = null;
            final int capacity = current().nextInt(2, 128);
            accept(e -> assertThrows(
                    NullPointerException.class,
                    () -> new WritableHexChannelEx<>(channel, e, capacity)
                    .isOpen()));
        }
        {
            final WritableByteChannel channel
                    = newChannel(new ByteArrayOutputStream());
            final int capacity = current().nextInt(2, 128);
            final WritableByteChannel whc = apply(
                    e -> new WritableHexChannelEx<>(channel, e, capacity));
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
            final WritableByteChannel whc = apply(
                    e -> new WritableHexChannelEx<>(null, e, 2));
            whc.close();
            whc.close();
            whc.close();
        }
        {
            WritableByteChannel channel
                    = newChannel(new ByteArrayOutputStream());
            final WritableByteChannel whc = apply(
                    e -> new WritableHexChannelEx<>(channel, e, 2));
            whc.close();
            whc.close();
            whc.close();
        }
    }

    /**
     * Tests {@link WritableHexChannelEx#write(java.nio.ByteBuffer)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWrite() throws IOException {
        final WritableByteChannel channel
                = newChannel(new ByteArrayOutputStream());
        final int capacity = current().nextInt(2, 128);
        try (WritableByteChannel whc = apply(
                e -> new WritableHexChannelEx<>(channel, e, capacity))) {
            final ByteBuffer src = allocate(current().nextInt(128));
            final int written = whc.write(src);
        }
    }

    /**
     * Tests {@link WritableHexChannelEx#write(java.nio.ByteBuffer)} with a
     * non-blocking channel.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWriteWithNonBlockingChannel() throws IOException {
        final WritableByteChannel channel = nonBlocking(
                WritableByteChannel.class,
                newChannel(new ByteArrayOutputStream()));
        final int capacity = current().nextInt(2, 128);
        try (WritableByteChannel whc = apply(
                e -> new WritableHexChannelEx<>(channel, e, capacity))) {
            final ByteBuffer src = allocate(current().nextInt(128));
            final int written = whc.write(src);
        }
    }
}
