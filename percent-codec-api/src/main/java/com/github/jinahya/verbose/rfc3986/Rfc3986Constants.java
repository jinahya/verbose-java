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

import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.List;

/**
 * Constants defined in RFC3986.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see  <a href="http://tools.ietf.org/html/rfc3986">RFC3986</a>.
 */
public final class Rfc3986Constants {

    /**
     * Unreserved characters defined in RFC3986.
     *
     * @see <a href="http://tools.ietf.org/html/rfc3986#page-13">2.3. Unreserved
     * Characters</a>
     */
    public static final List<Integer> RFC3986_UNRESERVED_CHARACTERS;

    static {
        final List<Integer> list = new ArrayList<>(66);
        for (int d = 0x30; d <= 0x39; d++) { // digit
            list.add(d);
        }
        for (int A = 0x41; A <= 0x5A; A++) { // alpha, upper case
            list.add(A);
        }
        for (int a = 0x61; a <= 0x7A; a++) { // alpha, lower case
            list.add(a);
        }
        list.add(0x2D); // '-'
        list.add(0x2E); // '.'
        list.add(0x5F); // '_'
        list.add(0x7E); // '~'
        RFC3986_UNRESERVED_CHARACTERS = unmodifiableList(list);
    }

    private Rfc3986Constants() {
        super();
    }
}
