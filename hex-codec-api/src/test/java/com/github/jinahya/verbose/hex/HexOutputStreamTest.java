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

import java.io.IOException;
import java.io.OutputStream;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 * A class testing {@link HexOutputStream} class.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexOutputStreamTest extends AbstractHexEncoderTest {

    /**
     * Tests lazy initialization of {@link HexOutputStream#out} and
     * {@link HexOutputStream#enc}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testWrite() throws IOException {
        final OutputStream out = null;
        final HexEncoder enc = null;
        final HexOutputStream hos = new HexOutputStream(out, enc) {
            @Override
            public void write(final int b) throws IOException {
                if (out == null) {
                    out = mock(OutputStream.class);
                    doNothing().when(out).write(anyInt());
                }
                if (enc == null) {
                    enc = encoder();
                }
                super.write(b);
            }
        };
        for (int i = 0; i < 128; i++) {
            hos.write(current().nextInt());
        }
    }

    private transient final Logger logger = getLogger(getClass());

}
