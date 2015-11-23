package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.US_ASCII;

public interface HexDecoder {

    int decodeSingle(ByteBuffer encoded);

    default void decode(final ByteBuffer encoded, final ByteBuffer decoded) {
        while (encoded.hasRemaining()) {
            decoded.put((byte) decodeSingle(encoded));
        }
    }

    default ByteBuffer decode(final ByteBuffer encoded) {
        final ByteBuffer decoded = ByteBuffer.allocate(encoded.remaining() / 2);
        decode(encoded, decoded);
        decoded.flip();
        return decoded;
    }

    default String decode(final String encoded, final Charset charset) {
        final byte[] encodedBytes = encoded.getBytes(US_ASCII);
        final byte[] decodedBytes = new byte[encodedBytes.length >> 1];
        decode(ByteBuffer.wrap(encodedBytes), ByteBuffer.wrap(decodedBytes));
        return new String(decodedBytes, charset);
    }
}
