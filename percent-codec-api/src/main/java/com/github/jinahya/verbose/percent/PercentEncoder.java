package com.github.jinahya.verbose.percent;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * An interface for percent-encoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
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

    /**
     * Encoded given string using specified character set to obtain the bytes
     *
     * @param decoded the string to encode
     * @param charset the character set to encode the input string to byte
     * array.
     *
     * @return encoded String
     */
    default String encode(final String decoded, final Charset charset) {
        final byte[] decodedBytes = decoded.getBytes(charset);
        final byte[] encodedBytes = new byte[decodedBytes.length * 3];
        final ByteBuffer decodedBuffer = ByteBuffer.wrap(decodedBytes);
        final ByteBuffer encodedBuffer = ByteBuffer.wrap(encodedBytes);
        encode(decodedBuffer, encodedBuffer);
        return new String(encodedBytes, 0, encodedBuffer.position());
    }
}
