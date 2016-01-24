package com.github.jinahya.verbose.hex;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * A class implementing {@code HexDecoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexDecoderImpl implements HexDecoder {

    /**
     * Decodes a nibble.
     *
     * @param encoded the hex character to decode
     * @return decoded number
     */
    private static int decodeNibble(final int encoded) {
        switch (encoded) {
            case '0': // 48, 0x30
            case '1': // 49, 0x31
            case '2': // 50, 0x32
            case '3': // 51, 0x33
            case '4': // 52, 0x34
            case '5': // 53, 0x35
            case '6': // 54, 0x36
            case '7': // 55, 0x37
            case '8': // 56, 0x38
            case '9': // 57, 0x39
                return encoded - 48; // to 0 ~ 9
            case 'A': // 65, 0x41
            case 'B': // 66, 0x42
            case 'C': // 67, 0x43
            case 'D': // 68, 0x44
            case 'E': // 69, 0x45
            case 'F': // 70, 0x46
                return encoded - 55; // to 10 ~ 15
            default: // assume 'a' ~ 'f'
                return encoded - 87; // to 10 ~ 15
        }
    }

    @Override
    public int decodeOctet(final ByteBuffer decoded) {
        if (decoded == null) {
            throw new NullPointerException("null decoded");
        }
        if (decoded.remaining() < 2) {
            throw new BufferUnderflowException();
        }
        final int upper = decodeNibble(decoded.get());
        final int lower = decodeNibble(decoded.get());
        return ((upper & 0xF) << 4) | (lower & 0xF); // <1>
    }
}
