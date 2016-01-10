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
 * Constants defined in {@code RFC 3986}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see <a href="https://tools.ietf.org/html/rfc3986">Uniform Resource
 * Identifier (URI): Generic Syntax (RFC 3986)</a>
 */
public class PercentConstraints {

    public static char requireReservedCharacter(final char c) {
        if (PercentUtilities.isReservedCharacter(c)) {
            return c;
        }
        throw new IllegalArgumentException(c + " is not a reserved character");
    }

    public static char requireUnreservedCharacter(final char c) {
        if (PercentUtilities.isUnreservedCharacter(c)) {
            return c;
        }
        throw new IllegalArgumentException(
                c + " is not an unreserved character");
    }

    protected PercentConstraints() {
        super();
    }
}
