package com.github.jinahya.verbose.hex;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class HexEncoderImplTest {

    /**
     * Encodes and compare to {@code RFC4648} test vectors.
     */
    @Test
    public void encodeAndCompareToRFC4648TestVectors() {
        Rfc4648TestVectors.base16Strings((d, e) -> {
            final String encoded = new HexEncoderImpl().encode(d);
            assertEquals(encoded, e);
        });
    }

    @Test(invocationCount = 128)
    public void encodeAndCompareCommons() throws DecoderException {
        final String decoded = RandomStringUtils.random(current().nextInt(128));
        final String verbose = new HexEncoderImpl().encode(decoded);
        final String commons = Hex.encodeHexString(decoded.getBytes(UTF_8));
        assertEquals(verbose, commons.toUpperCase());
    }

    private transient final Logger logger = getLogger(getClass());
}
