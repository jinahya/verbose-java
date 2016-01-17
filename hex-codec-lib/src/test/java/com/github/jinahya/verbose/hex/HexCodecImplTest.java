package com.github.jinahya.verbose.hex;

import static com.github.jinahya.verbose.hex.HexCodecTests.fillFile;
import static com.github.jinahya.verbose.hex.HexCodecTests.tempFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 * Test class for testing encoding/decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexCodecImplTest {

    @Test(invocationCount = 128)
    public void encodeDecode() {
        final byte[] created = new byte[current().nextInt(128)];
        current().nextBytes(created);
        final byte[] encoded = new byte[created.length << 1];
        final byte[] decoded = new byte[encoded.length >> 1];
        new HexEncoderImpl().encode(ByteBuffer.wrap(created),
                                    ByteBuffer.wrap(encoded));
        new HexDecoderImpl().decode(ByteBuffer.wrap(encoded),
                                    ByteBuffer.wrap(decoded));
        assertEquals(decoded, created);
    }

    private void encodedDecodeString(final String string,
                                     final Charset charset) {
        final String encoded = new HexEncoderImpl().encode(string, charset);
        final String decoded = new HexDecoderImpl().decode(encoded, charset);
        assertEquals(decoded, string);
    }

    @Test(invocationCount = 128)
    public void encodedDecodeString() {
        encodedDecodeString(RandomStringUtils.random(current().nextInt(1024)),
                            StandardCharsets.UTF_8);
        encodedDecodeString(
                RandomStringUtils.randomAscii(current().nextInt(1024)),
                StandardCharsets.US_ASCII);
    }

    @Test(invocationCount = 128)
    public void encodeDecodeStream() throws IOException {
        final File created = fillFile(tempFile());
        final File encoded = tempFile();
        try (InputStream in = new FileInputStream(created)) {
            try (OutputStream out = new HexEncoderStream(
                    new FileOutputStream(encoded), new HexEncoderImpl())) {
                final long copied = IOUtils.copyLarge(in, out);
                assertEquals(copied, created.length());
                out.flush();
            }
        }
        final File decoded = tempFile();
        try (InputStream in = new HexDecoderStream(
                new FileInputStream(encoded), new HexDecoderImpl())) {
            try (OutputStream out = new FileOutputStream(decoded)) {
                final long copied = IOUtils.copyLarge(in, out);
                assertEquals(copied, created.length());
                out.flush();
            }
        }
        assertTrue(FileUtils.contentEquals(decoded, created));
    }

    /**
     * Encodes random bytes and compare the result to what {@link Hex} encodes.
     *
     * @throws DecoderException if an error occurs.
     */
    @Test(invocationCount = 128)
    public void encodeVerboseDecodeCommons() throws DecoderException {
        final byte[] expected = new byte[current().nextInt(128)];
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
