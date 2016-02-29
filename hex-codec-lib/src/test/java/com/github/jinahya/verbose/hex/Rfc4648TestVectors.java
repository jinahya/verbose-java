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

import static java.util.Arrays.asList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Test Vectors defined in RFC 4648.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="http://tools.ietf.org/html/rfc4648#section-10">10. Test Vectors
 * (RFC 4648)</a>
 */
final class Rfc4648TestVectors {

    private static final List<String> BASE64 = asList(
            "", "",
            "f", "Zg==",
            "fo", "Zm8=",
            "foo", "Zm9v",
            "foob", "Zm9vYg==",
            "fooba", "Zm9vYmE=",
            "foobar", "Zm9vYmFy"
    );

    private static final List<String> BASE32 = asList(
            "", "",
            "f", "MY======",
            "fo", "MZXQ====",
            "foo", "MZXW6===",
            "foob", "MZXW6YQ=",
            "fooba", "MZXW6YTB",
            "foobar", "MZXW6YTBOI======"
    );

    private static final List<String> BASE32HEX = asList(
            "", "",
            "f", "CO======",
            "fo", "CPNG====",
            "foo", "CPNMU===",
            "foob", "CPNMUOG=",
            "fooba", "CPNMUOJ1",
            "foobar", "CPNMUOJ1E8======"
    );

    private static final List<String> BASE16 = asList(
            "", "",
            "f", "66",
            "fo", "666F",
            "foo", "666F6F",
            "foob", "666F6F62",
            "fooba", "666F6F6261",
            "foobar", "666F6F626172"
    );

    private static void accept(final List<String> list,
                               final BiConsumer<String, String> consumer) {
        for (final Iterator<String> i = list.iterator(); i.hasNext();) {
            consumer.accept(i.next(), i.next());
        }
    }

    static void base64(final BiConsumer<String, String> consumer) {
        accept(BASE64, consumer);
    }

    static void base32(final BiConsumer<String, String> consumer) {
        accept(BASE32, consumer);
    }

    static void base32hex(final BiConsumer<String, String> consumer) {
        accept(BASE32HEX, consumer);
    }

    /**
     * Accepts pairs of base16 test vectors to given consumer.
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
