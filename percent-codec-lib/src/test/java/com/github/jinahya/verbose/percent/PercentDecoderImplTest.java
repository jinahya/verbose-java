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
package com.github.jinahya.verbose.percent;

import com.buck.common.codec.Codec;
import com.buck.common.codec.CodecEncoder;
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
import static java.util.Arrays.copyOf;
import static java.util.concurrent.ThreadLocalRandom.current;
import javax.inject.Inject;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = PercentDecoderImplModule.class)
public class PercentDecoderImplTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @Test(invocationCount = 128)
    public void encodeRbuckDecodeVerbose() {
        final byte[] created; // <1>
        {
            created = new byte[current().nextInt(1024)];
            current().nextBytes(created);
        }
        final byte[] encoded; // <2>
        {
            final Codec codec = Codec.forName("percent-encoded");
            final CodecEncoder encoder = codec.newEncoder();
            encoded = encoder.encode(created);
        }
        final byte[] decoded; // <3>
        {
            final ByteBuffer buffer = allocate(encoded.length);
            decoder.decode(wrap(encoded), buffer);
            decoded = copyOf(buffer.array(), buffer.position());
        }
        assertEquals(decoded, created); // <4>
    }

    @Inject
    private PercentDecoder decoder;
}
