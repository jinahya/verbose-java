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
import static java.util.Objects.requireNonNull;
import java.util.function.Supplier;

/**
 * A {@code ReadableByteChannel} decodes hex characters to bytes.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @param <T> channel type parameter
 * @param <U> decoder type parameter
 */
public class ReadableHexChannel<T extends ReadableByteChannel, U extends HexDecoder>
        extends ReadableFilterChannel<T> {

    /**
     * Creates a new instance on top of given channel that given supplier
     * supplies.
     *
     * @param channelSupplier a supplier supplies the channel from which encoded
     * characters are read
     * @param decoderSupplier a supplier supplies the decoder for decoding
     * characters into bytes.
     */
    public ReadableHexChannel(final Supplier<T> channelSupplier,
                              final Supplier<U> decoderSupplier) {
        super(channelSupplier);
        this.decoderSupplier = requireNonNull(
                decoderSupplier, "decoderSupplier is null");
    }

    /**
     * Returns the decoder.
     *
     * @return the decoder.
     */
    protected U decoder() {
        if (decoder == null && (decoder = decoderSupplier.get()) == null) {
            throw new RuntimeException("null decoder supplied");
        }
        return decoder;
    }

    /**
     * {@inheritDoc} The {@code read(ByteBuffer)} method of
     * {@code ReadableHexChannel} class read maximum by double number of bytes
     * of {@code dst.remaining()} and decodes them using
     * {@link #decoderSupplier}.
     *
     * @param dst The buffer into which bytes are to be transferred
     * @return The number of bytes read, possibly zero, or -1 if the channel has
     * reached end-of-stream
     * @throws IOException If some other I/O error occurs
     */
    @Override
    public int read(final ByteBuffer dst) throws IOException {
        final ByteBuffer aux = allocate(dst.remaining() << 1); // <1>
        if (super.read(aux) == -1) { // <2>
            return -1;
        }
        if ((aux.position() & 1) == 1) { // <3>
            aux.limit(aux.position() + 1);
            while (aux.hasRemaining()) {
                if (super.read(aux) == -1) { // unacceptable end-of-stream
                    throw new EOFException();
                }
            }
        }
        aux.flip();
        return decoder().decode(aux, dst); // <4>
    }

    /**
     * The supplier lazily supplies the {@code decoder}.
     */
    private final Supplier<U> decoderSupplier;

    /**
     * The decoder lazily supplied from the {@code decoderSupplier}.
     */
    private U decoder;
}
