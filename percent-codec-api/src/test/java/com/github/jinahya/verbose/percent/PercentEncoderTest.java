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
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.lang3.RandomStringUtils;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentEncoderTest {

    private PercentEncoder encoder() {
        return new PercentEncoderDemo();
    }

    @Test(invocationCount = 256)
    public void encodeBuffer() {
        final ByteBuffer decoded
                = ByteBuffer.allocate(current().nextInt(1, 1024));
        final int remaining = decoded.remaining();
        final ByteBuffer encoded = encoder().encode(decoded);
        assertTrue(encoded.remaining() >= remaining);
    }

    @Test(invocationCount = 256)
    public void encodeBufferWithEncodedBuffer() {
        final ByteBuffer decoded
                = ByteBuffer.allocate(current().nextInt(1, 1024));
        final ByteBuffer encoded = ByteBuffer.allocate(
                current().nextInt(decoded.remaining() * 3));
        encoder().encode(decoded, encoded);
    }

    @Test(invocationCount = 256)
    public void encodeString() {
        final String decoded
                = RandomStringUtils.random(current().nextInt(1024));
        final String encoded = encoder().encode(decoded, UTF_8);
        assertTrue(encoded.length() >= decoded.length());
    }

    @Test(invocationCount = 256)
    public void encodeAscii() {
        final String decoded
                = RandomStringUtils.randomAscii(current().nextInt(1024));
        final String encoded = encoder().encode(decoded, US_ASCII);
        assertTrue(encoded.length() >= decoded.length());
    }
}
