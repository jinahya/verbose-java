package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;

public class HexDecoderImpl implements HexDecoder {

    private int decodeNibble(final int nibble) {
        switch (nibble) {
            case 0x30: // '0'
            case 0x31: // '1'
            case 0x32: // '2'
            case 0x33: // '3'
            case 0x34: // '4'
            case 0x35: // '5'
            case 0x36: // '6'
            case 0x37: // '7'
            case 0x38: // '8'
            case 0x39: // '9'
                return nibble - 0x30;
            case 0x41: // 'A'
            case 0x42: // 'B'
            case 0x43: // 'C'
            case 0x44: // 'D'
            case 0x45: // 'E'
            case 0x46: // 'F'
                return nibble - 0x37;
            default: // lower case alpha
                return nibble - 0x57;
        }
    }

    @Override
    public int decodeSingle(final ByteBuffer decoded) {
        final int high = decodeNibble(decoded.get());
        final int low = decodeNibble(decoded.get());
        return (high << 4) | low;
    }
}
