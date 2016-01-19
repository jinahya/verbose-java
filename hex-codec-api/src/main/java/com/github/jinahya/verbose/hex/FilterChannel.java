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

/**
 * An abstract channel for encoding/decoding bytes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> channel type parameter.
 * @param <U> codec type parameter.
 */
public abstract class FilterChannel<T extends Channel, U> implements Channel {

    /**
     * Creates a new instance on top of given channel.
     *
     * @param channel the channel to wrap.
     * @param filter the filter
     * @param capacity the capacity of intermediate buffer.
     * @param direct the flag for direct allocation of the intermediate buffer.
     */
    public FilterChannel(final T channel, final U filter, final int capacity,
                         final boolean direct) {
        super();
        if (capacity < 2) { // <1>
            throw new IllegalArgumentException(
                    "capacity(" + capacity + ") < 2");
        }
        this.channel = channel;
        this.filter = filter;
        this.capacity = capacity;
        this.direct = direct;
    }

    /**
     * Tells whether or not this channel is open. The {@code isOpen()} method of
     * {@code WritableHexChannel} class invokes {@link Channel#isOpen()} on
     * {@link #channel} and returns the result.
     *
     * @return {@code true} if, and only if, the {@link #channel} is open
     */
    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    /**
     * Closes this channel. The {@code close()} method of
     * {@code WritableHexChannel} class, if {@link #channel} is not
     * {@code null}, invokes {@link Channel#close()} on {@link #channel}.
     * Override this method if any prerequisite tasks need to be done on
     * {@link #channel} before closed.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.close();
        }
    }

    protected ByteBuffer buffer() {
        if (buffer == null) {
            buffer = direct
                     ? ByteBuffer.allocateDirect(capacity)
                     : ByteBuffer.allocate(capacity);
        }
        return buffer;
    }

    /**
     * The underlying channel.
     */
    protected T channel;

    /**
     * The codec.
     */
    protected U filter;

    private final int capacity;

    private final boolean direct;

    private ByteBuffer buffer;
}
