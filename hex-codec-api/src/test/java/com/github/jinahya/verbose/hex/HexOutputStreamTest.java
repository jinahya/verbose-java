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
import java.io.OutputStream;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexOutputStreamTest extends AbstractHexEncoderTest {

    @Test
    public void lazyOut() throws IOException {
        final OutputStream out = null;
        final HexEncoder enc = encoder();
        final OutputStream hos = new HexOutputStream(out, enc) {
            @Override
            public void write(final int b) throws IOException {
                if (out == null) {
                    out = new ByteArrayOutputStream();
                }
                super.write(b);
            }
        };
        hos.write(current().nextInt());
    }

    @Test
    public void lazyEnc() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final HexEncoder enc = null;
        final OutputStream hos = new HexOutputStream(out, enc) {
            @Override
            public void write(final int b) throws IOException {
                if (enc == null) {
                    enc = encoder();
                }
                super.write(b);
            }
        };
        hos.write(current().nextInt());
    }

    @Test
    public void lazyOutEnc() throws IOException {
        final OutputStream out = null;
        final HexEncoder enc = null;
        final HexOutputStream hos = new HexOutputStream(out, enc) {
            @Override
            public void write(final int b) throws IOException {
                if (out == null) {
                    out = new ByteArrayOutputStream();
                }
                if (enc == null) {
                    enc = encoder();
                }
                super.write(b);
            }
        };
        hos.write(current().nextInt());
    }

    private transient final Logger logger = getLogger(getClass());

}
