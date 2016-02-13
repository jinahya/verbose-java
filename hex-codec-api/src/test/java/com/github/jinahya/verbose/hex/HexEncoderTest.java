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
import static java.nio.ByteBuffer.allocate;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertThrows;
import org.testng.annotations.Test;

/**
 * A class tests {@code HexEncoderDemo}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexEncoderTest extends AbstractHexEncoderTest {

    @Test
    public void encodeBuffer() {
        accept(e -> assertThrows(NullPointerException.class,
                                 () -> e.encode((ByteBuffer) null)));
        {
            final ByteBuffer decoded = allocate(current().nextInt(128));
            final ByteBuffer encoded = apply(e -> e.encode(decoded));
            assertEquals(encoded.remaining(), decoded.capacity() << 1);
        }
        accept(e -> assertThrows(NullPointerException.class,
                                 () -> e.encode(allocate(1), null)));
        accept(e -> assertThrows(NullPointerException.class,
                                 () -> e.encode(null, allocate(2))));
        {
            final int decodedCapacity = current().nextInt(128);
            final int encodedCapacity
                    = decodedCapacity == 0
                      ? 0 : current().nextInt(decodedCapacity << 1);
            final ByteBuffer decoded = allocate(decodedCapacity);
            final ByteBuffer encoded = allocate(encodedCapacity);
            final int count = apply(e -> e.encode(decoded, encoded));
            assertEquals(count, decoded.position());
        }
    }

    @Test
    public void encodeString() {
        accept(e -> assertThrows(NullPointerException.class,
                                 () -> e.encode(null, UTF_8)));
        accept(e -> assertThrows(NullPointerException.class,
                                 () -> e.encode("", null)));
        {
            final String decoded = random(current().nextInt(128));
            final String encoded = apply(e -> e.encode(decoded, UTF_8));
            assertNotNull(encoded);
        }
        accept(e -> assertThrows(NullPointerException.class,
                                 () -> e.encode((String) null)));
        {
            final String decoded = random(current().nextInt(128));
            final String encoded = apply(e -> e.encode(decoded));
            assertNotNull(encoded);
        }
    }
}
