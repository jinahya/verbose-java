package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;

public interface HexEncoder {

    void encodeSingle(int decoded, ByteBuffer encoded);

    default void encode(final ByteBuffer decoded, final ByteBuffer encoded) {
        while (decoded.hasRemaining()) {
            encodeSingle(decoded.get(), encoded);
        }
    }

    default ByteBuffer encode(final ByteBuffer decoded) {
        final ByteBuffer encoded = ByteBuffer.allocate(decoded.remaining() * 2);
        encode(decoded, encoded);
        encoded.flip();
        return encoded;
    }

//    void encode(byte[] sourceArray, int sourceOffset, byte[] targetArray,
//                int targetOffset);
//
//    default byte[] encode(final byte[] array, final int offset) {
//        final byte[] tarray = new byte[(array.length - offset) * 2];
//        final int toffset = 0;
//        encode(array, offset, tarray, toffset);
//        return tarray;
//    }
//
//    default void encode(final ByteBuffer sourceBuffer,
//                        final ByteBuffer targetBuffer) {
//        final byte[] decoded = new byte[1];
//        final byte[] encoded = new byte[2];
//        while (sourceBuffer.hasRemaining()) {
//            decoded[0] = sourceBuffer.get();
//            encode(decoded, 0, encoded, 0);
//            targetBuffer.put(encoded);
//        }
//    }
//
//    default ByteBuffer encode(final ByteBuffer buffer) {
//        final ByteBuffer target = ByteBuffer.allocate(buffer.remaining() * 2);
//        encode(buffer, target);
//        return target;
//    }
}
