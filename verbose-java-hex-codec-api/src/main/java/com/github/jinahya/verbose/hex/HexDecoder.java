package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;

public interface HexDecoder {

    int decodeSingle(ByteBuffer encoded);

    default void decode(final ByteBuffer encoded, final ByteBuffer decoded) {
        while (encoded.hasRemaining()) {
            decoded.put((byte) decodeSingle(encoded));
        }
    }

    default ByteBuffer decode(final ByteBuffer encoded) {
        final ByteBuffer decoded = ByteBuffer.allocate(encoded.remaining() / 2);
        decode(encoded, decoded);
        decoded.flip();
        return decoded;
    }
//    void decode(byte[] sourceArray, int sourceOffset, byte[] targetArray,
//                int targetOffset);
//
//    default byte[] decode(byte[] array, int offset) {
//        final byte[] tarray = new byte[(array.length - offset) / 2];
//        final int toffset = 0;
//        HexDecoder.this.decode(array, offset, tarray, toffset);
//        return tarray;
//    }
//
//    default void decode(final ByteBuffer sourceBuffer,
//                        final ByteBuffer targetBuffer) {
//        final byte[] encoded = new byte[2];
//        final byte[] decoded = new byte[1];
//        while (sourceBuffer.remaining() >= 2) {
//            sourceBuffer.get(encoded);
//            HexDecoder.this.decode(encoded, 0, decoded, 0);
//            targetBuffer.put(decoded);
//        }
//    }
//
//    default ByteBuffer decode(final ByteBuffer buffer) {
//        final ByteBuffer target = ByteBuffer.allocate(buffer.remaining() / 2);
//        HexDecoder.this.decode(buffer, target);
//        return target;
//    }
}
