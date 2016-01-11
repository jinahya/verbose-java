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

import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentEncoderDemo implements PercentEncoder {

    @Override
    public void encodeOctet(final int decoded, final ByteBuffer encoded) {
        if ((decoded >= 0x30 && decoded <= 0x39)
            || (decoded >= 0x41 && decoded <= 0x5A)
            || (decoded >= 0x61 && decoded <= 0x7A) || decoded == 0x2D
            || decoded == 0x5F || decoded == 0x2E || decoded == 0x7E) {
            encoded.put((byte) decoded);
            return;
        }
        encoded.put((byte) 0x25);
        encoded.put(String.format("%02X", decoded & 0xFF).getBytes(US_ASCII));
    }
}
