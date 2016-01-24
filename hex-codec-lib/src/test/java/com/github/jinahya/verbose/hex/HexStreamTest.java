package com.github.jinahya.verbose.hex;

import static com.github.jinahya.verbose.hex.IoUtils.copy;
import static com.github.jinahya.verbose.hex.MdUtils.digest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * Test class for testing encoding/decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexStreamTest {

    /**
     * Encodes/decodes a randomly generated file.
     *
     * @throws IOException if an I/O error occurs.
     * @throws NoSuchAlgorithmException if failed to digest files
     */
    @Test
    public void encodeDecodeFile()
            throws IOException, NoSuchAlgorithmException {
        final File created = File.createTempFile("tmp", null); // <1>
        created.deleteOnExit();
        try (OutputStream o = new FileOutputStream(created)) { // <2>
            final byte[] b = new byte[current().nextInt(1048576)];
            current().nextBytes(b);
            o.write(b);
        }
        final File encoded = File.createTempFile("tmp", null); // <3>
        encoded.deleteOnExit();
        try (InputStream input = new FileInputStream(created)) { // <4>
            final OutputStream stream = new FileOutputStream(encoded);
            final HexEncoder encoder = new HexEncoderImpl();
            try (OutputStream output = new HexOutputStream(stream, encoder)) {
                copy(input, output);
                output.flush();
            }
        }
        final File decoded = File.createTempFile("tmp", null); // <5>
        decoded.deleteOnExit();
        try (OutputStream output = new FileOutputStream(decoded)) { // <6>
            final InputStream stream = new FileInputStream(encoded);
            final HexDecoder decoder = new HexDecoderImpl();
            try (InputStream input = new HexInputStream(stream, decoder)) {
                copy(input, output);
                output.flush();
            }
        }
        for (final String algorithm : new String[]{"MD5", "SHA-1", "SHA-256"}) { // <7>
            final byte[] createdDigest = digest(created, algorithm);
            final byte[] decodedDigest = digest(decoded, algorithm);
            assertEquals(decodedDigest, createdDigest);
        }
    }
}
