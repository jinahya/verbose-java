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
import static org.apache.commons.lang3.RandomStringUtils.randomAscii;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
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
            assertThrows(NullPointerException.class,
                         () -> decoder().decode((ByteBuffer) null));
            final int capacity = (current().nextInt(128) >> 1) << 1;
            final ByteBuffer decoded
                    = decoder().decode(ByteBuffer.allocate(capacity));
            assertEquals(decoded.remaining(), capacity >> 1);
        }
        {
            assertThrows(NullPointerException.class,
                         () -> decoder().decode(null, (ByteBuffer) null));
            assertThrows(NullPointerException.class,
                         () -> decoder().decode(mock(ByteBuffer.class), null));
            final int capacity = (current().nextInt(128) >> 1) << 1;
            decoder().decode(ByteBuffer.allocate(capacity),
                             ByteBuffer.allocate(capacity >> 1));
        }
    }

    @Test
    public void decodeString() {
        {
            assertThrows(NullPointerException.class,
                         () -> decoder().decode(null, mock(Charset.class)));
            assertThrows(NullPointerException.class,
                         () -> decoder().decode("", null));
            final int count = (current().nextInt(128) >> 1) << 1;
            final String encoded = randomAscii(count);
            final String decoded = decoder().decode(encoded, UTF_8);
        }
        {
            assertThrows(NullPointerException.class,
                         () -> decoder().decode((String) null));
            final int count = (current().nextInt(128) >> 1) << 1;
            final String encoded = random(count);
            final String decoded = decoder().decode(encoded);
        }
    }
}
