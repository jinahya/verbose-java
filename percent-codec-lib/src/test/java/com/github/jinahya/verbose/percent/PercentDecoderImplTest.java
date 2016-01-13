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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.lang3.RandomStringUtils;
import org.jvnet.testing.hk2testng.HK2;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@HK2(binders = PercentDecoderImplBinder.class)
public class PercentDecoderImplTest extends PercentDecoderTest {

    @Test
    public void testExampleFromURLEncoderDocumentation()
            throws UnsupportedEncodingException {
        final String expected = "The string ü@foo-bar";
        final String encoded = PercentCodecTests.fromURLEncoded(
                "The+string+%C3%BC%40foo-bar");
        final String actual = new PercentDecoderImpl().decode(
                encoded, StandardCharsets.UTF_8);
        assertEquals(actual, expected);
    }

    @Test(invocationCount = 128)
    public void testDecodingAgainstURLEncoder()
            throws UnsupportedEncodingException {
        final Charset charset = StandardCharsets.UTF_8;
        final String expected
                = RandomStringUtils.random(current().nextInt(128));
        String encoded = PercentCodecTests.fromURLEncoded(
                URLEncoder.encode(expected, charset.name()));
        final String actual = new PercentDecoderImpl().decode(encoded, charset);
        assertEquals(actual, expected);
    }
}
