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
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 * A class testing {@link HexInputStream} class.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexInputStreamTest extends AbstractHexDecoderTest {

    /**
     * Tests {@link HexInputStream#read()},
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void read() throws IOException {
        final InputStream in = null;
        final HexDecoder dec = null;
        final HexInputStream his = new HexInputStream(in, dec) {
            @Override
            public int read() throws IOException {
                if (in == null) {
                    in = mock(InputStream.class);
                    when(in.read()).thenReturn(current().nextInt(256));
                }
                if (dec == null) {
                    dec = decoder();
                }
                return super.read();
            }
        };
        for (int i = 0; i < 128; i++) {
            final int b = his.read();
        }
    }

    /**
     * Tests {@link HexInputStream#read(byte[], int, int)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void readWithArray() throws IOException {
        final InputStream in = null;
        final HexDecoder dec = null;
        final HexInputStream his = new HexInputStream(in, dec) {
            @Override
            public int read() throws IOException {
                if (in == null) {
                    in = mock(InputStream.class);
                    when(in.read()).thenReturn(current().nextInt(256));
                }
                if (dec == null) {
                    dec = decoder();
                }
                return super.read();
            }
        };
        for (int i = 0; i < 128; i++) {
            final byte[] b = new byte[current().nextInt(128)];
            final int off = b.length == 0
                            ? 0 : current().nextInt(0, b.length);
            final int len = b.length == 0
                            ? 0 : current().nextInt(0, b.length - off);
            final int r = his.read(b, off, len);
        }
    }

    /**
     * Tests {@link HexInputStream#read()} with even number of bytes.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void readFromEvenBytes() throws IOException {
        final InputStream in = new ByteArrayInputStream(
                new byte[(current().nextInt(128) >> 1) << 1]);
        final HexDecoder dec = decoder();
        try (final InputStream his = new HexInputStream(in, dec)) {
            for (int read; (read = his.read()) != -1;) {
            }
        }
    }

    /**
     * Expects a {@code EOFException} while calling
     * {@link HexInputStream#read()} with odd number of bytes.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(expectedExceptions = EOFException.class)
    public void readFromOddBytes() throws IOException {
        final InputStream in = new ByteArrayInputStream(
                new byte[current().nextInt(128) | 1]);
        final HexDecoder dec = decoder();
        try (final InputStream his = new HexInputStream(in, dec)) {
            for (int read; (read = his.read()) != -1;) {
            }
        }
    }

    /**
     * Tests {@link HexInputStream#read(byte[], int, int)} with even number of
     * bytes.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void readWithArrayFromEvenBytes() throws IOException {
        final int length = (current().nextInt(128) >> 1) << 1;
        final InputStream in = new ByteArrayInputStream(new byte[length]);
        final HexDecoder dec = decoder();
        try (final InputStream his = new HexInputStream(in, dec)) {
            final byte[] buf = new byte[current().nextInt(128)];
            for (int read; (read = his.read(buf)) != -1;) {
            }
        }
    }

    /**
     * Expects an {@code EOFException} while calling
     * {@link HexInputStream#read(byte[], int, int)} with odd number of bytes.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(expectedExceptions = EOFException.class)
    public void readWithArrayFromOddBytes() throws IOException {
        final int length = current().nextInt(128) | 1;
        final InputStream in = new ByteArrayInputStream(new byte[length]);
        final HexDecoder dec = decoder();
        try (final InputStream his = new HexInputStream(in, dec)) {
            final byte[] buf = new byte[current().nextInt(128)];
            for (int read; (read = his.read(buf)) != -1;) {
            }
        }
    }

    /**
     * Tests {@link HexInputStream#mark(int)}.
     */
    @Test
    public void mark() {
        final InputStream in = mock(InputStream.class);
        doNothing().when(in).mark(anyInt());
        final InputStream his = new HexInputStream(in, decoder());
        for (int i = 0; i < 128; i++) {
            final int readlimit = current().nextInt();
            his.mark(readlimit);
        }
    }

    /**
     * Tests {@link HexInputStream#available()}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void available() throws IOException {
        final InputStream in = mock(InputStream.class);
        when(in.read()).thenReturn(current().nextInt(256));
        when(in.available()).thenReturn(current().nextInt() >>> 1);
        final InputStream his = new HexInputStream(in, decoder());
        for (int i = 0; i < 128; i++) {
            final int available = his.available();
            assertTrue(available >= 0, "negative available");
        }
    }

    /**
     * Tests {@link HexInputStream#skip(long)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void skip() throws IOException {
        final InputStream in = mock(InputStream.class);
        when(in.read()).thenReturn(current().nextInt(256));
        when(in.skip(anyLong())).thenAnswer(i -> {
            final long n = i.getArgumentAt(0, long.class);
            return current().nextLong(n);
        });
        final InputStream his = new HexInputStream(in, decoder());
        for (int i = 0; i < 128; i++) {
            final long n = current().nextLong() >>> 1;
            final long skipped = his.skip(n);
            assertTrue(skipped <= n, "skppped more than requested");
        }
    }

    private transient final Logger logger = getLogger(getClass());
}
