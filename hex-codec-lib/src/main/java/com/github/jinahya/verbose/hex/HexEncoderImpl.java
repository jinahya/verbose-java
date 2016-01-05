package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;

public class HexEncoderImpl implements HexEncoder {

    private int encodeNibble(final int nibble) {
        switch (nibble) {
            case 0x00:
            case 0x01:
            case 0x02:
            case 0x03:
            case 0x04:
            case 0x05:
            case 0x06:
            case 0x07:
            case 0x08:
            case 0x09:
                return nibble + 0x30;
            default:
                return nibble + 0x37;
        }
    }

    @Override
    public void encodeSingle(final int decoded, final ByteBuffer encoded) {
        final int h = (decoded >> 4) & 0x0F;
        final int l = decoded & 0x0F;
        encoded.put((byte) encodeNibble(h));
        encoded.put((byte) encodeNibble(l));
    }
}
