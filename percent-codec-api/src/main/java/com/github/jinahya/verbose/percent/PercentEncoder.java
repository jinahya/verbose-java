package com.github.jinahya.verbose.percent;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
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
     * Encodes given byte and put encoded characters on specified byte buffer.
     * This method may lazily throw a {@code BufferOverflowException} while some
     * bytes already have been provided to given buffer.
     *
     * @param decoded the byte to encode
     * @param encoded the byte buffer onto which encoded characters are put
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
        int count = 0;
        while (decoded.hasRemaining()) { // <1>
            final int position = encoded.position(); // <2>
            try {
                encodeOctet(decoded.get(), encoded);
                count++;
            } catch (final BufferOverflowException boe) { // NOSONAR
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
        final ByteBuffer encoded // <1>
                = ByteBuffer.allocate(decoded.remaining() * 3);
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
     * @return an encoded String
     */
    default String encode(final String decoded, final Charset charset) {
        final byte[] decodedBytes = decoded.getBytes(charset); // <1>
        final byte[] encodedBytes = new byte[decodedBytes.length * 3]; // <2>
        final ByteBuffer decodedBuffer = ByteBuffer.wrap(decodedBytes);
        final ByteBuffer encodedBuffer = ByteBuffer.wrap(encodedBytes);
        encode(decodedBuffer, encodedBuffer); // <3>
        return new String(encodedBytes, 0, encodedBuffer.position(), US_ASCII);
    }

    /**
     * Encodes given string using {@link StandardCharsets#UTF_8} to obtain a
     * byte array.
     *
     * @param decoded the string to encode
     * @return an encoded string.
     * @see #encode(java.lang.String, java.nio.charset.Charset)
     */
    default String encode(final String decoded) {
        return encode(decoded, UTF_8);
    }
}
