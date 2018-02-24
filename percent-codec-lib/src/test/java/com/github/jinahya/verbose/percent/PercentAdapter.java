/*
 * Copyright 2017 Jin Kwon &lt;onacit at gmail.com&gt;.
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
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
class PercentAdapter {

    public String encode(final String s, final String enc)
            throws UnsupportedEncodingException {
        final String e = URLEncoder.encode(s, enc);
        return e.replace("*", "%2A").replace("+", "%20").replace("%2B", "+");
    }

    public String decode(final String s, final String enc)
            throws UnsupportedEncodingException {
        final String e = s.replace("%2A", "*").replace("%20", "+").replace("%2B", "+");
        return URLDecoder.decode(e, enc);
    }
}