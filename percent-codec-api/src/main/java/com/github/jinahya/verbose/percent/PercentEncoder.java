package com.github.jinahya.verbose.percent;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * An interface for encoding bytes to percent-encoded characters.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@FunctionalInterface
public interface PercentEncoder {

    /**
     * Encodes given byte into the specified buffer. A
     * {@code BufferOverflowException} will be thrown if the buffer's remaining
     * is less than required.
     *
     * @param decoded the byte to encode
     * @param encoded the buffer into which encoded characters are transfered.
     */
    void encodeOctet(int decoded, ByteBuffer encoded);

    /**
     * Encodes bytes in given input buffer and put encoded characters onto
     * specified output buffer.
     *
     * @param decoded the input buffer
     * @param encoded the output buffer
     * @return the number of bytes encoded that is the number of bytes consumed
     * from {@code decoded}
     */
    default int encode(final ByteBuffer decoded, final ByteBuffer encoded) {
        final int previous = decoded.position(); // <1>
        while (decoded.hasRemaining()) { // <2>
            try {
                encodeOctet(decoded.get(), encoded); // <3>
            } catch (final BufferOverflowException boe) { // NOSONAR <4>
                decoded.position(decoded.position() - 1);
                break;
            }
        }
        return decoded.position() - previous; // <5>
    }

    /**
     * Encodes bytes in given buffer and returns a new byte buffer containing
     * the result.
     *
     * @param decoded the buffer containing bytes to encode
     * @return a new byte buffer containing encoded characters.
     */
    default ByteBuffer encode(final ByteBuffer decoded) {
        final ByteBuffer encoded = allocate(decoded.remaining() * 3); // <1>
        encode(decoded, encoded); // <2>
        encoded.flip(); // <3>
        return encoded;
    }

    /**
     * Encodes given string using specified {@code charset} to obtain a byte
     * array from the string.
     *
     * @param decoded the string to encode
     * @param charset the character set to obtain a byte array from the string.
     *
     * @return an encoded String
     */
    default String encode(final String decoded, final Charset charset) {
        final byte[] decodedBytes = decoded.getBytes(charset); // <1>
        final byte[] encodedBytes = new byte[decodedBytes.length * 3]; // <2>
        final ByteBuffer decodedBuffer = wrap(decodedBytes);
        final ByteBuffer encodedBuffer = wrap(encodedBytes);
        encode(decodedBuffer, encodedBuffer); // <3>
        return new String(encodedBytes, 0, encodedBuffer.position(), US_ASCII);
    }

    /**
     * Encodes given string. This method invokes
     * {@link #encode(java.lang.String, java.nio.charset.Charset)} with given
     * string and {@link StandardCharsets#UTF_8} and returns the result.
     *
     * @param decoded the string to encode
     * @return an encoded string.
     * @see #encode(java.lang.String, java.nio.charset.Charset)
     */
    default String encode(final String decoded) {
        return encode(decoded, UTF_8);
    }
}
