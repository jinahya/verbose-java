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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * An output stream encodes bytes to hex characters.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentEncodingStream extends FilterOutputStream {

    public PercentEncodingStream(final OutputStream out,
                                 final PercentEncoder encoder) {
        super(out);
        this.encoder = encoder;
    }

    @Override
    public void write(final int b) throws IOException {
        if (encoded == null) {
            encoded = ByteBuffer.allocate(3);
        }
        encoded.position(0);
        encoder.encodeOctet(b, encoded);
        encoded.position(0);
        while (encoded.hasRemaining()) {
            super.write(encoded.get());
        }
    }

    protected PercentEncoder encoder;

    private ByteBuffer encoded;
}
