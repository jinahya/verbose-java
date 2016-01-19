package com.github.jinahya.verbose.percent;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * An interface for percent-decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@FunctionalInterface
public interface PercentDecoder {

    /**
     * Decodes characters in given byte buffer and returns a decoded byte. A
     * caution should be taken that this method may lazily throw a
     * {@code BufferUnderflowException} while some bytes already have been
     * consumed from given buffer.
     *
     * @param encoded the byte buffer
     * @return the decoded byte
     */
    int decodeOctet(ByteBuffer encoded);

    /**
     * Decodes characters in given input buffer and put decoded bytes to
     * specified output buffer.
     *
     * @param encoded the input buffer
     * @param decoded the output buffer
     * @return number of bytes encoded
     */
    default int decode(final ByteBuffer encoded, final ByteBuffer decoded) {
        int count = 0;
        while (decoded.hasRemaining()) { // <2>
            final int position = encoded.position();
            try {
                final int octet = decodeOctet(encoded);
                decoded.put((byte) octet);
                count++;
            } catch (final BufferUnderflowException bue) {
                encoded.position(position);
                break;
            }
        }
        return count;
    }

    /**
     * Decodes characters in given buffer and returns a new byte buffer
     * containing the result.
     *
     * @param encoded the byte buffer containing characters to encode
     * @return a byte buffer containing decoded bytes.
     */
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
        final byte[] encodedBytes = encoded.getBytes(US_ASCII); // <1>
        final byte[] decodedBytes = new byte[encodedBytes.length]; // <2>
        final ByteBuffer encodedBuffer = ByteBuffer.wrap(encodedBytes);
        final ByteBuffer decodedBuffer = ByteBuffer.wrap(decodedBytes);
        decode(encodedBuffer, decodedBuffer);
        return new String(decodedBytes, 0, decodedBuffer.position(), charset);
    }

    default String decode(final String encoded) {
        return decode(encoded, UTF_8);
    }
}
