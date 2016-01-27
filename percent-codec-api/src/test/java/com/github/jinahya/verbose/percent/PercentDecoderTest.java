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

import com.google.inject.Inject;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.lang3.RandomStringUtils.randomAscii;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * A class testing {@link PercentDecoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = PercentDecoderMockModule.class)
public class PercentDecoderTest {

    @Test(invocationCount = 128)
    public void decodeWithABuffer() {
        {
            final ByteBuffer encoded = allocate(current().nextInt(128));
            final ByteBuffer decoded = decoder.decode(encoded);
            assertTrue(decoded.remaining() <= encoded.capacity());
            assertTrue(decoded.remaining() >= encoded.capacity() / 3);
        }
        {
            final ByteBuffer encoded = allocate(current().nextInt(128));
            final ByteBuffer decoded = allocate(encoded.remaining());
            final int count = decoder.decode(encoded, decoded);
            assertTrue(count >= 0);
            assertEquals(count, decoded.position());
            assertTrue(decoded.position() <= encoded.position());
        }
    }

    @Test(invocationCount = 128)
    public void decodeString() {
        final String encoded = randomAscii(current().nextInt(128));
        final String decoded = decoder.decode(encoded);
        assertTrue(decoded.length() <= encoded.length());
    }

    @Test(invocationCount = 128)
    public void decodeAscii() {
        final String encoded = randomAscii(current().nextInt(128));
        final String decoded = decoder.decode(encoded, US_ASCII);
        assertTrue(decoded.length() <= encoded.length());
    }

    @Inject
    private PercentDecoder decoder;
}
