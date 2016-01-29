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
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.WritableByteChannel;

public class WritablePercentChannel implements WritableByteChannel {

    public WritablePercentChannel(final WritableByteChannel channel,
                                  final PercentEncoder encoder) {
        super();
        this.channel = channel;
        this.encoder = encoder;
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.close();
        }
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        final ByteBuffer aux = allocate(src.remaining() * 3);
        final int encoded = encoder.encode(src, aux);
        aux.flip();
        while (aux.hasRemaining()) {
            channel.write(aux);
        }
        return encoded;
    }

    protected WritableByteChannel channel;

    protected PercentEncoder encoder;
}
