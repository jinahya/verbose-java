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
package com.github.jinahya.verbose.percent;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * A demonstrative implementation.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class PercentDecoderDemo implements PercentDecoder {

    /**
     * {@inheritDoc} The {@code decodeOctet(ByteBuffer)} method of
     * {@code PercentDecoderDemo} class tries to increment
     * {@code encoded.posotion} by {@code 1} or {@code 3} and throws an instance
     * of {@code BufferUnderflowException} if failed.
     *
     * @param encoded {@inheritDoc}
     * @return {@code 0}
     */
    @Override
    public int decodeOctet(final ByteBuffer encoded) {
        try {
            encoded.position(
                    encoded.position() + (current().nextBoolean() ? 1 : 3));
        } catch (final IllegalArgumentException iae) {
            throw new BufferUnderflowException();
        }
        return 0;
    }
}
