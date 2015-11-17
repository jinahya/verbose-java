package com.github.jinahya.verbose.percent;

import java.nio.ByteBuffer;

public interface PercentDecoder {

    int decodeSingle(ByteBuffer encoded);

    default void decode(final ByteBuffer encoded, final ByteBuffer decoded) {
        while (encoded.hasRemaining()) {
            decoded.put((byte) decodeSingle(encoded));
        }
    }

    default ByteBuffer decode(final ByteBuffer encoded) {
        final ByteBuffer decoded = ByteBuffer.allocate(encoded.remaining());
        decode(encoded, decoded);
        decoded.flip();
        return decoded;
    }
}
