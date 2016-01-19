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
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 * A class testing {@link WritableHexChannel}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class WritableHexChannelTest extends AbstractHexEncoderTest {

    /**
     * Expects an {@code IllegalArgumentException} capacity is less than
     * {@code 2}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void expectIllegalArgumentExceptionWhenCapacityIsLessThan2()
            throws IOException {
        final WritableByteChannel w = null;
        final HexEncoder e = null;
        final int c = 1 - (current().nextInt() >>> 1);
        final boolean d = current().nextBoolean();
        try (WritableHexChannel whc = new WritableHexChannel(w, e, c, d)) {
        }
    }

    /**
     * Expects {@link WritableHexChannel#isOpen()} throws an
     * {@code IllegalStateException} when {@link WritableHexChannel#channel} is
     * {@code null}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void assertIsOpenThrowsIllegalStateExceptionWhenChannelisNull()
            throws IOException {
        final WritableByteChannel w = null;
        final HexEncoder e = encoder();
        final int c = current().nextInt(128) << 1;
        final boolean d = current().nextBoolean();
        try (WritableHexChannel whc = new WritableHexChannel(w, e, c, d)) {
            final boolean open = whc.isOpen();
        }
    }

    /**
     * Assert {@link WritableHexChannel#close()} silently returns even if
     * {@link WritableHexChannel#channel} is {@code null}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void assertCloseReturnsEvenIfChannelIsNull() throws IOException {
        final WritableByteChannel w = null;
        final HexEncoder e = encoder();
        final int c = current().nextInt(128) << 1;
        final boolean d = current().nextBoolean();
        try (WritableByteChannel whc = new WritableHexChannel(w, e, c, d)) {
        }
    }

    @Test
    public void createWithNullChannel() throws IOException {
        final WritableByteChannel w = null;
        final HexEncoder e = encoder();
        final int c = current().nextInt(128) << 1;
        final boolean d = current().nextBoolean();
        try (WritableHexChannel whc = new WritableHexChannel(w, e, c, d) {
            @Override
            public int write(final ByteBuffer buffer) throws IOException {
                if (channel == null) {
                    channel = Channels.newChannel(new ByteArrayOutputStream());
                }
                return super.write(buffer);
            }
        }) {
            final ByteBuffer src = ByteBuffer.allocate(current().nextInt(128));
            final int written = whc.write(src);
        }
    }

    @Test
    public void createWithNullEncoder() throws IOException {
        final WritableByteChannel w
                = Channels.newChannel(new ByteArrayOutputStream());
        final HexEncoder e = null;
        final int c = current().nextInt(128) << 1;
        final boolean d = current().nextBoolean();
        try (WritableHexChannel whc = new WritableHexChannel(w, e, c, d) {
            @Override
            public int write(final ByteBuffer buffer) throws IOException {
                if (encoder == null) {
                    encoder = encoder();
                }
                return super.write(buffer);
            }
        }) {
            final ByteBuffer src = ByteBuffer.allocate(current().nextInt(128));
            final int written = whc.write(src);
        }
    }

    @Test
    public void write() throws IOException {
        final WritableByteChannel w = null;
        final HexEncoder e = null;
        final int c = current().nextInt(128) << 1;
        final boolean d = current().nextBoolean();
        try (WritableHexChannel whc = new WritableHexChannel(w, e, c, d) {
            @Override
            public int write(final ByteBuffer buffer) throws IOException {
                if (channel == null) {
                    channel = Channels.newChannel(new ByteArrayOutputStream());
                }
                if (encoder == null) {
                    encoder = encoder();
                }
                return super.write(buffer);
            }
        }) {
            final ByteBuffer src = ByteBuffer.allocate(current().nextInt(128));
            final int written = whc.write(src);
        }
    }

    private transient final Logger logger = getLogger(getClass());

}
