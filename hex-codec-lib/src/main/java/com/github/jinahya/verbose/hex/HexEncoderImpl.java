package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;

public class HexEncoderImpl implements HexEncoder {

    private static int encodeNibble(final int decoded) {
        switch (decoded) {
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
                return decoded + 0x30;
            default:
                return decoded + 0x37;
        }
    }

    @Override
    public void encodeSingle(final int decoded, final ByteBuffer encoded) {
        final int h = (decoded >> 4) & 0x0F;
        final int l = decoded & 0x0F;
        encoded.put((byte) encodeNibble(h));
        encoded.put((byte) encodeNibble(l));
    }

    byte[] encode(final byte[] input, final int inoff, final int inlen,
                  final byte[] output, final int outoff) {
        final int outlen = inlen << 1;
        final ByteBuffer encoded = ByteBuffer.wrap(output, outoff, outlen);
        final int inlim = inoff + inlen;
        for (int i = inoff; i < inlim; i++) {
            encodeSingle(input[i], encoded);
        }
        return output;
    }

    byte[] encode(final byte[] array, final int offset, final int length) {
        return encode(array, offset, length, new byte[length << 1], 0);
    }

    byte[] encode(final byte[] array, final int offset) {
        return encode(array, offset, array.length - offset);
    }

    byte[] encode(final byte[] array) {
        return encode(array, 0);
    }
}
