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

import static java.nio.ByteBuffer.wrap;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAscii;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexCodecTest {

    @Test(enabled = true, invocationCount = 128)
    public void encodeDecodeBytes() {
        final byte[] createdBytes = new byte[current().nextInt(128)];
        current().nextBytes(createdBytes);
        final byte[] encodedBytes = new byte[createdBytes.length << 1];
        new HexEncoderDemo().encode(wrap(createdBytes), wrap(encodedBytes));
        final byte[] decodedBytes = new byte[encodedBytes.length >> 1];
        new HexDecoderDemo().decode(wrap(encodedBytes), wrap(decodedBytes));
        assertEquals(decodedBytes, createdBytes);
    }

    @Test(enabled = true, invocationCount = 128)
    public void encodeDecodeString() {
        final String created = random(current().nextInt(128));
        final String encoded = new HexEncoderDemo().encode(created);
        final String decoded = new HexDecoderDemo().decode(encoded);
        assertEquals(decoded, created);
    }

    @Test(enabled = true, invocationCount = 128)
    public void encodeDecodeAscii() {
        final String created = randomAscii(current().nextInt(128));
        final String encoded = new HexEncoderDemo().encode(created, US_ASCII);
        final String decoded = new HexDecoderDemo().decode(encoded, US_ASCII);
        assertEquals(decoded, created);
    }

    private transient final Logger logger = getLogger(getClass());

}
