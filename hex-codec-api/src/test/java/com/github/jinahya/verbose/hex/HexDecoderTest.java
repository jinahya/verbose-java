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
import static org.apache.commons.lang3.RandomStringUtils.randomAscii;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertThrows;
import org.testng.annotations.Test;

/**
 * A class tests {@code HexEncoderDemo}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexDecoderTest extends AbstractHexDecoderTest {

    @Test
    public void decodeBuffer() {
        {
            accept(d -> assertThrows(NullPointerException.class,
                                     () -> d.decode((ByteBuffer) null)));
            final int capacity = (current().nextInt(128) >> 1) << 1;
            final ByteBuffer decoded = apply(d -> d.decode(allocate(capacity)));
            assertEquals(decoded.remaining(), capacity >> 1);
        }
        {
            accept(d -> assertThrows(NullPointerException.class,
                                     () -> d.decode(null, allocate(0))));
            accept(d -> assertThrows(NullPointerException.class,
                                     () -> d.decode(allocate(0), null)));
            final int capacity = (current().nextInt(128) >> 1) << 1;
            final ByteBuffer encoded = allocate(capacity);
            final ByteBuffer decoded = allocate(capacity >> 1);
            final int count = apply(d -> d.decode(encoded, decoded));
            assertEquals(count, decoded.position());
        }
    }

    @Test
    public void decodeString() {
        accept(d -> assertThrows(NullPointerException.class,
                                 () -> d.decode(null, UTF_8)));
        accept(d -> assertThrows(NullPointerException.class,
                                 () -> d.decode("", null)));
        {
            final int count = (current().nextInt(128) >> 1) << 1;
            final String encoded = randomAscii(count);
            final String decoded = apply(d -> d.decode(encoded, UTF_8));
            assertNotNull(decoded);
        }
        accept(d -> assertThrows(NullPointerException.class,
                                 () -> d.decode((String) null)));
        {
            final int count = (current().nextInt(128) >> 1) << 1;
            final String encoded = randomAscii(count);
            final String decoded = apply(d -> d.decode(encoded));
            assertNotNull(decoded);
        }
    }
}
