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
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 * A class testing {@link HexInputStream} class.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexInputStreamTest extends AbstractHexDecoderTest {

    /**
     * Tests {@link HexInputStream#read()} with even number of bytes.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void read() throws IOException {
        final InputStream in = new ByteArrayInputStream(
                new byte[(current().nextInt(128) >> 1) << 1]);
        try (final InputStream his = apply(d -> new HexInputStream(in, d))) {
            while (his.read() != -1);
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
        try (final InputStream his = apply(d -> new HexInputStream(in, d))) {
            while (his.read() != -1);
        }
    }

    /**
     * Tests {@link HexInputStream#read(byte[], int, int)} with even number of
     * bytes.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void readWithArray() throws IOException {
        final int length = (current().nextInt(128) >> 1) << 1;
        final InputStream in = new ByteArrayInputStream(new byte[length]);
        try (final InputStream his = apply(d -> new HexInputStream(in, d))) {
            final byte[] buf = new byte[current().nextInt(128)];
            while (his.read(buf) != -1);
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
        try (final InputStream his = apply(d -> new HexInputStream(in, d))) {
            final byte[] buf = new byte[current().nextInt(128)];
            while (his.read(buf) != -1);
        }
    }

    /**
     * Tests {@link HexInputStream#mark(int)}.
     */
    @Test
    public void mark() {
        final InputStream his = apply(d -> new HexInputStream(
                new ByteArrayInputStream(new byte[current().nextInt(128)]), d));
        his.mark(current().nextInt() >> 2);
    }

    /**
     * Tests {@link HexInputStream#available()}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void available() throws IOException {
        final InputStream his = apply(d -> new HexInputStream(
                new ByteArrayInputStream(new byte[current().nextInt(128)]), d));
        final int available = his.available();
    }

    /**
     * Tests {@link HexInputStream#skip(long)}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(invocationCount = 128)
    public void skip() throws IOException {
        final InputStream in = mock(InputStream.class);
        when(in.skip(anyLong())).thenAnswer(
                i -> current().nextLong(i.getArgumentAt(0, long.class) + 1));
        try (InputStream his = apply(dec -> new HexInputStream(in, dec))) {
            final long n = current().nextLong(current().nextLong(1024L));
            final long skipped = his.skip(n);
            assertTrue(skipped <= n);
        }
    }
}
