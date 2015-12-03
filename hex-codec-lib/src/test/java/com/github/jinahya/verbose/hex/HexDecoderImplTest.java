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

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.testng.annotations.Test;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexDecoderImplTest {

    @Test
    public void decodeForRFC4648TestVectors() {
        TestVectors.consumeRFC4648TestVectors((d, e) -> {
            final ByteBuffer decoded = ByteBuffer.wrap(d);
            new HexDecoderImpl().decode(ByteBuffer.wrap(e), decoded);
            decoded.flip();
            assertTrue(decoded.equals(ByteBuffer.wrap(d)));
        });
    }

    private transient final Logger logger = getLogger(getClass());
}