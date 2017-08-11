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
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import static java.util.Objects.requireNonNull;
import java.util.function.Supplier;

/**
 * An abstract channel for filtering underlying channels.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> channel type parameter.
 */
public class FilterChannel<T extends Channel> implements Channel {

    /**
     * Creates a new instance on top of given channel.
     *
     * @param channelSupplier the channel to wrap.
     */
    public FilterChannel(final Supplier<T> channelSupplier) {
        super();
        this.channelSupplier = requireNonNull(
                channelSupplier, "channelSupplier is null");
    }

    /**
     * Tells whether or not this channel is open. The {@code isOpen()} method of
     * {@code FilterChannel} class return the value of {@code channel.isOpen()}.
     *
     * @return {@code true} if, and only if, the {@link #channel} is not
     * {@code null} and is open.
     */
    @Override
    public boolean isOpen() {
        return !closed;
    }

    /**
     * Closes this channel. The {@code close()} method of {@code FilterChannel}
     * class, if {@link #channelSupplier} is not {@code null}, invokes
     * {@link Channel#close()} on {@link #channelSupplier}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.close();
            channel = null;
        }
        closed = true;
    }

    /**
     * Returns the underlying channel.
     *
     * @return the underlying channel
     * @throws ClosedChannelException if this channel is already closed
     */
    protected T channel() throws ClosedChannelException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        if (channel == null && (channel = channelSupplier.get()) == null) {
            throw new RuntimeException("null channel supplied");
        }
        return channel;
    }

    private final Supplier<T> channelSupplier; // <1>

    private boolean closed; // <2>

    private T channel; // <3>
}
