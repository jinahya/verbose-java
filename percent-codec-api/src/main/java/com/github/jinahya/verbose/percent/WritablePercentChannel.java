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
package com.github.jinahya.verbose.percent;

import java.io.IOException;
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.WritableByteChannel;
import static java.util.Objects.requireNonNull;
import java.util.function.Supplier;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * A writable byte channel encodes bytes being written into percent encoded
 * bytes.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @param <T> channel type parameter
 * @param <U> encoder type parameter
 */
public class WritablePercentChannel<T extends WritableByteChannel, U extends PercentEncoder>
        implements WritableByteChannel {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance.
     *
     * @param channelSupplier a supplier for the channel to which encoded bytes
     * are written
     * @param encoderSupplier a supplier for the encoder for encoding
     */
    public WritablePercentChannel(final Supplier<T> channelSupplier,
                                  final Supplier<U> encoderSupplier) {
        super();
        this.channelSupplier = requireNonNull(
                channelSupplier, "channelSupplier is null");
        this.encoderSupplier = requireNonNull(
                encoderSupplier, "encoderSupplier is null");
    }

    // -------------------------------------------------------------------------
    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        if (channel != null) {
            channel.close();
            channel = null;
        }
        closed = true;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        if (src == null) {
            throw new NullPointerException("src is null");
        }
        if (src.capacity() == 0 || !src.hasRemaining()) {
            return 0;
        }
        // @todo: implement.
        throw new UnsupportedOperationException("not implemented yet");
    }

    // ----------------------------------------------------------------- channel
    protected T channel() throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        if (channel == null && (channel = channelSupplier.get()) == null) {
            throw new RuntimeException("supplied channel is null");
        }
        return channel;
    }

    // ----------------------------------------------------------------- encoder
    protected U encoder() throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        if (encoder == null && (encoder = encoderSupplier.get()) == null) {
            throw new RuntimeException("supplied decoder is null");
        }
        return encoder;
    }

    // -------------------------------------------------------------------------
    private final Supplier<T> channelSupplier;

    private final Supplier<U> encoderSupplier;

    // -------------------------------------------------------------------------
    private boolean closed;

    // -------------------------------------------------------------------------
    private transient T channel;

    private transient U encoder;
}
