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
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
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
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void expectIllegalArgumentExceptionWhenCapacityIsLessThan2()
            throws IOException {
        final ReadableByteChannel c = null;
        final HexDecoder d = null;
        final int p = 1 - (current().nextInt() >>> 1);
        final boolean r = current().nextBoolean();
        try (ReadableHexChannel rhc = new ReadableHexChannel(c, d, p, r)) {
        }
    }

    /**
     * Expects {@link ReadableHexChannel#isOpen()} throws an
     * {@code IllegalStateException} when {@link ReadableHexChannel#channel} is
     * {@code null}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void assertIsOpenThrowsIllegalStateExceptionWhenChannelisNull()
            throws IOException {
        final ReadableByteChannel c = null;
        final HexDecoder d = decoder();
        final int p = current().nextInt(128) << 1;
        final boolean r = current().nextBoolean();
        try (ReadableHexChannel rhc = new ReadableHexChannel(c, d, p, r)) {
            final boolean open = rhc.isOpen();
        }
    }

    /**
     * Assert {@link ReadableHexChannel#close()} silently returns even if
     * {@link ReadableHexChannel#channel} is {@code null}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void assertCloseReturnsEvenIfChannelIsNull() throws IOException {
        final ReadableByteChannel c = null;
        final HexDecoder d = decoder();
        final int p = current().nextInt(128) << 1;
        final boolean r = current().nextBoolean();
        try (ReadableHexChannel rhc = new ReadableHexChannel(c, d, p, r)) {
        }
    }

    @Test
    public void createWithNullChannel() throws IOException {
        final ReadableByteChannel c = null;
        final HexDecoder d = decoder();
        final int p = current().nextInt(128) << 1;
        final boolean r = current().nextBoolean();
        try (ReadableHexChannel whc = new ReadableHexChannel(c, d, p, r) {
            @Override
            public int read(final ByteBuffer buffer) throws IOException {
                if (channel == null) {
                    channel = Channels.newChannel(
                            new ByteArrayInputStream(new byte[128]));
                }
                return super.read(buffer);
            }
        }) {
            final ByteBuffer dst = ByteBuffer.allocate(current().nextInt(128));
            final int read = whc.read(dst);
        }
    }

    @Test
    public void createWithNullDecoder() throws IOException {
        final ReadableByteChannel c
                = Channels.newChannel(new ByteArrayInputStream(new byte[128]));
        final HexDecoder d = null;
        final int p = current().nextInt(128) << 1;
        final boolean r = current().nextBoolean();
        try (ReadableHexChannel whc = new ReadableHexChannel(c, d, p, r) {
            @Override
            public int read(final ByteBuffer buffer) throws IOException {
                if (decoder == null) {
                    decoder = decoder();
                }
                return super.read(buffer);
            }
        }) {
            final ByteBuffer dst = ByteBuffer.allocate(current().nextInt(128));
            final int read = whc.read(dst);
        }
    }

    @Test
    public void write() throws IOException {
        final ReadableByteChannel c = null;
        final HexDecoder d = null;
        final int p = current().nextInt(128) << 1;
        final boolean r = current().nextBoolean();
        try (ReadableHexChannel whc = new ReadableHexChannel(c, d, p, r) {
            @Override
            public int read(final ByteBuffer buffer) throws IOException {
                if (channel == null) {
                    channel = Channels.newChannel(
                            new ByteArrayInputStream(new byte[128]));
                }
                if (decoder == null) {
                    decoder = decoder();
                }
                return super.read(buffer);
            }
        }) {
            final ByteBuffer src = ByteBuffer.allocate(current().nextInt(128));
            final int read = whc.read(src);
        }
    }

    private transient final Logger logger = getLogger(getClass());

}
