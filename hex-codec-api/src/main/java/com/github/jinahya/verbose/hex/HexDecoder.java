package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    /**
     * Decodes all remaining bytes from given input buffer and put results to
     * specified output buffer.
     *
     * @param encoded the input buffer
     * @param decoded the output buffer
     */
    default void decode(final ByteBuffer encoded, final ByteBuffer decoded) {
        while (encoded.hasRemaining()) {
            decoded.put((byte) decodeOctet(encoded));
        }
    }

    /**
     * Decodes all remaining bytes from given input buffer and returns a byte
     * buffer containing result.
     *
     * @param encoded the input buffer
     * @return a byte buffer containing result
     */
    default ByteBuffer decode(final ByteBuffer encoded) {
        final ByteBuffer decoded // <1>
                = ByteBuffer.allocate(encoded.remaining() / 2);
        decode(encoded, decoded);
        decoded.flip();
        return decoded;
    }

    /**
     * Decodes given string and returns the result as a string encoded with
     * specified character set.
     *
     * @param encoded the string to decode
     * @param charset the character set to encode output string.
     * @return a decoded string
     */
    default String decode(final String encoded, final Charset charset) {
        final byte[] encodedBytes = encoded.getBytes(US_ASCII);
        final byte[] decodedBytes = new byte[encodedBytes.length >> 1];
        decode(ByteBuffer.wrap(encodedBytes), ByteBuffer.wrap(decodedBytes));
        return new String(decodedBytes, charset);
    }

    /**
     * Decodes given string. The {@code decode(String)} method of
     * {@code HexDecoder} class invokes
     * {@link #decode(java.lang.String, java.nio.charset.Charset)} with given
     * string and {@link StandardCharsets#UTF_8} and returns the result.
     *
     * @param encoded the string to decode
     * @return a decoded string
     */
    default String decode(final String encoded) {
        return decode(encoded, UTF_8);
    }
}
