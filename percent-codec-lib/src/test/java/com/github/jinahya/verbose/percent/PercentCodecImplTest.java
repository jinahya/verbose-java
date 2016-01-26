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
package com.github.jinahya.verbose.percent;

import java.io.UnsupportedEncodingException;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.concurrent.ThreadLocalRandom.current;
import javax.inject.Inject;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAscii;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = {PercentEncoderImplModule.class,
                  PercentDecoderImplModule.class})
public class PercentCodecImplTest {

    @Test(invocationCount = 128)
    public void encodeDecodeString() throws UnsupportedEncodingException {
        final String created = random(current().nextInt(128));
        final String encoded = encoder.encode(created);
        final String decoded = decoder.decode(encoded);
        assertEquals(decoded, created);
    }

    @Test(invocationCount = 128)
    public void encodeDecodeAscii() throws UnsupportedEncodingException {
        final String created = randomAscii(current().nextInt(128));
        final String encoded = encoder.encode(created, US_ASCII);
        final String decoded = decoder.decode(encoded, US_ASCII);
        assertEquals(decoded, created);
    }

    @Inject
    private PercentEncoder encoder;

    @Inject
    private PercentDecoder decoder;
}
