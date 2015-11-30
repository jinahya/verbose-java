package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

public class HexEncoderImplTest {

    @Test
    public void encodeForRFC4648TestVectors() {
        TestVectors.consumeRFC4648TestVectors((d, e) -> {
            final ByteBuffer encoded = ByteBuffer.wrap(e);
            new HexEncoderImpl().encode(ByteBuffer.wrap(d), encoded);
            encoded.flip();
            assertTrue(encoded.equals(ByteBuffer.wrap(e)));
        });
    }

    private transient final Logger logger = getLogger(getClass());
}
