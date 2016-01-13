package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * And interface for hex decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@FunctionalInterface
public interface HexDecoder {

    /**
     * Decodes two encoded hex characters from given byte buffer and returns the
     * decoded octet.
     *
     * @param encoded the byte buffer contains encoded characters.
     * @return a decoded octet.
     */
    int decodeOctet(ByteBuffer encoded);

    default void decode(final ByteBuffer encoded, final ByteBuffer decoded) {
        while (encoded.hasRemaining()) {
            decoded.put((byte) decodeOctet(encoded));
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

    default String decode(final String encoded) {
        return decode(encoded, UTF_8);
    }
}
