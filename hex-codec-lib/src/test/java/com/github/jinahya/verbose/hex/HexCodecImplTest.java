package com.github.jinahya.verbose.hex;

import static com.github.jinahya.verbose.hex.HexCodecTests.digest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import static com.github.jinahya.verbose.hex.HexCodecTests.copy1;
import static com.github.jinahya.verbose.hex.HexCodecTests.copy;
import static com.github.jinahya.verbose.hex.HexCodecTests.digest;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;

/**
 * Test class for testing encoding/decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexCodecImplTest {

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

    private static void encodedDecodeString(final String string,
                                            final Charset charset) {
        final String encoded = new HexEncoderImpl().encode(string, charset);
        final String decoded = new HexDecoderImpl().decode(encoded, charset);
        assertEquals(decoded, string);
    }

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

    @Test(invocationCount = 128)
    public void encodedDecodeString() {
        encodedDecodeString(RandomStringUtils.random(current().nextInt(128)),
                            StandardCharsets.UTF_8);
        encodedDecodeString(
                RandomStringUtils.randomAscii(current().nextInt(128)),
                StandardCharsets.US_ASCII);
    }

    @Test(invocationCount = 128)
    public void encodeDecodeStream()
            throws IOException, NoSuchAlgorithmException {
        final File createdFile = File.createTempFile("tmp", null);
        createdFile.deleteOnExit();
        final File encodedFile = File.createTempFile("tmp", null);
        encodedFile.deleteOnExit();
        try (InputStream input = new FileInputStream(createdFile)) {
            try (OutputStream output = new HexOutputStream(
                    new FileOutputStream(encodedFile), new HexEncoderImpl())) {
                final long copied = copy(input, output);
                assertEquals(copied, createdFile.length());
                output.flush();
            }
        }
        assertEquals(encodedFile.length(), createdFile.length() << 1);
        final File decodedFile = File.createTempFile("tmp", null);
        decodedFile.deleteOnExit();
        try (InputStream input = new HexInputStream(
                new FileInputStream(encodedFile), new HexDecoderImpl())) {
            try (OutputStream output = new FileOutputStream(decodedFile)) {
                final long copied = copy(input, output);
                assertEquals(copied, createdFile.length());
                output.flush();
            }
        }
        final String algorithm = "MD5";
        final byte[] createdDigest = digest(createdFile, algorithm);
        final byte[] decodedDigest = digest(decodedFile, algorithm);
        assertEquals(decodedDigest, createdDigest);
    }

    @Test(invocationCount = 128)
    public void encodeDecodeChannel()
            throws IOException, NoSuchAlgorithmException {
        final Path createdPath = Files.createTempFile(null, null);
        createdPath.toFile().deleteOnExit();
        final Path encodedPath = Files.createTempFile(null, null);
        encodedPath.toFile().deleteOnExit();
        try (FileChannel readable = FileChannel.open(
                createdPath, StandardOpenOption.READ)) {
            try (WritableHexChannel writable = new WritableHexChannel(
                    FileChannel.open(encodedPath, StandardOpenOption.WRITE),
                    new HexEncoderImpl(), current().nextInt(2, 128),
                    current().nextBoolean()) {
                @Override
                public void close() throws IOException {
                    ((FileChannel) channel).force(false);
                    super.close();
                }
            }) {
                final long copied = copy1(readable, writable);
                assertEquals(copied, Files.size(createdPath));
            }
        }
        assertEquals(Files.size(encodedPath), Files.size(createdPath) << 1);
        final Path decodedPath = Files.createTempFile(null, null);
        decodedPath.toFile().deleteOnExit();
        try (ReadableByteChannel readable = new ReadableHexChannel(
                FileChannel.open(encodedPath, StandardOpenOption.READ),
                new HexDecoderImpl(), current().nextInt(2, 128),
                current().nextBoolean())) {
            try (FileChannel writable = FileChannel.open(
                    decodedPath, StandardOpenOption.WRITE)) {
                final long copied = copy1(readable, writable);
                assertEquals(copied, Files.size(createdPath));
                writable.force(false);
            }
        }
        final String algorithm = "MD5";
        final byte[] createdDigest = digest(createdPath, algorithm);
        final byte[] decodedDigest = digest(decodedPath, algorithm);
        assertEquals(decodedDigest, createdDigest);
    }

    private transient final Logger logger = getLogger(getClass());
}
