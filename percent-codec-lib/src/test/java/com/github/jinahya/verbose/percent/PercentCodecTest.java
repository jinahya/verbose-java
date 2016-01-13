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
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import javax.inject.Inject;
import org.apache.commons.lang3.RandomStringUtils;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
abstract class PercentCodecTest {

    protected void accept(
            final BiConsumer<PercentEncoder, PercentDecoder> consumer) {
        consumer.accept(percentEncoder(), percentDecoder());
    }

    protected <T> T apply(
            final BiFunction<PercentEncoder, PercentDecoder, T> function) {
        return function.apply(percentEncoder(), percentDecoder());
    }

    @Test(invocationCount = 128)
    public void encodeDecodeString() throws UnsupportedEncodingException {
        final String created = RandomStringUtils.random(current().nextInt(128));
        accept((e, d) -> {
            final String encoded = e.encode(created);
            final String decoded = d.decode(encoded);
            assertEquals(decoded, created);
        });
    }

    protected PercentEncoder percentEncoder() {
        return percentEncoder;
    }

    protected PercentDecoder percentDecoder() {
        return percentDecoder;
    }

    @Inject
    private PercentEncoder percentEncoder;

    @Inject
    private PercentDecoder percentDecoder;
}
