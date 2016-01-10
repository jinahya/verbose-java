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

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Constants defined in {@code RFC 3986}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc3986">Uniform Resource
 * Identifier (URI): Generic Syntax (RFC 3986)</a>
 */
public class PercentConstants {

    /**
     * Reserved characters defined in {@code RFC 3986}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc3986#section-2.2">2.2.
     * Reserved Characters (RFC 3986)</a>
     */
    public static final List<Character> RESERVED_CHARACTERS = Arrays.asList(
            ':', ',', '?', '#', '[', ']', '@', // gen-delims
            '!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=' // sub-delims
    );

    /**
     * Unreserved characters defined in {@code RFC 3986}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc3986#section-2.3">2.3.
     * Unreserved Characters (RFC 3986)</a>
     */
    public static final List<Character> UNRESERVED_CHARACTERS = Arrays.asList(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '-', '_', '.', '~'
    );

    public static final Pattern UNRESERVED_CHARACTERS_PATTERN
            = Pattern.compile("[0-9A-Za-z-\\._~]+");

    protected PercentConstants() {
        super();
    }
}
