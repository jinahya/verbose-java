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

import static java.nio.charset.StandardCharsets.US_ASCII;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collector;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class TestVectors {

    private static final String[] RFC4648 = {
        "", "",
        "f", "66",
        "fo", "666F",
        "foo", "666F6F",
        "foob", "666F6F62",
        "fooba", "666F6F6261",
        "foobar", "666F6F626172"
    };

    private static final List<byte[]> RFC4648_DECODED_BYTES
            = Arrays.stream(RFC4648)
            .map(s -> s.getBytes(US_ASCII))
            .collect(Collector.<byte[], List<byte[]>, List<byte[]>>of(
                    ArrayList::new,
                    List::add,
                    (l, r) -> {
                        l.addAll(r);
                        return l;
                    }, Collections::unmodifiableList)
            );

    static void consumeRFC4648TestVectors(
            final BiConsumer<byte[], byte[]> consumer) {
        for (final Iterator<String> i = Arrays.asList(RFC4648).iterator();
             i.hasNext();) {
            consumer.accept(i.next().getBytes(US_ASCII),
                            i.next().getBytes(US_ASCII));
        }
    }

}
