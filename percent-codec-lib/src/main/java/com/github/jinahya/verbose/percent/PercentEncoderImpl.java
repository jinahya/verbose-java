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

import com.github.jinahya.verbose.hex.HexEncoder;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import java.util.ServiceLoader;
import static java.util.ServiceLoader.load;
import java.util.function.Supplier;

/**
 * A class implementing {@code PercentEncoder}. This class uses an instance of
 * {@link HexEncoder} loaded with {@link ServiceLoader#load(java.lang.Class)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentEncoderImpl implements PercentEncoder {

    /**
     * Creates a new instance with given supplier of {@link HexEncoder}.
     *
     * @param hexEncoderSupplier the supplier
     */
    public PercentEncoderImpl(final Supplier<HexEncoder> hexEncoderSupplier) {
        super();
        this.hexEncoderSupplier = requireNonNull(hexEncoderSupplier);
    }

    /**
     * Creates a new instance. This constructor calls the constructor takes one
     * argument with a supplier supplying a {@link HexEncoder} loaded via
     * service provider interface.
     */
    public PercentEncoderImpl() {
        this(() -> load(HexEncoder.class).iterator().next()); // <1>
    }

    @Override
    public void encodeOctet(final int decoded, final ByteBuffer encoded) {
        if (encoded.remaining() < 1) {
            throw new BufferOverflowException();
        }
        if ((decoded >= 0x30 && decoded <= 0x39) // digit
            || (decoded >= 0x41 && decoded <= 0x5A) // upper case alpha
            || (decoded >= 0x61 && decoded <= 0x7A) // lower case alpha
            || decoded == 0x2D // '-'
            || decoded == 0x5F // '_'
            || decoded == 0x2E // '.'
            || decoded == 0x7E) { // '~'
            encoded.put((byte) decoded); // <1>
            return;
        }
        if (encoded.remaining() < 3) {
            throw new BufferOverflowException();
        }
        encoded.put((byte) 0x25); // '%'  // <2>
        ofNullable(hexEncoder) // <3>
                .orElseGet(hexEncoderSupplier)
                .encodeOctet(decoded, encoded);
    }

    private final Supplier<HexEncoder> hexEncoderSupplier; // <1>

    private HexEncoder hexEncoder; // <1>
}
