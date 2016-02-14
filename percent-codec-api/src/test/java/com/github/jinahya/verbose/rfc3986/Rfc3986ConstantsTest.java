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
package com.github.jinahya.verbose.rfc3986;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class Rfc3986ConstantsTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @Test
    public static void UNRESERVED_CHARACTERS() {
        logger.debug("UNRESERVED_CHARACTERS: {}",
                     Rfc3986Constants.RFC3986_UNRESERVED_CHARACTERS);
        logger.debug("UNRESERVED_CHARACTERS: {}",
                     Rfc3986Constants.RFC3986_UNRESERVED_CHARACTERS.stream()
                     .map(i -> (char) i.intValue()).collect(toList()));
    }
}
