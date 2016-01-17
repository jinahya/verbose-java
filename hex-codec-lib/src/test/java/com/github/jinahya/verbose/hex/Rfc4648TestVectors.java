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

import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.function.BiConsumer;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class Rfc4648TestVectors {

    private static final String[] BASE16 = {
        "", "",
        "f", "66",
        "fo", "666F",
        "foo", "666F6F",
        "foob", "666F6F62",
        "fooba", "666F6F6261",
        "foobar", "666F6F626172"
    };

    static void base16Strings(final BiConsumer<String, String> consumer) {
        for (int i = 0; i < BASE16.length; i++) {
            consumer.accept(BASE16[i], BASE16[++i]);
        }
    }

    static void base16Bytes(final BiConsumer<byte[], byte[]> consumer) {
        base16Strings((d, e) -> {
            consumer.accept(d.getBytes(UTF_8), e.getBytes(UTF_8));
        });
    }

    private Rfc4648TestVectors() {
        super();
    }
}
