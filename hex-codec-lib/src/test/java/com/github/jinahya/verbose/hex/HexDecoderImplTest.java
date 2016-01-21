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
import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * A class testing {@link HexEncoderImpl}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexDecoderImplTest {

    /**
     * Tests decoding with {@code RFC4648} test vectors.
     */
    @Test
    public void testDecodingAgainstRfc4648TestVectors() {
        Rfc4648TestVectors.base16((d, e) -> {
            final String decoded = new HexDecoderImpl().decode(e);
            assertEquals(decoded, d);
        });
    }

    @Test(invocationCount = 128)
    public void encodeCommonsDecodeVerbose() throws DecoderException {
        final byte[] created = new byte[current().nextInt(128)];
        current().nextBytes(created);
        final byte[] encoded = new Hex().encode(created);
        final byte[] decoded = new byte[encoded.length >> 1];
        new HexDecoderImpl().decode(
                ByteBuffer.wrap(encoded), ByteBuffer.wrap(decoded));
        assertEquals(decoded, created);
    }

    @Test(invocationCount = 128)
    public void encodeGuavaDecodeVerbose() {
        final String created = RandomStringUtils.random(current().nextInt(128));
        final String encoded
                = BaseEncoding.base16().encode(created.getBytes(UTF_8));
        final String decoded = new HexDecoderImpl().decode(encoded);
        assertEquals(decoded, created);
    }

}
