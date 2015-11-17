package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class HexEncoderImplTest {

    @Test
    public void encodeForRFC4648TestVectors() {
        TestVectors.consumeRFC4648TestVectors((decoded, encoded) -> {
            {
                final byte[] actual = new HexEncoderImpl().encode(decoded, 0);
                assertEquals(actual, encoded);
            }
            {
                final ByteBuffer expected = ByteBuffer.wrap(decoded);
                final ByteBuffer actual = new HexEncoderImpl().encode(expected);
                assertEquals(actual, expected);
            }
        });
    }

    private transient final Logger logger = getLogger(getClass());
}
