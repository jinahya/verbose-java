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
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.ServiceLoader.load;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Default implementation of {@code PercentDecoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentDecoderImpl implements PercentDecoder {

    /**
     * Creates a new instance with given supplier of {@link HexDecoder}.
     *
     * @param hexDecoderSupplier the supplier
     */
    public PercentDecoderImpl(final Supplier<HexDecoder> hexDecoderSupplier) {
        super();
        this.hexDecoderSupplier = requireNonNull(hexDecoderSupplier);
    }

    /**
     * Creates a new instance.
     */
    public PercentDecoderImpl() {
        this(() -> load(HexDecoder.class).iterator().next());
    }

    @Override
    public int decodeOctet(final ByteBuffer encoded) {
        if (encoded.remaining() < 1) {
            throw new BufferUnderflowException();
        }
        final byte e = encoded.get(encoded.position()); // <2>
        if (e == 0x25) { // <2>
            if (encoded.remaining() < 3) {
                throw new BufferUnderflowException();
            }
            encoded.position(encoded.position() + 1);
            return ofNullable(hexDecoder) // <3>
                    .orElseGet(hexDecoderSupplier)
                    .decodeOctet(encoded);
        }
        encoded.position(encoded.position() + 1); // <4>
        return e;
    }

    private transient final Logger logger
            = Logger.getLogger(getClass().getName());

    private final Supplier<HexDecoder> hexDecoderSupplier;

    private HexDecoder hexDecoder;
}
