package com.github.jinahya.verbose.hex;

import static java.nio.ByteBuffer.wrap;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAscii;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * Test class for testing encoding/decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexCodecImplTest {

    /**
     * Encodes/decodes a random byte array.
     */
    @Test(invocationCount = 128)
    public void encodeDecodeBytes() {
        final byte[] created = new byte[current().nextInt(128)];
        current().nextBytes(created);
        final byte[] encoded = new byte[created.length << 1];
        final byte[] decoded = new byte[encoded.length >> 1];
        new HexEncoderImpl().encode(wrap(created), wrap(encoded));
        new HexDecoderImpl().decode(wrap(encoded), wrap(decoded));
        assertEquals(decoded, created);
    }

    /**
     * Encodes/decodes a randomly generated string.
     */
    @Test(invocationCount = 128)
    public void encodedDecodeString() {
        final String created = random(current().nextInt(128));
        final String encoded = new HexEncoderImpl().encode(created);
        final String decoded = new HexDecoderImpl().decode(encoded);
        assertEquals(decoded, created);
    }

    /**
     * Encodes/decodes a randomly generated ascii string.
     */
    @Test(invocationCount = 128)
    public void encodedDecodeAscii() {
        final String created = randomAscii(current().nextInt(128));
        final String encoded = new HexEncoderImpl().encode(created, US_ASCII);
        final String decoded = new HexDecoderImpl().decode(encoded, US_ASCII);
        assertEquals(decoded, created);
    }
}
