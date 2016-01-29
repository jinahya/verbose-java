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

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.ReadableByteChannel;

public class ReadablePercentChannel implements ReadableByteChannel {

    public ReadablePercentChannel(final ReadableByteChannel channel,
                                  final PercentDecoder decoder) {
        super();
        this.channel = channel;
        this.decoder = decoder;
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
    public int read(final ByteBuffer dst) throws IOException {
        final ByteBuffer aux = allocate(dst.remaining());
        if (channel.read(aux) == -1) {
            return -1;
        }
        aux.flip();
        int decoded = decoder.decode(aux, dst);
        assert aux.remaining() < 3;
        if (aux.hasRemaining()) {
            assert dst.hasRemaining();
            aux.compact();
            aux.limit(aux.position() + (3 - aux.position()));
            while (aux.hasRemaining()) {
                if (channel.read(aux) == -1) {
                    throw new EOFException("unexpected eof");
                }
            }
            aux.flip();
            assert decoder.decode(aux, dst) == 1;
            decoded += 1;
        }
        return decoded;
    }

    protected ReadableByteChannel channel;

    protected PercentDecoder decoder;
}
