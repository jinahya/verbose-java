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
package com.github.jinahya.verbose.hex;

import com.google.common.io.BaseEncoding;
import static java.nio.ByteBuffer.wrap;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.codec.binary.Hex;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * A class testing {@link HexEncoderImpl}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexDecoderImplTest {

    /**
     * Tests decoding using {@code RFC4648} test vectors.
     */
    @Test
    public void decodeVerboseCompareRfc4648TestVector() {
        Rfc4648TestVectors.base16(
                (d, e) -> assertEquals(new HexDecoderImpl().decode(e), d)
        );
    }

    /**
     * Cross-checks {@link HexDecoderImpl} against {@link Hex}.
     */
    @Test(invocationCount = 128)
    public void encodeCommonsDecodeVerbose() {
        final byte[] created = new byte[current().nextInt(128)]; // <1>
        current().nextBytes(created);
        final byte[] encoded = new Hex().encode(created); // <2>
        final byte[] decoded = new byte[encoded.length >> 1]; // <3>
        new HexDecoderImpl().decode(wrap(encoded), wrap(decoded));
        assertEquals(decoded, created); // <4>
    }

    /**
     * Cross-checks {@link HexDecoderImpl} against {@link BaseEncoding}.
     */
    @Test(invocationCount = 128)
    public void encodeGuavaDecodeVerbose() {
        final byte[] created = new byte[current().nextInt(128)];
        current().nextBytes(created);
        final byte[] encoded // <1>
                = BaseEncoding.base16().encode(created).getBytes(US_ASCII);
        final byte[] decoded = new byte[encoded.length >> 1];
        new HexDecoderImpl().decode(wrap(encoded), wrap(decoded));
        assertEquals(decoded, created);
    }

}
