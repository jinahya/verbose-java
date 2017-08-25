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
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import static java.util.Objects.requireNonNull;
import java.util.function.Supplier;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * A class implementing {@code PercentEncoder}. This class internally uses an
 * instance of {@link HexEncoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> hex encoder type parameter
 */
public class PercentEncoderImpl<T extends HexEncoder>
        implements PercentEncoder {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance with an supplier of {@link HexEncoder}.
     *
     * @param encoderSupplier the supplier for a {@link HexEncoder}.
     */
    public PercentEncoderImpl(final Supplier<T> encoderSupplier) {
        super();
        this.encoderSupplier = requireNonNull(
                encoderSupplier, "encoderSupplier is null");
    }

    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc} The {@code #encodeOctet(int, java.nio.ByteBuffer)} method
     * of {@code PercentEncoderImpl} class uses the {@link HexEncoder} instance
     * {@link #encoder()} method returns.
     *
     * @param decoded {@inheritDoc}
     * @param encoded {@inheritDoc}
     */
    @Override
    public void encodeOctet(final int decoded, final ByteBuffer encoded) {
        // @todo: validate arguments!
        if ((decoded >= 0x30 && decoded <= 0x39) // digit
            || (decoded >= 0x41 && decoded <= 0x5A) // alpha, upper case
            || (decoded >= 0x61 && decoded <= 0x7A) // alpha, lower case
            || decoded == 0x2D // '-'
            || decoded == 0x5F // '_'
            || decoded == 0x2E // '.'
            || decoded == 0x7E) { // '~'
            encoded.put((byte) decoded); // <1>
            return;
        }
        encoded.put((byte) 0x25); // '%'  // <2>
        encoder().encodeOctet(decoded, encoded);
    }

    // ----------------------------------------------------------------- encoder
    /**
     * Returns a lazily obtained instance of {@link HexEncoder}.
     *
     * @return an instance of {@link HexEncoder}.
     */
    protected T encoder() {
        if (encoder == null && (encoder = encoderSupplier.get()) == null) {
            throw new RuntimeException("null encoder supplied");
        }
        return encoder;
    }

    // -------------------------------------------------------------------------
    private final Supplier<T> encoderSupplier; // <1>

    private T encoder; // <2>
}
