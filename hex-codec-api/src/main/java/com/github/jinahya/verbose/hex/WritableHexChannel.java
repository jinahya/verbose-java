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
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.WritableByteChannel;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class WritableHexChannel extends WritableFilterChannel {

    /**
     * Creates a new instance on top of given channel.
     *
     * @param channel the channel into which encoded hex characters are written
     * @param encoder the encoder to encode bytes
     */
    public WritableHexChannel(final WritableByteChannel channel,
                              final HexEncoder encoder) {
        super(channel);
        this.encoder = encoder;
    }

    /**
     * Writes a sequence of bytes to this channel from the given buffer.
     *
     * @param src {@inheritDoc}
     * @return The number of bytes written, possibly zero
     * @throws IOException If some other I/O error occurs
     */
    @Override
    public int write(final ByteBuffer src) throws IOException {
        final ByteBuffer aux = allocate(src.remaining() << 1); // <1>
        final int count = encoder.encode(src, aux); // <2>
        for (aux.flip(); aux.hasRemaining();) { // <3>
            channel.write(aux);
        }
        return count;
    }

    /**
     * The encoder for encoding bytes into characters.
     */
    protected HexEncoder encoder;
}
