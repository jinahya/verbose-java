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
     * @param channel the channel to wrap.
     */
    public FilterChannel(final T channel) {
        super();
        this.channel = channel;
    }

    /**
     * Tells whether or not this channel is open. The {@code isOpen()} method of
     * {@code FilterChannel} class return the value of {@code channel.isOpen()}.
     *
     * @return {@code true} if, and only if, the {@link #channel} is open
     */
    @Override
    public boolean isOpen() {
        return channel.isOpen(); // <1>
    }

    /**
     * Closes this channel. The {@code close()} method of {@code FilterChannel}
     * class, if {@link #channel} is not {@code null}, invokes
     * {@link Channel#close()} on {@link #channel}.
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
     * The underlying channel.
     */
    protected T channel;
}
