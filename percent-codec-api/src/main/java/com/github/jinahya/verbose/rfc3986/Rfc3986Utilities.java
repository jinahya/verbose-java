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

/**
 * A utility class for checking compatibilities against RFC3986.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see  <a href="http://tools.ietf.org/html/rfc3986">RFC3986</a>.
 */
public final class Rfc3986Utilities {

    /**
     * Checks if given byte is an unreserved character.
     *
     * @param decoded the byte to check
     * @return {@code true} if given byte is an unreserved character.
     * @see <a href="http://tools.ietf.org/html/rfc3986#page-13">2.3. Unreserved
     * Characters</a>
     */
    public static boolean isRfc3986UnsafeCharacter(final int decoded) {
        return Rfc3986Constants.RFC3986_UNRESERVED_CHARACTERS.contains(decoded);
    }

    /**
     * Checks if given byte is an unreserved character. An
     * {@code IllegalArgumentException} will be throws if given byte is not an
     * unreserved character.
     *
     * @param decoded the byte to check
     * @return given byte
     * @see <a href="http://tools.ietf.org/html/rfc3986#page-13">2.3. Unreserved
     * Characters</a>
     */
    public static int requireRfc3986UnsafeCharacter(final int decoded) {
        if (!isRfc3986UnsafeCharacter(decoded)) {
            throw new IllegalArgumentException(
                    "not an unsafe character: " + decoded);
        }
        return decoded;
    }

    private Rfc3986Utilities() {
        super();
    }
}
