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
import java.nio.channels.ReadableByteChannel;

/**
 * A readable byte channel decodes hex characters to bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ReadableHexChannel1
        extends FilterChannel<ReadableByteChannel, HexDecoder>
        implements ReadableByteChannel {

    /**
     * Creates a new instance on top of given channel.
     *
     * @param channel the underlying channel
     * @param decoder a decoder to decode bytes from the underlying channel
     * @param capacity capacity for decoding buffer
     * @param direct a flag for direct allocation of decoding buffer.
     */
    public ReadableHexChannel1(final ReadableByteChannel channel,
                               final HexDecoder decoder, final int capacity,
                               final boolean direct) {
        super(channel, decoder, capacity, direct);
        if (capacity < 2) {
            throw new IllegalArgumentException(
                    "capacity(" + capacity + ") < 2");
        }
    }

    /**
     * Reads a sequence of bytes from this channel into the given buffer.
     *
     * @param dst The buffer into which bytes are to be transferred
     * @return The number of bytes read, possibly zero, or -1 if the channel has
     * reached end-of-stream
     * @throws IOException If some other I/O error occurs
     */
    @Override
    public int read(final ByteBuffer dst) throws IOException {
        int count = 0;
        while (dst.hasRemaining()) {
            final int max = dst.remaining() * 2;
            if (buffer().limit() > max) { // <1>
                buffer().limit(max);
            }
            final int remaining = buffer().remaining();
            final int read = channel.read(buffer());
            if (read == -1) { // <2>
                if (count == 0 && buffer().position() == 0) {
                    return -1;
                }
                break;
            }
            buffer().flip(); // <3>
            count += filter.decode(buffer(), dst);
            buffer().compact();
            if (read < remaining) { // <4>
                break;
            }
        }
        if ((buffer().position() & 1) == 1) { // <5>
            buffer().limit(buffer().position() + 1);
            while (buffer().hasRemaining()) {
                if (channel.read(buffer()) == -1) {
                    throw new EOFException(); // unexpected end-of-stream
                }
            }
        }
        buffer().flip();
        count += filter.decode(buffer(), dst);
        buffer().compact();
        return count;
    }

}
