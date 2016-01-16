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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.WritableByteChannel;

/**
 * A {@code WritableByteChannel} which encodes bytes to hex characters.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexEncoderChannel implements WritableByteChannel {

    /**
     * Creates a new instance on top of given channel.
     *
     * @param channel the channel to wrap.
     * @param encoder the encoder for encoding bytes to hex characters
     * @param capacity the capacity of intermediate buffer.
     * @param direct the flag for direct allocation of the intermediate buffer.
     */
    public HexEncoderChannel(final WritableByteChannel channel,
                             final HexEncoder encoder, final int capacity,
                             final boolean direct) {
        super();
        if (capacity < 2) { // <1>
            throw new IllegalArgumentException(
                    "capacity(" + capacity + ") < 2");
        }
        this.channel = channel;
        this.encoder = encoder;
        this.capacity = capacity;
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
     * Writes a sequence of bytes to this channel from the given buffer. The
     * {@code write(ByteBuffer)} method of {@code HexEncoderChannel} encodes
     * given buffer using {@link #encoder} and writes the result to
     * {@link #channel}.
     *
     * @param src The buffer from which bytes are to be retrieved
     * @return The number of bytes written, possibly zero
     * @throws IOException If some other I/O error occurs
     */
    @Override
    public int write(final ByteBuffer src) throws IOException {
        if (buffer == null) { // <1>
            buffer = direct
                     ? ByteBuffer.allocateDirect(capacity)
                     : ByteBuffer.allocate(capacity);
        }
        int count = 0;
        while (src.hasRemaining()) {
            count += encoder.encode(src, buffer); // <2>
            buffer.flip();
            final int remaining = buffer.remaining();
            final int written = channel.write(buffer); // <3>
            buffer.compact();
            if (written < remaining) { // <4>
                break;
            }
        }
        for (buffer.flip(); buffer.hasRemaining();) { // <5>
            channel.write(buffer);
        }
        buffer.compact();
        return count;
    }

    /**
     * The underlying channel to which encoded characters are written.
     */
    protected WritableByteChannel channel;

    /**
     * The encoder for encoding bytes to characters.
     */
    protected HexEncoder encoder;

    private final int capacity;

    private final boolean direct;

    private ByteBuffer buffer;
}
