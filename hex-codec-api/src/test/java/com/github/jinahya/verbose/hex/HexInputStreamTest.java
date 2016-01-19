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
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexInputStreamTest extends AbstractHexDecoderTest {

    @Test
    public void lazyIn() throws IOException {
        final InputStream in = null;
        final HexDecoder dec = decoder();
        final HexInputStream his = new HexInputStream(in, dec) {
            @Override
            public int read() throws IOException {
                if (in == null) {
                    in = new ByteArrayInputStream(new byte[0]);
                }
                return super.read();
            }
        };
        final int b = his.read();
    }

    @Test
    public void lazyDec() throws IOException {
        final InputStream in = new ByteArrayInputStream(new byte[0]);
        final HexDecoder dec = null;
        final HexInputStream his = new HexInputStream(in, dec) {
            @Override
            public int read() throws IOException {
                if (dec == null) {
                    dec = decoder();
                }
                return super.read();
            }
        };
        final int b = his.read();
    }

    @Test
    public void lazyInDec() throws IOException {
        final InputStream in = null;
        final HexDecoder dec = null;
        final HexInputStream his = new HexInputStream(in, dec) {
            @Override
            public int read() throws IOException {
                if (in == null) {
                    in = new ByteArrayInputStream(new byte[0]);
                }
                if (dec == null) {
                    dec = decoder();
                }
                return super.read();
            }
        };
        final int b = his.read();
    }

    @Test
    public void readSingleForEvenBytes() throws IOException {
        final InputStream in = new ByteArrayInputStream(
                new byte[(current().nextInt(128) >> 1) << 1]);
        final HexDecoder dec = decoder();
        try (final InputStream his = new HexInputStream(in, dec)) {
            for (int read; (read = his.read()) != -1;) {
            }
        }
    }

    @Test(expectedExceptions = EOFException.class)
    public void readSingleForOddBytes() throws IOException {
        final InputStream in = new ByteArrayInputStream(
                new byte[current().nextInt(128) | 1]);
        final HexDecoder dec = decoder();
        try (final InputStream his = new HexInputStream(in, dec)) {
            for (int read; (read = his.read()) != -1;) {
            }
        }
    }

    @Test
    public void readWithArrayForEvenBytes() throws IOException {
        final int length = (current().nextInt(128) >> 1) << 1;
        final InputStream in = new ByteArrayInputStream(new byte[length]);
        final HexDecoder dec = decoder();
        try (final InputStream his = new HexInputStream(in, dec)) {
            final byte[] buf = new byte[current().nextInt(128)];
            for (int read; (read = his.read(buf)) != -1;) {
            }
        }
    }

    @Test(expectedExceptions = EOFException.class)
    public void readWithArrayForOddBytes() throws IOException {
        final int length = current().nextInt(128) | 1;
        final InputStream in = new ByteArrayInputStream(new byte[length]);
        final HexDecoder dec = decoder();
        try (final InputStream his = new HexInputStream(in, dec)) {
            final byte[] buf = new byte[current().nextInt(128)];
            for (int read; (read = his.read(buf)) != -1;) {
            }
        }
    }

    @Test
    public void mark() throws IOException {
        final int length = (current().nextInt(128) >> 1) << 1;
        final InputStream in = new ByteArrayInputStream(new byte[length]);
        final HexDecoder dec = decoder();
        try (final InputStream his = new HexInputStream(in, dec)) {
            his.mark(current().nextInt() >>> 2);
        }
    }

    @Test
    public void available() throws IOException {
        final int length = (current().nextInt(128) >> 1) << 1;
        final InputStream in = new ByteArrayInputStream(new byte[length]);
        final HexDecoder dec = decoder();
        try (final InputStream his = new HexInputStream(in, dec)) {
            final int available = his.available();
            assertEquals(available, length >> 1);
        }
    }

    @Test
    public void skip() throws IOException {
        final int length = (current().nextInt(128) >> 1) << 1;
        final InputStream in = new ByteArrayInputStream(new byte[length]);
        final HexDecoder dec = decoder();
        try (final InputStream his = new HexInputStream(in, dec)) {
            final long skipped = his.skip(current().nextLong() >> 1);
        }
    }

    private transient final Logger logger = getLogger(getClass());
}
