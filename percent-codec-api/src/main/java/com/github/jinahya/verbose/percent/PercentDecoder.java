package com.github.jinahya.verbose.percent;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * An interface for percent-decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
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

    /**
     * Decodes given string and returns a String encoded with specified
     * character set.
     *
     * @param encoded the string to decode
     * @param charset the character set to decode translated byte array into the
     * output String.
     *
     * @return decoded String
     */
    default String decode(final String encoded, final Charset charset) {
        final byte[] encodedBytes = encoded.getBytes(US_ASCII);
        final byte[] decodedBytes = new byte[encodedBytes.length];
        final ByteBuffer encodedBuffer = ByteBuffer.wrap(encodedBytes);
        final ByteBuffer decodedBuffer = ByteBuffer.wrap(decodedBytes);
        decode(encodedBuffer, decodedBuffer);
        return new String(decodedBytes, 0, decodedBuffer.position(), charset);
    }
}
