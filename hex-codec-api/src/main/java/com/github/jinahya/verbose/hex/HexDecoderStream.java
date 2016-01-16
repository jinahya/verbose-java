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
     * Creates a new instance on top of specified input stream.
     *
     * @param in the input stream
     * @param dec the decoder
     */
    public HexDecoderStream(final InputStream in, final HexDecoder dec) {
        super(in);
        this.dec = dec;
    }

    /**
     * Reads the next byte of data from the input stream. The {@code read()}
     * method of {@code HexDecoderStream} class reads two bytes from the
     * underlying input stream and decodes them into a single byte using
     * {@link #dec} and returns the result.
     *
     * @return the next byte of data, or -1 if the end of the stream is reached.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        if (buf == null) { // <1>
            buf = ByteBuffer.allocate(2);
        }
        final int b1 = super.read(); // <2>
        if (b1 == -1) {
            return b1;
        }
        buf.put((byte) b1);
        final int b2 = super.read(); // <3>
        if (b2 == -1) {
            throw new EOFException();
        }
        buf.put((byte) b2);
        buf.flip();
        final int b = dec.decodeOctet(buf); // <4>
        buf.compact();
        return b;
    }

    /**
     * Reads up to {@code len} bytes of data from this input stream into an
     * array of bytes. The {@code read(byte[], int, int)} method of
     * {@code HexDecoderStream} class tries to read up to most {@code len} bytes
     * via {@link #read()}.
     *
     * @param b the buffer into which the data is read.
     * @param off the start offset in the destination array {@code b}
     * @param len the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or -1 if there is
     * no more data because the end of the stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public final int read(final byte[] b, final int off, final int len)
            throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        int count = 0;
        for (; count < len; count++) {
            final int read = read();
            if (read == -1) { // <1>
                if (count == 0) {
                    return -1;
                }
                break;
            }
            b[off + count] = (byte) read;
        }
        return count;
    }

    /**
     * Marks the current position in this input stream. The {@code mark(int)}
     * method of {@code HexDecoderStream} class invokes
     * {@link InputStream#mark(int)} on {@link #in} with doubled value of given
     * {@code readLimit}.
     *
     * @param readlimit the maximum limit of bytes that can be read before the
     * mark position becomes invalid.
     */
    @Override
    public synchronized void mark(final int readlimit) {
        final int reallimit = Integer.MAX_VALUE / 2; // <1>
        if (readlimit > reallimit) {
            throw new IllegalArgumentException(
                    "readlimit(" + readlimit + ") > " + reallimit);
        }
        super.mark(readlimit * 2);
    }

    /**
     * Returns an estimate of the number of bytes that can be read (or skipped
     * over) from this input stream without blocking by the next invocation of a
     * method for this input stream. The {@code available()} method of
     * {@code HexDecoderStream} class invokes {@link InputStream#available()} on
     * {@link #in} and returns the value divided by {@code 2}.
     *
     * @return an estimate of the number of bytes that can be read (or skipped
     * over) from this input stream without blocking or {@code 0} when it
     * reaches the end of the input stream.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int available() throws IOException {
        return super.available() / 2;
    }

    /**
     * Skips over and discards n bytes of data from this input stream. The
     * {@code skip(long)} method of {@code HexDecoderStream} class tries to read
     * up to most {@code n} bytes using {@link #read()} utile an
     * {@code end-of-stream} reached.
     *
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @throws IOException if the stream does not support seek, or if some other
     * I/O error occurs.
     */
    @Override
    public long skip(final long n) throws IOException {
        long count = 0L;
        for (; count < n && read() != -1; count++);
        return count;
    }

    /**
     * The decoder for decoding hex characters to bytes.
     */
    protected HexDecoder dec;

    private ByteBuffer buf;
}
