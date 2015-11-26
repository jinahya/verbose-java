package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.US_ASCII;

public interface HexEncoder {

    void encodeSingle(int decoded, ByteBuffer encoded);

    /**
     * Encodes all remaining bytes from given input buffer and puts results to
     * specified output buffer.
     *
     * @param decoded the input buffer
     * @param encoded the output buffer
     */
    default void encode(final ByteBuffer decoded, final ByteBuffer encoded) {
        while (decoded.hasRemaining()) {
            encodeSingle(decoded.get(), encoded);
        }
    }

    default ByteBuffer encode(final ByteBuffer decoded) {
        final ByteBuffer encoded = ByteBuffer.allocate(decoded.remaining() * 2);
        encode(decoded, encoded);
        encoded.flip();
        return encoded;
    }

    default String encode(final String decoded, final Charset charset) {
        final byte[] decodedBytes = decoded.getBytes(charset);
        final byte[] encodedBytes = new byte[decodedBytes.length << 1];
        encode(ByteBuffer.wrap(decodedBytes), ByteBuffer.wrap(encodedBytes));
        return new String(encodedBytes, US_ASCII);
    }
}
