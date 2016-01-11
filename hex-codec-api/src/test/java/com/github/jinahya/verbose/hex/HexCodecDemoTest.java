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
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexCodecDemoTest {

    @Test(invocationCount = 128)
    public void encodeDecodeString() {
        final String created = RandomStringUtils.random(current().nextInt(2));
        final String encoded = new HexEncoderDemo().encode(created);
        final String decoded = new HexDecoderDemo().decode(encoded);
        assertEquals(decoded, created);
    }

    private transient final Logger logger = getLogger(getClass());

}
