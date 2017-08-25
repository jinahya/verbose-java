package com.github.jinahya.verbose.percent;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * An interface for decoding percent-encoded characters to bytes.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
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
        // @todo: validate arguments!
        final int decodedPosition = decoded.position(); // <1>
        while (decoded.hasRemaining()) { // <2>
            final int encodedPosition = encoded.position(); // <3>
            try {
                final int octet = decodeOctet(encoded); // <4>
                decoded.put((byte) octet);
            } catch (final BufferUnderflowException bue) { // NOSONAR
                encoded.position(encodedPosition); // <5>
                break;
            }
        }
        return decoded.position() - decodedPosition; // <6>
    }

    /**
     * Decodes characters in given buffer and returns a new byte buffer
     * containing the result.
     *
     * @param encoded the byte buffer containing characters to encode
     * @return a byte buffer containing decoded bytes.
     */
    default ByteBuffer decode(final ByteBuffer encoded) {
        // @todo: validate arguments!
        final ByteBuffer decoded = allocate(encoded.remaining()); // <1>
        decode(encoded, decoded); // <2>
        return (ByteBuffer) decoded.flip(); // <3>
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
        // @todo: validate arguments!
        final byte[] encodedBytes = encoded.getBytes(US_ASCII); // <1>
        final byte[] decodedBytes = new byte[encodedBytes.length]; // <2>
        final ByteBuffer encodedBuffer = wrap(encodedBytes);
        final ByteBuffer decodedBuffer = wrap(decodedBytes);
        decode(encodedBuffer, decodedBuffer); // <3>
        return new String(decodedBytes, 0, decodedBuffer.position(), charset); // <4>
    }
}
