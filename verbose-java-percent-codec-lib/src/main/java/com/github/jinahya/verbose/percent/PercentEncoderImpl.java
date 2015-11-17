/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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

import com.github.jinahya.verbose.hex.HexEncoder;
import com.github.jinahya.verbose.hex.HexEncoderImpl;
import java.nio.ByteBuffer;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentEncoderImpl implements PercentEncoder {

    @Override
    public void encodeSingle(final int decoded, final ByteBuffer encoded) {
        if (Rfc3986.isUnreservedCharacter(decoded)) {
            encoded.put((byte) decoded);
        } else {
            encoded.put((byte) 0x25); // '%'
            hexEncoder.encodeSingle(decoded, encoded);
        }
    }

    private final HexEncoder hexEncoder = new HexEncoderImpl();
}
