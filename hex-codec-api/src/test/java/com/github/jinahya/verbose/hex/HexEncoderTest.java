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
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
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
        {
            assertThrows(NullPointerException.class,
                         () -> encoder().encode((ByteBuffer) null));
            final int capacity = current().nextInt(128);
            final ByteBuffer decoded = ByteBuffer.allocate(capacity);
            final ByteBuffer encoded = encoder().encode(decoded);
            assertEquals(encoded.remaining(), decoded.capacity() << 1);
        }
        {
            assertThrows(NullPointerException.class,
                         () -> encoder().encode(ByteBuffer.allocate(0), null));
            assertThrows(NullPointerException.class,
                         () -> encoder().encode(null, ByteBuffer.allocate(0)));
            final int capacity = current().nextInt(128);
            final ByteBuffer decoded = ByteBuffer.allocate(capacity);
            final ByteBuffer encoded = ByteBuffer.allocate(capacity << 1);
            final int count = encoder().encode(decoded, encoded);
            assertEquals(count, decoded.capacity());
        }
    }

    @Test
    public void encodeString() {
        {
            assertThrows(NullPointerException.class,
                         () -> encoder().encode(null, mock(Charset.class)));
            assertThrows(NullPointerException.class,
                         () -> encoder().encode("", null));
            final int count = current().nextInt(128);
            final String decoded = random(count);
            final String encoded = encoder().encode(decoded, UTF_8);
        }
        {
            assertThrows(NullPointerException.class,
                         () -> encoder().encode((String) null));
            final int count = current().nextInt(128);
            final String decoded = random(count);
            final String encoded = encoder().encode(decoded);
        }
    }
}
