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
import java.nio.channels.WritableByteChannel;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class WritableHexChannel_O
        extends WritableFilterChannel<WritableByteChannel> {

    public WritableHexChannel_O(final WritableByteChannel channel,
                                         final HexEncoder encoder) {
        super(channel);
        this.encoder = encoder;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        final ByteBuffer aux = ByteBuffer.allocate(src.remaining() << 1); // <1>
        final int count = encoder.encode(src, aux); // <2>
        for (aux.flip(); aux.hasRemaining();) { // <3>
            channel.write(aux);
        }
        return count; // <4>
    }

    protected HexEncoder encoder;
}
