package com.github.jinahya.verbose.hex;

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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

public class HexCodecImplTest extends AbstractHexCodecImplTest {

    @Test(invocationCount = 128)
    public void encodeDecode() {
        final byte[] created = new byte[current().nextInt(1024)];
        current().nextBytes(created);
        final byte[] encoded = new byte[created.length << 1];
        acceptEncoder(e -> {
            e.encode(ByteBuffer.wrap(created), ByteBuffer.wrap(encoded));
        });
        final byte[] decoded = new byte[encoded.length >> 1];
        acceptDecoder(d -> {
            d.decode(ByteBuffer.wrap(encoded), ByteBuffer.wrap(decoded));
        });
        assertEquals(decoded, created);
    }

    private void encodedDecodeString(
            final String string, final Charset charset) {
        acceptCodec((e, d) -> {
            final String encoded = e.encode(string, charset);
            final String decoded = d.decode(encoded, charset);
            assertEquals(decoded, string);
        });
    }

    @Test
    public void encodedDecodeString() {
        encodedDecodeString(RandomStringUtils.random(current().nextInt(1024)),
                            StandardCharsets.UTF_8);
        encodedDecodeString(
                RandomStringUtils.randomAscii(current().nextInt(1024)),
                StandardCharsets.US_ASCII);
    }

    @Test(invocationCount = 128)
    public void encodeDecodeStream() throws IOException {
        final File created = File.createTempFile("hex", null);
        created.deleteOnExit();
        try (OutputStream output = new FileOutputStream(created)) {
            final byte[] array = new byte[current().nextInt(128)];
            final int count = current().nextInt(128);
            for (int i = 0; i < count; i++) {
                current().nextBytes(array);
                output.write(array);
            }
            output.flush();
        }
        final File encoded = File.createTempFile("hex", null);
        encoded.deleteOnExit();
        try (InputStream in = new FileInputStream(created)) {
            try (OutputStream out = new HexEncoderStream(
                    new FileOutputStream(encoded), new HexEncoderImpl())) {
                final long copied = IOUtils.copyLarge(in, out);
                assertEquals(copied, created.length());
                out.flush();
            }
        }
        final File decoded = File.createTempFile("hex", null);
        decoded.deleteOnExit();
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

    private transient final Logger logger = getLogger(getClass());
}
