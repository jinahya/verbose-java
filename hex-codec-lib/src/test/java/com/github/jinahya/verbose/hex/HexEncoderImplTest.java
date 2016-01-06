package com.github.jinahya.verbose.hex;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class HexEncoderImplTest {

    @Test
    public void encodeForRFC4648TestVectors() {
        TestVectors.acceptRFC4648ByteArrays((d, e) -> {
            final ByteBuffer encoded
                    = new HexEncoderImpl().encode(ByteBuffer.wrap(d));
            assertEquals(encoded, ByteBuffer.wrap(e));
        });
    }

    private transient final Logger logger = getLogger(getClass());
}
