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

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * An input stream decodes hex characters to bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexDecoderStream extends FilterInputStream {

    /**
     * Creates a new instance with given input stream and decoder.
     *
     * @param in the input stream
     * @param decoder the decoder
     */
    public HexDecoderStream(final InputStream in, final HexDecoder decoder) {
        super(in);
        this.decoder = decoder;
    }

    @Override
    public synchronized void mark(final int readlimit) {
        super.mark(readlimit * 2);
    }

    @Override
    public int available() throws IOException {
        return super.available() / 2;
    }

    @Override
    public long skip(final long n) throws IOException {
        long count = 0L;
        for (long i = 0; i < n; i++) {
            final int r = read();
            if (r == -1) {
                break;
            }
        }
        return count;
    }

    @Override
    public final int read(final byte[] b, final int off, final int len)
            throws IOException {
        int i;
        for (i = 0; i < len; i++) {
            final int r = read();
            if (r == -1) {
                if (i == 0) {
                    return -1;
                }
                break;
            }
            b[off + i] = (byte) r;
        }
        return i;
    }

    /**
     * {@inheritDoc} The {@code read()} method of {@code HexDecoderStream} class
     * read two byte from the underlying input stream and decodes them into a
     * single octet using {@link #decoder} and returns the result.
     *
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        if (decoded == null) {
            decoded = ByteBuffer.allocate(2);
        }
        decoded.position(0);
        final int b1 = super.read(); // <1>
        if (b1 == -1) {
            return b1;
        }
        decoded.put((byte) b1);
        final int b2 = super.read(); // <2>
        if (b2 == -1) {
            throw new EOFException();
        }
        decoded.put((byte) b2);
        decoded.position(0);
        return decoder.decodeOctet(decoded); // <3>
    }

    /**
     * The encoder to encode two hex characters to a octet.
     */
    protected HexDecoder decoder;

    private ByteBuffer decoded;
}
