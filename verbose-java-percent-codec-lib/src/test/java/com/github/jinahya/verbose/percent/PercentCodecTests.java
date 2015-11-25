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
final class PercentCodecTests {

    static String fromURLEncoded(final String urlEncoded) {
        return urlEncoded
                .replaceAll("\\*", "%2A")
                .replaceAll("%7E", "~")
                .replaceAll("\\+", "%20");
    }

    static String toUrlEncoded(final String percentEncoded) {
        return percentEncoded
                .replaceAll("%2A", "\\*")
                .replaceAll("~", "%7E")
                .replaceAll("%20", "\\+");
    }

    private PercentCodecTests() {
        super();
    }
}
