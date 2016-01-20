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

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 * A class testing {@link PercentDecoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentDecoderTest {

    private static PercentDecoder decoder() {
        return e -> {
            if (!e.hasRemaining()) {
                throw new BufferUnderflowException();
            }
            if (e.remaining() >= 3 && current().nextBoolean()) {
                e.position(e.position() + 3);
                return 0;
            }
            e.position(e.position() + 1);
            return 0;
        };
    }

    @Test(invocationCount = 128)
    public void decodeWithABuffer() {
        final ByteBuffer encoded = ByteBuffer.allocate(current().nextInt(128));
        final ByteBuffer decoded = decoder().decode(encoded);
        assertTrue(decoded.remaining() <= encoded.capacity());
    }

    @Test(invocationCount = 128)
    public void decodeWithDecodedBuffer() {
        final ByteBuffer encoded = ByteBuffer.allocate(current().nextInt(128));
        final ByteBuffer decoded = ByteBuffer.allocate(encoded.remaining());
        decoder().decode(encoded, decoded);
        assertTrue(decoded.position() <= encoded.position());
    }

    @Test(invocationCount = 128)
    public void decodeString() {
        final String encoded
                = RandomStringUtils.randomAscii(current().nextInt(128));
        final String decoded = decoder().decode(encoded);
        assertTrue(decoded.length() <= encoded.length());
    }

    @Test(invocationCount = 128)
    public void decodeAscii() {
        final String encoded
                = RandomStringUtils.randomAscii(current().nextInt(1024));
        final String decoded = decoder().decode(encoded, US_ASCII);
        assertTrue(decoded.length() <= encoded.length());
    }

    private transient final Logger logger = getLogger(getClass());

}
