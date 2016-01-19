package com.github.jinahya.verbose.percent;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * An interface for percent-encoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@FunctionalInterface
public interface PercentEncoder {

    /**
     * Encodes given byte and put encoded characters to specified byte buffer. A
     * caution should be taken that this method may lazily throw a
     * {@code BufferOverflowException} while some bytes already have been
     * provided to specified output buffer.
     *
     * @param decoded the octet to encode
     * @param encoded the byte buffer to which encoded characters are put.
     */
    void encodeOctet(int decoded, ByteBuffer encoded);

    /**
     * Encodes bytes in given input buffer and put encoded characters to
     * specified output buffer.
     *
     * @param decoded the input buffer
     * @param encoded the output buffer
     * @return number of bytes encoded
     */
    default int encode(final ByteBuffer decoded, final ByteBuffer encoded) {
        int count = 0;
        while (decoded.hasRemaining()) {
            final int position = encoded.position();
            try {
                encodeOctet(decoded.get(), encoded);
                count++;
            } catch (final BufferOverflowException boe) {
                decoded.position(decoded.position() - 1);
                encoded.position(position);
                break;
            }
        }
        return count;
    }

    /**
     * Encodes bytes in given buffer and returns a new byte buffer containing
     * the result.
     *
     * @param decoded the buffer containing bytes to encode
     * @return a new byte buffer containing encoded characters.
     */
    default ByteBuffer encode(final ByteBuffer decoded) {
        final ByteBuffer encoded = ByteBuffer.allocate(decoded.remaining() * 3);
        encode(decoded, encoded);
        encoded.flip();
        return encoded;
    }

    /**
     * Encodes given string using specified character set to obtain a byte
     * array.
     *
     * @param decoded the string to encode
     * @param charset the character set to obtain a byte array from given
     * string.
     *
     * @return encoded String
     */
    default String encode(final String decoded, final Charset charset) {
        final byte[] decodedBytes = decoded.getBytes(charset);
        final byte[] encodedBytes = new byte[decodedBytes.length * 3];
        final ByteBuffer decodedBuffer = ByteBuffer.wrap(decodedBytes);
        final ByteBuffer encodedBuffer = ByteBuffer.wrap(encodedBytes);
        encode(decodedBuffer, encodedBuffer);
        return new String(encodedBytes, 0, encodedBuffer.position(), US_ASCII);
    }

    default String encode(final String decoded) {
        return encode(decoded, UTF_8);
    }
}
