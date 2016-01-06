package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;

public class HexDecoderImpl implements HexDecoder {

    private int decodeNibble(final int encoded) {
        switch (encoded) {
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
                return encoded - 0x30;
            case 0x41: // 'A'
            case 0x42: // 'B'
            case 0x43: // 'C'
            case 0x44: // 'D'
            case 0x45: // 'E'
            case 0x46: // 'F'
                return encoded - 0x37;
            default: // lower case alpha
                return encoded - 0x57;
        }
    }

    @Override
    public int decodeSingle(final ByteBuffer decoded) {
        final int h = decodeNibble(decoded.get());
        final int l = decodeNibble(decoded.get());
        return (h << 4) | l;
    }

    byte[] decode(final byte[] input, final int inoff, final int inlen,
                  final byte[] output, final int outoff) {
        final ByteBuffer decoded = ByteBuffer.wrap(input, inoff, inlen);
        for (int i = outoff; decoded.hasRemaining(); i++) {
            output[i] = (byte) decodeSingle(decoded);
        }
        return output;
    }

    byte[] decode(final byte[] array, final int offset, final int length) {
        return decode(array, offset, length, new byte[length >> 1], 0);
    }

    byte[] decode(final byte[] array, final int offset) {
        return decode(array, offset, array.length - offset);
    }

    byte[] decode(final byte[] array) {
        return decode(array, 0);
    }
}
