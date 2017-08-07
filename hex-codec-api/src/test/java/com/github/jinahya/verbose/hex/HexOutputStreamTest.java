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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.testng.annotations.Test;

/**
 * A class testing {@link HexOutputStream} class.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexOutputStreamTest extends AbstractHexEncoderTest {

    @Test
    public void testWrite() throws IOException {
        try (final HexOutputStream hos = apply(
                e -> new HexOutputStream(new ByteArrayOutputStream(), e))) {
            hos.write(current().nextInt());
        }
    }
}
