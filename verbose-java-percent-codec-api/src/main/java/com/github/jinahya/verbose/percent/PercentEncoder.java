package com.github.jinahya.verbose.percent;

import java.nio.ByteBuffer;

public interface PercentEncoder {

    void encodeSingle(int decoded, ByteBuffer encoded);

    default void encode(final ByteBuffer decoded, final ByteBuffer encoded) {
        while (decoded.hasRemaining()) {
            encodeSingle(decoded.get(), encoded);
        }
    }

    default ByteBuffer encode(final ByteBuffer decoded) {
        final ByteBuffer encoded = ByteBuffer.allocate(decoded.remaining() * 3);
        encode(decoded, encoded);
        encoded.flip();
        return encoded;
    }
}
