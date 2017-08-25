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
 * A mock of {@link PercentDecoder}.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
class PercentDecoderMock implements PercentDecoder {

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
