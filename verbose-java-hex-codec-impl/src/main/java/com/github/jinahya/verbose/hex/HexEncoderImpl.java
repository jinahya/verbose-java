package com.github.jinahya.verbose.hex;

/**
 * Hello world!
 *
 */
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

    private void encodeOctet(final int octet, final byte[] array,
                             final int offset) {
        final int high = octet >> 4 & 0x0F;
        array[offset] = (byte) encodeNibble(high);
        final int low = octet & 0x0F;
        array[offset + 1] = (byte) encodeNibble(low);
    }

    @Override
    public void encode(final byte[] sourceArray, final int sourceOffset,
                       final byte[] targetArray, int targetOffset) {
        for (int i = sourceOffset; i < sourceArray.length; i++) {
            encodeOctet(sourceArray[i], targetArray, targetOffset);
            targetOffset += 2;
        }
    }

}
