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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class Rfc3986 {

    public static final List<Character> UNRESERVED_CHARACTERS;

    static {
        final List<Character> list = new ArrayList<>(56);
        for (char i = 0x30; i <= 0x39; i++) { // digit
            list.add(i);
        }
        for (char i = 0x41; i <= 0x5A; i++) { // upper case alpha
            list.add(i);
        }
        for (char i = 0x61; i <= 0x7A; i++) { // lower case alpha
            list.add(i);
        }
        list.add((char) 0x2D); // '-'
        list.add((char) 0x2E); // '.'
        list.add((char) 0x5F); // '_'
        list.add((char) 0x7E); // '~'
        UNRESERVED_CHARACTERS = Collections.unmodifiableList(list);
    }

    public static final Pattern RESERVED_CHARACTERS_PATTERN
            = Pattern.compile("[0-9A-Za-z-\\._~]+");

    public static List<Character> RESERVED_CHARACTERS = Arrays.asList(
            ':', ',', '?', '#', '[', ']', '@',
            '!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=');

    public static boolean isUnreservedCharacter(final int c) {
        return (c >= 0x30 && c <= 0x39) // digits
               || (c >= 0x41 && c <= 0x5A) // upper case alpha
               || (c >= 0x61 && c <= 0x7A) // lower case alpha
               || c == 0x2D // '-'
               || c == 0x5F // '_'
               || c == 0x2E // '.'
               || c == 0x7E; // '~'
    }

    public static int requireUnreservedCharacter(final int c) {
        if (isUnreservedCharacter(c)) {
            return c;
        }
        throw new IllegalArgumentException(
                c + " is not a rfc3986 reserved character");
    }

    private Rfc3986() {
        super();
    }
}
