package com.github.jinahya.verbose.percent;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.InvalidMarkException;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * An interface for percent-decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface PercentDecoder {

    /**
     * Decodes characters in given buffer and returns a decoded byte.
     *
     * @param encoded the byte buffer
     * @return decoded byte
     */
    int decodeOctet(ByteBuffer encoded);

    default int decode(final ByteBuffer encoded, final ByteBuffer decoded) {
        int count = 0;
        Integer mark = null; // <1>
        {
            final int position = encoded.position();
            try {
                encoded.reset();
                mark = encoded.position();
            } catch (final InvalidMarkException ime) {
            }
            encoded.position(position);
        }
        while (decoded.hasRemaining()) {
            encoded.mark();
            try {
                decoded.put((byte) decodeOctet(encoded));
            } catch (final BufferUnderflowException bue) {
                encoded.reset();
                break;
            }
        }
        if (mark != null) { // <2>
            final int position = encoded.position();
            encoded.position(mark);
            encoded.mark();
        }
        return count;
    }

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
        final byte[] encodedBytes = encoded.getBytes(US_ASCII);
        final byte[] decodedBytes = new byte[encodedBytes.length];
        final ByteBuffer encodedBuffer = ByteBuffer.wrap(encodedBytes);
        final ByteBuffer decodedBuffer = ByteBuffer.wrap(decodedBytes);
        decode(encodedBuffer, decodedBuffer);
        return new String(decodedBytes, 0, decodedBuffer.position(), charset);
    }

    default String decode(final String encoded) {
        return decode(encoded, UTF_8);
    }
}
