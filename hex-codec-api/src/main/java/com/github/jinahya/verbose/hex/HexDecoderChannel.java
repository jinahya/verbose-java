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
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexDecoderChannel implements ReadableByteChannel {

    public HexDecoderChannel(final ReadableByteChannel channel,
                             final HexDecoder decoder, final int capacity,
                             final boolean direct) {
        super();
        if (capacity < 2) {
            throw new IllegalArgumentException(
                    "capacity(" + capacity + ") < 2");
        }
        this.channel = channel;
        this.decoder = decoder;
        this.capacity = (capacity >> 1) << 1;
        this.direct = direct;
    }

    /**
     * Tells whether or not this channel is open. The {@code isOpen()} method of
     * {@code HexEncoderChannel} class invokes {@link Channel#isOpen()} on
     * {@link #channel} and returns the result. An {@code IllegalStateException}
     * will be thrown if {@link #channel} is {@code null}.
     *
     * @return {@code true} if, and only if, the {@link #channel} is open
     */
    @Override
    public boolean isOpen() {
        if (channel == null) {
            throw new IllegalStateException("channel is currently null");
        }
        return channel.isOpen();
    }

    /**
     * Closes this channel. The {@code close()} method of
     * {@code HexEncoderChannel} class, if {@link #channel} is not {@code null},
     * invokes {@link Channel#close()} on {@link #channel}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.close();
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
        if (buffer == null) { // <1>
            buffer = direct ? ByteBuffer.allocateDirect(capacity)
                     : ByteBuffer.allocate(capacity);
        }
        int count = 0;
        while (dst.hasRemaining()) {
            buffer.limit(Math.min(buffer.limit(), dst.remaining() * 2)); // <2>
            final int remaining = buffer.remaining();
            final int read = channel.read(buffer);
            if (read == -1) { // <3>
                if (count == 0) {
                    return -1;
                }
                break;
            }
            if ((read & 1) == 1) { // <4>
                buffer.limit(buffer.position() + 1);
                while (buffer.hasRemaining()) {
                    if (channel.read(buffer) == -1) {
                        throw new EOFException(); // unexpected end-of-stream
                    }
                }
            }
            buffer.flip();
            count += decoder.decode(buffer, dst); // <5>
            buffer.compact();
            if (read < remaining) {
                break;
            }
        }
        return count;
    }

    protected ReadableByteChannel channel;

    protected HexDecoder decoder;

    private final int capacity;

    private final boolean direct;

    private ByteBuffer buffer;
}
