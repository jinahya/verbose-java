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

import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.IntStream.range;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertThrows;
import org.testng.annotations.Test;

/**
 * A class testing {@link HexDecoderChannel}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexDecoderChannelTest {

    /**
     * Asserts the constructor throws an {@code IllegalArgumentException} for
     * invalid {@code capacity}.
     */
    @Test
    public void constructWithInvalidCapacity() {
        range(0, 128)
                .map(i -> current().nextInt())
                .map(c -> c > 0 ? c >> 31 : c)
                .forEach(c -> assertThrows(
                        IllegalArgumentException.class,
                        () -> new HexDecoderChannel(
                                null, null, c, current().nextBoolean()))
                );
    }

    private transient final Logger logger = getLogger(getClass());
}
