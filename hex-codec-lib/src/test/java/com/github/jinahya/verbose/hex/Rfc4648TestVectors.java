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
package com.github.jinahya.verbose.hex;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class Rfc4648TestVectors {

    private static final List<String> BASE16 = Arrays.asList(
            "", "",
            "f", "66",
            "fo", "666F",
            "foo", "666F6F",
            "foob", "666F6F62",
            "fooba", "666F6F6261",
            "foobar", "666F6F626172"
    );

    /**
     * Accepts pairs of base 16 test vectors to given consumer.
     *
     * @param consumer the consumer accepts a pair of a decoded value and an
     * encoded value.
     */
    static void base16(final BiConsumer<String, String> consumer) {
        for (final Iterator<String> i = BASE16.iterator(); i.hasNext();) {
            consumer.accept(i.next(), i.next());
        }
    }

    private Rfc4648TestVectors() {
        super();
    }
}
