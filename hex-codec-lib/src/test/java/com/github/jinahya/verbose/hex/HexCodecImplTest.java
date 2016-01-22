package com.github.jinahya.verbose.hex;

import static com.github.jinahya.verbose.hex.HexCodecTests.copy;
import static com.github.jinahya.verbose.hex.HexCodecTests.digest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import static java.nio.ByteBuffer.wrap;
import java.nio.channels.FileChannel;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import static java.nio.charset.StandardCharsets.US_ASCII;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.DSYNC;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomAscii;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
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
        try (RandomAccessFile raf = new RandomAccessFile(created, "rwd")) { // <2>
            raf.setLength(current().nextLong(1048576));
        }
        final File encoded = File.createTempFile("tmp", null); // <3>
        encoded.deleteOnExit();
        try (InputStream input = new FileInputStream(created)) { // <4>
            try (OutputStream output = new HexOutputStream(
                    new FileOutputStream(encoded), new HexEncoderImpl())) {
                final long copied = copy(input, output);
                assertEquals(copied, created.length());
            }
        }
        assertEquals(encoded.length(), created.length() << 1); // <5>
        final File decoded = File.createTempFile("tmp", null);
        decoded.deleteOnExit();
        try (InputStream input = new HexInputStream( // <6>
                new FileInputStream(encoded), new HexDecoderImpl())) {
            try (OutputStream output = new FileOutputStream(decoded)) {
                final long copied = copy(input, output);
                assertEquals(copied, created.length());
                output.flush();
            }
        }
        for (final String algorithm : new String[]{"MD5", "SHA-1", "SHA-256"}) { // <7>
            final byte[] createdDigest = digest(created, algorithm);
            final byte[] decodedDigest = digest(decoded, algorithm);
            assertEquals(decodedDigest, createdDigest);
        }
    }

    /**
     * Encodes/decodes a randomly generated path.
     *
     * @throws IOException if an I/O error occurs.
     * @throws NoSuchAlgorithmException if failed to digest paths.
     */
    @Test
    public void encodeDecodePath()
            throws IOException, NoSuchAlgorithmException {
        final Path created = Files.createTempFile(null, null); // <1>
        created.toFile().deleteOnExit();
        try (RandomAccessFile raf // <2>
                = new RandomAccessFile(created.toFile(), "rwd")) {
            raf.setLength(current().nextLong(1048576));
        }
        final Path encoded = Files.createTempFile(null, null); // <3>
        encoded.toFile().deleteOnExit();
        try (ReadableByteChannel readable = open(created, READ)) { // <4>
            try (WritableHexChannel writable = new WritableHexChannel(
                    open(encoded, WRITE, DSYNC), new HexEncoderImpl(),
                    current().nextInt(2, 128), current().nextBoolean())) {
                final long copied = copy(readable, writable);
                assertEquals(copied, Files.size(created));
            }
        }
        assertEquals(Files.size(encoded), Files.size(created) << 1);
        final Path decoded = Files.createTempFile(null, null); // <5>
        decoded.toFile().deleteOnExit();
        try (ReadableByteChannel readable = new ReadableHexChannel( // <6>
                open(encoded, READ), new HexDecoderImpl(),
                current().nextInt(2, 128), current().nextBoolean())) {
            try (FileChannel writable = open(decoded, WRITE, DSYNC)) {
                final long copied = copy(readable, writable);
                assertEquals(copied, Files.size(created));
            }
        }
        for (final String algorithm : new String[]{"MD5", "SHA-1", "SHA-256"}) { // <7>
            final byte[] createdDigest = digest(created, algorithm);
            final byte[] decodedDigest = digest(decoded, algorithm);
            assertEquals(decodedDigest, createdDigest);
        }
    }

    private transient final Logger logger = getLogger(getClass());
}
