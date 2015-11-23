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
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentEncoderImplTest {

    @Test
    public void testExampleFromURLEncoderDocumentation()
            throws UnsupportedEncodingException {

        final String decoded = "The string ü@foo-bar";
        final String expected = PercentCodecTests.fromURLEncoded(URLEncoder.encode(decoded, "UTF-8"));
        final String actual = new PercentEncoderImpl().encode(decoded, StandardCharsets.UTF_8);
        assertEquals(actual, expected);
    }

    @Test(invocationCount = 1024)
    public void testEncodingAgainstURLEncoder()
            throws UnsupportedEncodingException {
        final Charset charset = StandardCharsets.UTF_8;
        final String decoded = RandomStringUtils.random(current().nextInt(128));
        String expected = PercentCodecTests.fromURLEncoded(URLEncoder.encode(decoded, charset.name()));
        final String actual = new PercentEncoderImpl().encode(decoded, charset);
        assertEquals(actual, expected);
    }
}
