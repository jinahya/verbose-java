package com.github.jinahya.verbose.hex;

import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class HexCodecTest {

    @Test(invocationCount = 1024)
    public void encodeDecodeWithArray() {
        final byte[] expected = new byte[current().nextInt(1024)];
        current().nextBytes(expected);
        final byte[] encoded = new HexEncoderImpl().encode(expected);
        final byte[] actual = new HexDecoderImpl().decode(encoded);
        assertEquals(actual, expected);
    }

    private transient final Logger logger = getLogger(getClass());
}
