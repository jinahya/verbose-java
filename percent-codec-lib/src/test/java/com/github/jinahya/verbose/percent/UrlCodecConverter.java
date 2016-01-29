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

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class UrlCodecConverter {

    static final String EXAMPLE_DECODED = "The string ü@foo-bar";

    static final String EXAMPLE_ENCODED = "The+string+%C3%BC%40foo-bar";

    static String toPercentEncoded(final String urlEncoded) {
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

    private UrlCodecConverter() {
        super();
    }
}
