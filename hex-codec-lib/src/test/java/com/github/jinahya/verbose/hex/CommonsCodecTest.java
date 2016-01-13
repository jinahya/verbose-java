package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class CommonsCodecTest {

    @Test(invocationCount = 128)
    public void encodeVerboseDecodeCommons() throws DecoderException {
        final byte[] expected = new byte[current().nextInt(1024)];
        current().nextBytes(expected);
        final byte[] encoded = new byte[expected.length << 1];
        new HexEncoderImpl().encode(ByteBuffer.wrap(expected),
                                    ByteBuffer.wrap(encoded));
        final byte[] actual = new Hex().decode(encoded);
        assertEquals(actual, expected);
    }

    @Test(invocationCount = 128)
    public void encodeCommonsDecodeVerbose() throws DecoderException {
        final byte[] expected = new byte[current().nextInt(1024)];
        current().nextBytes(expected);
        final byte[] encoded = new Hex().encode(expected);
        final byte[] actual = new byte[encoded.length >> 1];
        new HexDecoderImpl().decode(ByteBuffer.wrap(encoded),
                                    ByteBuffer.wrap(actual));
        assertEquals(actual, expected);
    }

    private transient final Logger logger = getLogger(getClass());
}
