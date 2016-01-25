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
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import static java.nio.charset.Charset.availableCharsets;
import java.nio.charset.CharsetEncoder;
import static java.nio.charset.StandardCharsets.US_ASCII;
import java.util.Map.Entry;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class CharsetTest {

    @Test
    public void name() throws CharacterCodingException {
        final String decodedName = "길동";
        System.out.printf("%-20s %-20s\n", "canonical", "encoded");
        System.out.printf("%1$20s %1$20s\n", "--------------------");
        for (Entry<String, Charset> entry : availableCharsets().entrySet()) {
            final String canonical = entry.getKey();
            final Charset charset = entry.getValue();
            if (!charset.canEncode()) {
                continue;
            }
            final CharsetEncoder encoder = charset.newEncoder();
            try {
                if (!encoder.canEncode(decodedName)) {
                    continue;
                }
            } catch (final UnsupportedOperationException uoe) { // :(
                continue;
            }
            final ByteBuffer decodedBuffer
                    = encoder.encode(CharBuffer.wrap(decodedName));
            final ByteBuffer encodedBuffer
                    = new HexEncoderImpl().encode(decodedBuffer);
            final byte[] bytes = encodedBuffer.array();
            final int offset
                    = encodedBuffer.arrayOffset() + encodedBuffer.position();
            final int length = encodedBuffer.remaining();
            final String encodedName
                    = new String(bytes, offset, length, US_ASCII);
            System.out.printf("%-20s %-20s\n", canonical, encodedName);
        }
    }
}
