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
import java.nio.ByteBuffer;
import static java.util.Objects.requireNonNull;
import static java.util.ServiceLoader.load;
import java.util.function.Supplier;

/**
 * A class implements {@code PercentDecoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentDecoderImpl implements PercentDecoder {

    /**
     * Creates a new instance uses an instance of {@link HexDecoder}.
     *
     * @param decoderSupplier the supplier supplies an instance of
     * {@link HexDecoder}.
     */
    public PercentDecoderImpl(final Supplier<HexDecoder> decoderSupplier) {
        super();
        this.decoderSupplier = requireNonNull(
                decoderSupplier, "decoderSupplier is null");
    }

    /**
     * Creates a new instance. This constructor calls
     * {@link #PercentDecoderImpl(java.util.function.Supplier)} with a supplier
     * which loads an instance of {@link HexDecoder} through
     * {@link java.util.ServiceLoader}.
     */
    public PercentDecoderImpl() {
        this(() -> load(HexDecoder.class).iterator().next());
    }

    @Override
    public int decodeOctet(final ByteBuffer encoded) {
        final byte e = encoded.get(); // <1>
        if (e == 0x25) { // '%' <2>
            return decoder().decodeOctet(encoded); // XX
        }
        return e; // <3>
    }

    protected HexDecoder decoder() {
        if (decoder == null && (decoder = decoderSupplier.get()) == null) {
            throw new RuntimeException("supplied decoder is null");
        }
        return decoder;
    }

    private final Supplier<HexDecoder> decoderSupplier; // <1>

    private transient HexDecoder decoder; // <2>
}
