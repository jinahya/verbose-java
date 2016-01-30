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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * An output stream encodes bytes to hex characters.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexOutputStream extends FilterOutputStream {

    /**
     * Creates a new instance on top of given output stream.
     *
     * @param out the output stream
     * @param enc the encoder for {@link #enc}
     */
    public HexOutputStream(final OutputStream out, final HexEncoder enc) {
        super(out);
        this.enc = enc;
    }

    /**
     * Writes the specified byte to this output stream. The {@code write(int)}
     * method of {@code HexOutputStream} class encodes given byte using
     * {@link #enc} and writes two hex characters to {@link #out}.
     *
     * @param b the byte
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(final int b) throws IOException {
        if (buf == null) { // <1>
            buf = ByteBuffer.allocate(2);
        }
        enc.encodeOctet(b, buf); // <2>
        buf.flip();
        super.write(buf.get()); // <3>
        super.write(buf.get()); // <3>
        buf.clear();
    }

    /**
     * The encoder for encoding bytes to hex characters.
     */
    protected HexEncoder enc;

    private ByteBuffer buf;
}
