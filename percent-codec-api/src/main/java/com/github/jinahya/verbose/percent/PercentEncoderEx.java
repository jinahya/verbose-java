package com.github.jinahya.verbose.percent;

import java.nio.ByteBuffer;

/**
 * An interface for percent-encoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public interface PercentEncoderEx extends PercentEncoder {

    default void encode(final byte[] inarr, final int inoff, final int inlen,
                        final byte[] outarr, final int outoff) {

        final ByteBuffer encoded = ByteBuffer.wrap(inarr, inoff, inlen);
        final int outlen = outarr.length - outoff;
        final ByteBuffer decoded = ByteBuffer.wrap(outarr, outoff, outlen);
        encode(encoded, decoded);
    }

    default void encode(final byte[] inarr, final int inoff,
                        final byte[] outarr, final int outoff) {
        encode(inarr, inoff, inarr.length - inoff, outarr, outoff);
    }

    default void encode(final byte[] inarr, final byte[] outarr,
                        final int outoff) {
        encode(inarr, 0, outarr, outoff);
    }

    default void encode(final byte[] inarr, final int inoff, final int inlen,
                        final byte[] outarr) {
        encode(inarr, inoff, inlen, outarr, 0);
    }

    default void encode(final byte[] inarr, final int inoff,
                        final byte[] outarr) {
        encode(inarr, inoff, inarr.length - inoff, outarr);
    }

    default void encode(final byte[] inarr, final byte[] outarr) {
        encode(inarr, 0, outarr);
    }

}
