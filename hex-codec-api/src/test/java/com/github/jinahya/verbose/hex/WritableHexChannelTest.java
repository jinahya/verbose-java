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
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.Channels.newChannel;
import java.nio.channels.WritableByteChannel;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
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
public class WritableHexChannelTest extends AbstractHexEncoderTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    /**
     * Tests {@link WritableHexChannelEx#isOpen()}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testIsOpen() throws IOException {
        final WritableByteChannel whc
                = new WritableHexChannel(() -> null, () -> null);
        assertTrue(whc.isOpen());
        whc.close();
        assertFalse(whc.isOpen());
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
                    = new WritableHexChannel(() -> null, () -> null);
            whc.close();
            whc.close();
            whc.close();
        }
        {
            final WritableHexChannel whc = apply(e -> new WritableHexChannel(
                    () -> null, () -> e));
            whc.close();
            whc.close();
            whc.close();
        }
        {
            final WritableHexChannel whc = new WritableHexChannel(
                    () -> newChannel(new ByteArrayOutputStream()), () -> null);
            whc.close();
            whc.close();
            whc.close();
        }
        {
            final WritableByteChannel whc = apply(e -> new WritableHexChannel(
                    () -> newChannel(new ByteArrayOutputStream()), () -> e));
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
        try (WritableByteChannel whc = apply(e -> new WritableHexChannel(
                () -> newChannel(new ByteArrayOutputStream()),
                () -> e))) {
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
        try (WritableByteChannel whc = apply(e -> new WritableHexChannel(
                () -> nonBlocking(
                        WritableByteChannel.class,
                        newChannel(new ByteArrayOutputStream())),
                () -> e))) {
            final ByteBuffer src = allocate(current().nextInt(1, 128));
            final int written = whc.write(src);
        }
    }
}
