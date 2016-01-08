package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class HexCodecTest {

    @Test(invocationCount = 1024)
    public void encodeDecode() {
        final byte[] expected = new byte[current().nextInt(1024)];
        current().nextBytes(expected);
        final byte[] encoded = new byte[expected.length << 1];
        new HexEncoderImpl().encode(
                ByteBuffer.wrap(expected), ByteBuffer.wrap(encoded));
        final byte[] actual = new byte[encoded.length >> 1];
        new HexDecoderImpl().decode(
                ByteBuffer.wrap(encoded), ByteBuffer.wrap(actual));
        assertEquals(actual, expected);
    }

    private transient final Logger logger = getLogger(getClass());
}
