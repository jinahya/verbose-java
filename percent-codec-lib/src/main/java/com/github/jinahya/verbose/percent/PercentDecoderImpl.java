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

import com.github.jinahya.verbose.hex.HexDecoder;
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import static java.util.Objects.requireNonNull;
import java.util.function.Supplier;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * A class implements {@code PercentDecoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentDecoderImpl<T extends HexDecoder>
        implements PercentDecoder {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance uses an instance of {@link HexDecoder}.
     *
     * @param decoderSupplier the supplier supplies an instance of
     * {@link HexDecoder}.
     */
    public PercentDecoderImpl(final Supplier<T> decoderSupplier) {
        super();
        this.decoderSupplier = requireNonNull(
                decoderSupplier, "decoderSupplier is null");
    }

    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc} The {@code decodedOctet} method of
     * {@code PercentDecoderImpl} class uses an {@link HexDecoder} that
     * {@link #decoder()} method returns.
     *
     * @param encoded {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int decodeOctet(final ByteBuffer encoded) {
        // @todo: validate arguments!
        final byte e = encoded.get(); // <1>
        if (e == 0x25) { // '%' <2>
            return decoder().decodeOctet(encoded); // XX
        }
        return e; // <3>
    }

    // -------------------------------------------------------------------------
    /**
     * Returns a lazily instantiated instance of {@link HexDecoder}.
     *
     * @return an instance of {@link HexDecoder}.
     */
    protected T decoder() {
        if (decoder == null && (decoder = decoderSupplier.get()) == null) {
            throw new RuntimeException("null decoder supplied");
        }
        return decoder;
    }

    // -------------------------------------------------------------------------
    private final Supplier<T> decoderSupplier; // <1>

    private T decoder; // <2>
}
