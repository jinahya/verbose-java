package com.github.jinahya.verbose.hex;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * And interface for hex encoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@FunctionalInterface
public interface HexEncoder {

    /**
     * Encodes given byte and puts two hex characters to specified byte buffer.
     * Only lower 8 bits of given byte is used for encoding. A
     * {@link BufferOverflowException} will be thrown if
     * {@code encoded.remaining} is less than {@code 2}.
     *
     * @param decoded the byte to encode
     * @param encoded the byte buffer on which encoded characters are put.
     */
    void encodeOctet(int decoded, ByteBuffer encoded);

    /**
     * Encodes bytes from given input buffer and puts results to specified
     * output buffer. This method continuously invokes
     * {@link #encodeOctet(int, java.nio.ByteBuffer)} with {@code decoded.get()}
     * and {@code encoded} as its arguments while {@code decoded.hasRemaining()}
     * returns {@code true} and {@code encoded.remaining()} is greater than or
     * equals to {@code 2}.
     *
     * @param decoded the input buffer
     * @param encoded the output buffer
     * @return number of bytes encoded from {@code decoded}.
     */
    default int encode(final ByteBuffer decoded, final ByteBuffer encoded) {
        int count = 0;
        while (decoded.hasRemaining() && (encoded.remaining() >= 2)) { // <1>
            encodeOctet(decoded.get(), encoded);
            count++;
        }
        return count;
    }

    /**
     * Encodes all remaining bytes from given input buffer and returns a byte
     * buffer containing the result.
     *
     * @param decoded the byte buffer containing bytes to encode
     * @return a byte buffer containing encoded bytes.
     */
    default ByteBuffer encode(final ByteBuffer decoded) {
        final ByteBuffer encoded // <1>
                = ByteBuffer.allocate(decoded.remaining() << 1);
        encode(decoded, encoded);
        encoded.position(0);
        return encoded;
    }

    /**
     * Encodes given string using specified character set to get a byte array
     * from the string.
     *
     * @param decoded the string to encoding
     * @param charset the character set to get a byte array from the string.
     * @return encoded string.
     */
    default String encode(final String decoded, final Charset charset) {
        final byte[] decodedBytes = decoded.getBytes(charset); // <1>
        final byte[] encodedBytes = new byte[decodedBytes.length * 2]; // <2>
        encode(ByteBuffer.wrap(decodedBytes), // <3>
               ByteBuffer.wrap(encodedBytes));
        return new String(encodedBytes, US_ASCII); // <4>
    }

    /**
     * Encodes given string. This method invokes
     * {@link #encode(java.lang.String, java.nio.charset.Charset)} with the
     * given string and {@link StandardCharsets#UTF_8} as its arguments and
     * returns the result.
     *
     * @param decoded the string to encode
     * @return encoded string
     * @see #encode(java.lang.String, java.nio.charset.Charset)
     */
    default String encode(final String decoded) {
        return encode(decoded, UTF_8);
    }
}
