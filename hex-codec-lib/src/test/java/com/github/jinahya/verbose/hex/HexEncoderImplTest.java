package com.github.jinahya.verbose.hex;

import com.google.common.io.BaseEncoding;
import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * A class testing {@link HexEncoderImpl}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexEncoderImplTest {

    /**
     * Tests encoding with {@code RFC4648} test vectors.
     */
    @Test
    public void testEncodingAgainstRfc4648TestVectors() {
        Rfc4648TestVectors.base16((d, e) -> {
            final String encoded = new HexEncoderImpl().encode(d);
            assertEquals(encoded, e);
        });
    }

    @Test(invocationCount = 128)
    public void encodeVerboseDecodeCommons() throws DecoderException {
        final byte[] created = new byte[current().nextInt(128)];
        current().nextBytes(created);
        final byte[] encoded = new byte[created.length << 1];
        new HexEncoderImpl().encode(
                ByteBuffer.wrap(created), ByteBuffer.wrap(encoded));
        final byte[] decoded = new Hex().decode(encoded);
        assertEquals(decoded, created);
    }

    @Test(invocationCount = 128)
    public void encodeVerboseDecodeGuava() {
        final String created = RandomStringUtils.random(current().nextInt(128));
        final String encoded = new HexEncoderImpl().encode(created);
        final byte[] decoded = BaseEncoding.base16().decode(encoded);
        assertEquals(new String(decoded, UTF_8), created);
    }

}
