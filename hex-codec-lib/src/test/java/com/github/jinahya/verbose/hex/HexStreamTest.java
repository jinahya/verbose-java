package com.github.jinahya.verbose.hex;

import static com.github.jinahya.verbose.util.IoUtils.copy;
import static com.github.jinahya.verbose.util.MdUtils.digest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import static java.io.File.createTempFile;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.invoke.MethodHandles.lookup;
import java.security.NoSuchAlgorithmException;
import static java.util.Arrays.asList;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * A test class for testing encoding/decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexStreamTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    /**
     * Tests {@link HexEncoderImpl} and {@link HexDecoderImpl} using
     * {@link HexOutputStream} and {@link HexInputStream}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void encodeDecodeStream() throws IOException {
        final byte[] created;
        {
            created = new byte[current().nextInt(1024)];
            current().nextBytes(created);
        }
        final byte[] encoded;
        {
            final ByteArrayOutputStream baos
                    = new ByteArrayOutputStream(created.length << 1);
            try (InputStream i = new ByteArrayInputStream(created);
                 OutputStream o = new HexOutputStream(
                         baos, new HexEncoderImpl())) {
                copy(i, o, new byte[current().nextInt(1, 128)]);
            }
            encoded = baos.toByteArray();
            assertEquals(encoded.length, created.length << 1);
        }
        final byte[] decoded; // <3>
        {
            final ByteArrayOutputStream baos
                    = new ByteArrayOutputStream(encoded.length >> 1);
            try (InputStream i = new HexInputStream(
                    new ByteArrayInputStream(encoded), new HexDecoderImpl())) {
                copy(i, baos, new byte[current().nextInt(1, 128)]);
            }
            decoded = baos.toByteArray();
            assertEquals(decoded.length, encoded.length >> 1);
        }
        assertEquals(decoded, created);
    }

    /**
     * Tests {@link HexEncoderImpl} and {@link HexDecoderImpl} using
     * {@link HexOutputStream} and {@link HexInputStream} on files.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void encodeDecodeFile()
            throws IOException, NoSuchAlgorithmException {
        final File created;
        {
            created = createTempFile("tmp", null);
            created.deleteOnExit();
            try (OutputStream o = new FileOutputStream(created)) {
                final byte[] b = new byte[current().nextInt(1024)];
                current().nextBytes(b);
                o.write(b);
                o.flush();
            }
        }
        final File encoded;
        {
            encoded = createTempFile("tmp", null);
            encoded.deleteOnExit();
            try (InputStream input = new FileInputStream(created)) {
                final OutputStream out = new FileOutputStream(encoded);
                final HexEncoder enc = new HexEncoderImpl();
                try (OutputStream output = new HexOutputStream(out, enc)) {
                    copy(input, output, new byte[current().nextInt(1, 128)]);
                    output.flush();
                }
            }
        }
        final File decoded;
        {
            decoded = createTempFile("tmp", null);
            decoded.deleteOnExit();
            try (OutputStream output = new FileOutputStream(decoded)) {
                final InputStream in = new FileInputStream(encoded);
                final HexDecoder dec = new HexDecoderImpl();
                try (InputStream input = new HexInputStream(in, dec)) {
                    copy(input, output, new byte[current().nextInt(1, 128)]);
                    output.flush();
                }
            }
        }
        assertEquals(encoded.length(), created.length() << 1);
        assertEquals(decoded.length(), encoded.length() >> 1);
        final byte[] buffer = new byte[current().nextInt(1, 128)];
        for (final String algorithm : asList("MD5", "SHA-1", "SHA-256")) {
            final byte[] createdDigest = digest(algorithm, created, buffer);
            final byte[] decodedDigest = digest(algorithm, decoded, buffer);
            assertEquals(decodedDigest, createdDigest);
        }
        created.delete();
        encoded.delete();
        decoded.delete();
    }
}
