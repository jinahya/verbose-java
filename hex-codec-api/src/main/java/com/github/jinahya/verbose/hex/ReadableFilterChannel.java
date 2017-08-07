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
import java.nio.channels.ReadableByteChannel;
import java.util.function.Supplier;

/**
 * A {@code ReadableByteChannel} filters another {@code ReadableByteChannel}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ReadableFilterChannel<T extends ReadableByteChannel>
        extends FilterChannel<T>
        implements ReadableByteChannel {

    /**
     * Creates a new instance of top of given channel.
     *
     * @param channelSupplier the channel
     */
    public ReadableFilterChannel(final Supplier<T> channelSupplier) {
        super(channelSupplier);
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
        return channel().read(dst);
    }
}
