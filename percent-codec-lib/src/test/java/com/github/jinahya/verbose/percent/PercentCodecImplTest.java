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

import com.github.jinahya.verbose.hex.HexDecoderImpl;
import com.github.jinahya.verbose.hex.HexEncoderImpl;
import static com.github.jinahya.verbose.util.RsUtils.randomUsAscii;
import static com.github.jinahya.verbose.util.RsUtils.randomUtf8;
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import java.nio.charset.StandardCharsets;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import javax.inject.Inject;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * A test class for both {@link PercentEncoderImpl} and
 * {@link PercentDecoderImpl}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = {PercentEncoderImplModule.class,
                  PercentDecoderImplModule.class})
public class PercentCodecImplTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    /**
     * Tests {@link HexEncoderImpl#encode(java.nio.ByteBuffer)} and
     * {@link HexDecoderImpl#decode(java.nio.ByteBuffer)}.
     */
    @Test(invocationCount = 128)
    public void encodeDecodeBuffer() {
        final ByteBuffer created = allocate(current().nextInt(1024));
        current().nextBytes(created.array());
        final ByteBuffer encoded = encoder.encode(created);
        final ByteBuffer decoded = decoder.decode(encoded);
        created.flip();
        assertEquals(decoded, created);
    }

    /**
     * Tests
     * {@link HexEncoderImpl#encode(java.lang.String, java.nio.charset.Charset)}
     * and
     * {@link HexDecoderImpl#decode(java.lang.String, java.nio.charset.Charset)}
     * with {@link StandardCharsets#UTF_8}.
     */
    @Test(invocationCount = 128)
    public void encodeDecodeUtf8() {
        final String created = randomUtf8(current().nextInt(1024));
        final String encoded = encoder.encode(created, UTF_8);
        final String decoded = decoder.decode(encoded, UTF_8);
        assertEquals(decoded, created);
    }

    /**
     * Tests
     * {@link HexEncoderImpl#encode(java.lang.String, java.nio.charset.Charset)}
     * and
     * {@link HexDecoderImpl#decode(java.lang.String, java.nio.charset.Charset)}
     * with {@link StandardCharsets#US_ASCII}.
     */
    @Test(invocationCount = 128)
    public void encodeDecodeAscii() {
        final String created = randomUsAscii(current().nextInt(1024));
        final String encoded = encoder.encode(created, US_ASCII);
        final String decoded = decoder.decode(encoded, US_ASCII);
        assertEquals(decoded, created);
    }

    @Inject
    private PercentEncoder encoder;

    @Inject
    private PercentDecoder decoder;
}
