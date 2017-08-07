package com.github.jinahya.verbose.hex;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
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
     * Encodes given number and puts two hex characters onto specified byte
     * buffer. Only lower 8 bits of given number are used for encoding. A
     * {@link BufferOverflowException} will be thrown if
     * {@code encoded.remaining()} is less than {@code 2}.
     *
     * @param decoded the number to encode
     * @param encoded the byte buffer onto which encoded characters are put.
     */
    void encodeOctet(int decoded, ByteBuffer encoded);

    /**
     * Encodes bytes from given input buffer and puts results to specified
     * output buffer. This method continuously invokes
     * {@link #encodeOctet(int, java.nio.ByteBuffer)} with {@code decoded.get()}
     * and {@code encoded} as its arguments while {@code decoded} has remaining
     * and {@code encoded} has {@code 2} or more remaining.
     *
     * @param decoded the input buffer from which decoded number are encoded
     * @param encoded the output buffer to which encoded characters are put
     * @return number of bytes consumed from {@code decoded}.
     */
    default int encode(final ByteBuffer decoded, final ByteBuffer encoded) {
        // @todo: validate arguments!
        final int position = decoded.position(); // <1>
        while (decoded.hasRemaining() && (encoded.remaining() >= 2)) { // <2>
            encodeOctet(decoded.get(), encoded);
        }
        return decoded.position() - position; // <3>
    }

    /**
     * Encodes remaining bytes from given input buffer and returns a byte buffer
     * containing the result.
     *
     * @param decoded the byte buffer containing bytes to encode
     * @return a byte buffer containing encoded bytes.
     */
    default ByteBuffer encode(final ByteBuffer decoded) {
        // @todo: validate argument(s)!
        final ByteBuffer encoded = allocate(decoded.remaining() << 1); // <1>
        encode(decoded, encoded); // <2>
        return (ByteBuffer) encoded.flip(); // <3>
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
        // @todo: validate arguments!
        final byte[] decodedBytes = decoded.getBytes(charset); // <1>
        final byte[] encodedBytes = new byte[decodedBytes.length * 2]; // <2>
        encode(wrap(decodedBytes), wrap(encodedBytes)); // <3>
        return new String(encodedBytes, US_ASCII); // <4>
    }

//    /**
//     * Encodes given string. This method invokes
//     * {@link #encode(java.lang.String, java.nio.charset.Charset)} method with
//     * given string and {@link StandardCharsets#UTF_8} as its arguments and
//     * returns the result.
//     *
//     * @param decoded the string to encode
//     * @return an encoded string
//     * @see #encode(java.lang.String, java.nio.charset.Charset)
//     */
//    default String encode(final String decoded) {
//        return encode(decoded, UTF_8);
//    }
}
