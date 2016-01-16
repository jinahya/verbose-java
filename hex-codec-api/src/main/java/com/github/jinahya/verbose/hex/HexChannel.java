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

import java.nio.ByteBuffer;
import java.nio.channels.Channel;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> channel type parameter
 */
public abstract class HexChannel<T extends Channel> {

    public HexChannel(final T channel, final int capacity,
                      final boolean direct) {
        super();
        if (capacity < 2) {
            throw new IllegalArgumentException(
                    "capacity(" + capacity + ") < 2");
        }
        this.channel = channel;
        this.capacity = (capacity >> 1) << 1;
        this.direct = direct;
    }

    protected ByteBuffer buffer() {
        if (buffer == null) {
            buffer = direct
                     ? ByteBuffer.allocateDirect(capacity)
                     : ByteBuffer.allocate(capacity);
        }
        return buffer;
    }

    protected T channel;

    private final int capacity;

    private final boolean direct;

    private ByteBuffer buffer;
}
