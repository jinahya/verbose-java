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
import java.nio.channels.ReadableByteChannel;
import static java.util.Objects.requireNonNull;
import java.util.function.Supplier;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * A readable byte channel reads percent-encoded bytes from an underlying byte
 * channel and decodes them.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 * @param <T> channel type parameter
 * @param <U> decoder type parameter
 */
public class ReadablePercentChannel<T extends ReadableByteChannel, U extends PercentDecoder>
        implements ReadableByteChannel {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    // -------------------------------------------------------------------------
    public ReadablePercentChannel(final Supplier<T> channelSupplier,
                                  final Supplier<U> decoderSupplier) {
        super();
        this.channelSupplier = requireNonNull(
                channelSupplier, "channelSupplier is null");
        this.decoderSupplier = requireNonNull(
                decoderSupplier, "decoderSupplier is null");
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
    public int read(final ByteBuffer dst) throws IOException {
        if (dst == null) {
            throw new NullPointerException("dst is null");
        }
        if (dst.capacity() == 0 || !dst.hasRemaining()) {
            return 0;
        }
        // @todo: implement.
        throw new UnsupportedOperationException("not fully implemented yet");
    }

    // ----------------------------------------------------------------- channel
    protected T channel() throws ClosedChannelException {
        if (closed) {
            throw new ClosedChannelException();
        }
        if (channel == null && (channel = channelSupplier.get()) == null) {
            throw new RuntimeException("supplied channel is null");
        }
        return channel;
    }

    // ----------------------------------------------------------------- decoder
    protected U decoder() {
        if (decoder == null && (decoder = decoderSupplier.get()) == null) {
            throw new RuntimeException("supplied channel is null");
        }
        return decoder;
    }

    // -------------------------------------------------------------------------
    private final Supplier<T> channelSupplier;

    private final Supplier<U> decoderSupplier;

    // -------------------------------------------------------------------------
    private boolean closed;

    // -------------------------------------------------------------------------
    private transient T channel;

    private transient U decoder;
}
