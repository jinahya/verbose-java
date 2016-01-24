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

import static com.google.common.base.Charsets.US_ASCII;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.nio.ByteBuffer.wrap;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class ClasFileTest {

    @Test
    public void cafebabe() throws IOException {
        final Class<?> type = getClass();
        final String name = type.getSimpleName() + ".class";
        try (InputStream input = type.getResourceAsStream(name)) {
            assert input != null : "failed to load resource: " + name;
            final byte[] decoded = new byte[4];
            new DataInputStream(input).readFully(decoded);
            final byte[] encoded = new byte[decoded.length << 1];
            new HexEncoderImpl().encode(wrap(decoded), wrap(encoded));
            System.out.println("magic: " + new String(encoded, US_ASCII));
        }
    }
}
