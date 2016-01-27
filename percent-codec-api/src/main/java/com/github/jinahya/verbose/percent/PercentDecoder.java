package com.github.jinahya.verbose.percent;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * An interface for decoding percent-encoded characters to bytes.
 */
@FunctionalInterface
public interface PercentDecoder {

    /**
     * Decodes a single byte from a sequence of characters in given buffer. A
     * {@code BufferUnderflowException} will be thrown if the buffer's remaining
     * is less than required
     *
     * @param encoded the buffer from which encoded characters are read
     * @return a decoded byte
     */
    int decodeOctet(ByteBuffer encoded);

    /**
     * Decodes characters in given input buffer and put decoded bytes to
     * specified output buffer.
     *
     * @param encoded the input buffer
     * @param decoded the output buffer
     * @return number of bytes encoded that is the number of byte provided to
     * {@code decoded}
     */
    default int decode(final ByteBuffer encoded, final ByteBuffer decoded) {
        final int previous = decoded.position(); // <1>
        while (decoded.hasRemaining()) { // <2>
            try {
                final int octet = decodeOctet(encoded); // <3>
                decoded.put((byte) octet);
            } catch (final BufferUnderflowException bue) { // NOSONAR <4>
                break;
            }
        }
        return decoded.position() - previous; // <5>
    }

    /**
     * Decodes characters in given buffer and returns a new byte buffer
     * containing the result.
     *
     * @param encoded the byte buffer containing characters to encode
     * @return a byte buffer containing decoded bytes.
     */
    default ByteBuffer decode(final ByteBuffer encoded) {
        final ByteBuffer decoded = allocate(encoded.remaining()); // <1>
        decode(encoded, decoded); // <2>
        decoded.flip(); // <3>
        return decoded;
    }

    /**
     * Decodes given string using specified character set to translate a byte
     * array into the result string.
     *
     * @param encoded the string to decode
     * @param charset the character set to decode translated byte array into the
     * result string
     *
     * @return a decoded String
     */
    default String decode(final String encoded, final Charset charset) {
        final byte[] encodedBytes = encoded.getBytes(US_ASCII); // <1>
        final byte[] decodedBytes = new byte[encodedBytes.length]; // <2>
        final ByteBuffer encodedBuffer = wrap(encodedBytes);
        final ByteBuffer decodedBuffer = wrap(decodedBytes);
        decode(encodedBuffer, decodedBuffer); // <3>
        return new String(decodedBytes, 0, decodedBuffer.position(), charset); // <4>
    }

    /**
     * Decodes given string. This method invokes
     * {@link #decode(java.lang.String, java.nio.charset.Charset)} with given
     * string and {@link StandardCharsets#UTF_8} and returns the result.
     *
     * @param encoded the string to decode
     * @return a decoded string.
     * @see #decode(java.lang.String, java.nio.charset.Charset)
     */
    default String decode(final String encoded) {
        return decode(encoded, UTF_8);
    }
}
