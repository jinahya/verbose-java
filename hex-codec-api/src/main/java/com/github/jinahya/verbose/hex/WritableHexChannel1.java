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
import java.nio.channels.WritableByteChannel;

/**
 * A writable byte channel encodes bytes to hex characters.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class WritableHexChannel1
        extends FilterChannel<WritableByteChannel, HexEncoder>
        implements WritableByteChannel {

    /**
     * Creates a new instance on top of given channel.
     *
     * @param channel the channel to wrap.
     * @param encoder the encoder for encoding bytes
     * @param capacity the capacity of intermediate buffer.
     * @param direct the flag for direct allocation of the intermediate buffer.
     */
    public WritableHexChannel1(final WritableByteChannel channel,
                               final HexEncoder encoder, final int capacity,
                               final boolean direct) {
        super(channel, encoder, capacity, direct);
        if (capacity < 2) { // <1>
            throw new IllegalArgumentException(
                    "capacity(" + capacity + ") < 2");
        }
    }

    /**
     * Writes a sequence of bytes to this channel from the given buffer. The
     * {@code write(ByteBuffer)} method of {@code WritableHexChannel} class
     * encodes given buffer using {@link #filter} and writes the result to
     * {@link #channel}.
     *
     * @param src The buffer from which bytes are to be retrieved
     * @return The number of bytes consumed from the buffer, possibly zero
     * @throws IOException If some other I/O error occurs
     */
    @Override
    public int write(final ByteBuffer src) throws IOException {
        int count = 0;
        while (src.hasRemaining()) {
            count += filter.encode(src, buffer()); // <2>
            buffer().flip();
            final int remaining = buffer().remaining();
            final int written = channel.write(buffer()); // <3>
            buffer().compact();
            if (written < remaining) { // <4>
                break;
            }
        }
        for (buffer().flip(); buffer().hasRemaining();) { // <5>
            channel.write(buffer());
        }
        buffer().compact();
        return count;
    }

}
