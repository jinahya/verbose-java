package com.github.jinahya.verbose.hex;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

/**
 * A class implementing {@code HexEncoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexEncoderImpl implements HexEncoder {

    /**
     * Encodes a nibble.
     *
     * @param decoded the number to encode.
     * @return encoded character.
     */
    private static int encodeNibble(final int decoded) {
        switch (decoded) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return decoded + 48; // to '0'(48, 0x30) ~ '9'(57, 0x39) <1>
            default: // ? 10 to 15
                return decoded + 55; // to 'A'(65, 0x40) ~ 'F'(70, 0x46) <2>
        }
    }

    @Override
    public void encodeOctet(final int decoded, final ByteBuffer encoded) {
        if (encoded.remaining() < 2) {
            throw new BufferOverflowException();
        }
        final int upper = (decoded >> 0b100) & 017; // <1>
        final int lower = decoded & 0xF; // <2>
        encoded.put((byte) encodeNibble(upper));
        encoded.put((byte) encodeNibble(lower));
    }
}
