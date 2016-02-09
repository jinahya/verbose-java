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
import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.ReadableByteChannel;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ReadableHexChannel extends ReadableFilterChannel {

    /**
     * Creates a new instance on top of given channel.
     *
     * @param channel the channel from which encoded characters are read
     * @param decoder the decoder for decoding characters into bytes.
     */
    public ReadableHexChannel(final ReadableByteChannel channel,
                              final HexDecoder decoder) {
        super(channel);
        this.decoder = decoder;
    }

    /**
     * Reads a sequence of bytes from this channel into the given buffer. The
     * {@code read(ByteBuffer)} method of {@code ReadableHexChannel} class read
     * maximum by double number of bytes of {@code dst.remaining()} and decodes
     * them using {@link #decoder}.
     *
     * @param dst The buffer into which bytes are to be transferred
     * @return The number of bytes read, possibly zero, or -1 if the channel has
     * reached end-of-stream
     * @throws IOException If some other I/O error occurs
     */
    @Override
    public int read(final ByteBuffer dst) throws IOException {
        final ByteBuffer aux = allocate(dst.remaining() >> 1 << 2); // <1>
        if (channel.read(aux) == -1) { // <2>
            return -1;
        }
        if ((aux.position() & 1) == 1) { // <3>
            aux.limit(aux.position() + 1);
            while (aux.hasRemaining()) {
                if (channel.read(aux) == -1) { // unacceptable end-of-stream
                    throw new EOFException();
                }
            }
        }
        aux.flip();
        return decoder.decode(aux, dst); // <3>
    }

    /**
     * The decoder for decoding characters into bytes.
     */
    protected HexDecoder decoder;

}
