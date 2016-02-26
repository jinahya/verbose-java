package com.github.jinahya.verbose.hex;

import static com.github.jinahya.verbose.io.IoUtils.copy;
import static com.github.jinahya.verbose.security.MdUtils.digest;
import java.io.File;
import static java.io.File.createTempFile;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import static java.util.Arrays.asList;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * Test class for testing encoding/decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexStreamTest {

    @Test
    public void encodeDecodeFile()
            throws IOException, NoSuchAlgorithmException {
        final File created = createTempFile("tmp", null);
        created.deleteOnExit(); // <1>
        try (OutputStream o = new FileOutputStream(created)) {
            final byte[] b = new byte[current().nextInt(1024)];
            current().nextBytes(b);
            o.write(b);
        }
        final File encoded = createTempFile("tmp", null);
        encoded.deleteOnExit();
        try (InputStream input = new FileInputStream(created)) {
            final OutputStream out = new FileOutputStream(encoded);
            final HexEncoder enc = new HexEncoderImpl();
            try (OutputStream output = new HexOutputStream(out, enc)) {
                copy(input, output);
                output.flush();
            }
        }
        final File decoded = createTempFile("tmp", null);
        decoded.deleteOnExit();
        try (OutputStream output = new FileOutputStream(decoded)) {
            final InputStream in = new FileInputStream(encoded);
            final HexDecoder dec = new HexDecoderImpl();
            try (InputStream input = new HexInputStream(in, dec)) {
                copy(input, output);
                output.flush();
            }
        }
        assertEquals(encoded.length(), created.length() << 1);
        assertEquals(decoded.length(), encoded.length() >> 1);
        for (final String algorithm : asList("MD5", "SHA-1", "SHA-256")) {
            final byte[] createdDigest = digest(created, algorithm);
            final byte[] decodedDigest = digest(decoded, algorithm);
            assertEquals(decodedDigest, createdDigest);
        }
    }
}
