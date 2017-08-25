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

import static com.github.jinahya.verbose.util.RsUtils.randomUsAscii;
import static com.github.jinahya.verbose.util.RsUtils.randomUtf8;
import com.google.inject.Inject;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * A class testing {@link PercentEncoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = PercentEncoderMockModule.class)
public class PercentEncoderTest {

    @Test(invocationCount = 128)
    public void encodeBuffer() {
        {
            final ByteBuffer decoded = allocate(current().nextInt(128));
            final ByteBuffer encoded = encoder.encode(decoded);
            assertTrue(encoded.remaining() >= decoded.capacity());
            assertTrue(encoded.remaining() <= decoded.capacity() * 3);
        }
        {
            final ByteBuffer decoded = allocate(current().nextInt(128));
            final ByteBuffer encoded = allocate(current().nextInt(128));
            final int count = encoder.encode(decoded, encoded);
            assertTrue(count >= 0);
            assertEquals(count, decoded.position());
            assertTrue(encoded.position() >= decoded.position());
        }
    }

    @Test(invocationCount = 128)
    public void encodeStringUtf8() {
        final String decoded = randomUtf8(current().nextInt(1024));
        final String encoded = encoder.encode(decoded, UTF_8);
        assertTrue(encoded.length() >= decoded.length());
    }

    @Test(invocationCount = 128)
    public void encodeStringUsAscii() {
        final String decoded = randomUsAscii(current().nextInt(1024));
        final String encoded = encoder.encode(decoded, US_ASCII);
        assertTrue(encoded.length() >= decoded.length());
    }

    @Inject
    private PercentEncoder encoder;
}
