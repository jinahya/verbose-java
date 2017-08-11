package com.github.jinahya.verbose.hex;

import static com.github.jinahya.verbose.util.IoUtils.copy;
import static com.github.jinahya.verbose.util.MdUtils.digest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.Channels.newChannel;
import java.nio.channels.FileChannel;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.exists;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.DELETE_ON_CLOSE;
import static java.nio.file.StandardOpenOption.DSYNC;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test class for testing encoding/decoding using channels.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexChannelTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    /**
     * Provides algorithm identifiers.
     *
     * @return an array of algorithm identifies
     */
    @DataProvider
    private Object[][] algorithms() {
        return new Object[][]{{"MD5"}, {"SHA-1"}, {"SHA-256"}};
    }

    /**
     * Tests {@link HexEncoderImpl} and {@link HexDecoderImpl} using
     * {@link WritableHexChannel} and {@link ReadableHexChannel}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void encodedDecodeChannel() throws IOException {
        final byte[] created;
        {
            created = new byte[current().nextInt(1024)];
            current().nextBytes(created);
        }
        final byte[] encoded;
        {
            final ByteArrayOutputStream baos
                    = new ByteArrayOutputStream(created.length << 1);
            try (ReadableByteChannel r
                    = newChannel(new ByteArrayInputStream(created));
                 WritableByteChannel w = new WritableHexChannel(
                         () -> newChannel(baos), () -> new HexEncoderImpl())) {
                copy(r, w, allocate(current().nextInt(1, 128)));
            }
            encoded = baos.toByteArray();
            assertEquals(encoded.length, created.length << 1);
        }
        final byte[] decoded;
        {
            final ByteArrayOutputStream baos
                    = new ByteArrayOutputStream(encoded.length >> 1);
            try (ReadableByteChannel r = new ReadableHexChannel(
                    () -> newChannel(new ByteArrayInputStream(encoded)),
                    () -> new HexDecoderImpl());
                 WritableByteChannel w = newChannel(baos)) {
                copy(r, w, allocate(current().nextInt(1, 128)));
            }
            decoded = baos.toByteArray();
            assertEquals(decoded.length, encoded.length >> 1);
        }
        assertEquals(decoded, created);
    }

    /**
     * Tests {@link HexEncoderImpl} and {@link HexDecoderImpl} using
     * {@link WritableHexChannel} and {@link ReadableHexChannel} on paths.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(dataProvider = "algorithms")
    public void encodeDecodePath(final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        final Path createdPath; // <1>
        final byte[] createdDigest;
        {
            createdPath = createTempFile(null, null);
            try (FileChannel c = open(createdPath, WRITE, DSYNC)) {
                final ByteBuffer b = allocate(current().nextInt(1024));
                current().nextBytes(b.array());
                for (; b.hasRemaining(); c.write(b));
            }
            createdDigest = digest(algorithm, createdPath,
                                   allocate(current().nextInt(1, 128)));
        }
        final Path encodedPath; // <2>
        {
            encodedPath = createTempFile(null, null);
            try (ReadableByteChannel readable
                    = open(createdPath, READ, DELETE_ON_CLOSE)) {
                final WritableByteChannel channel
                        = open(encodedPath, WRITE, DSYNC);
                try (WritableByteChannel writable
                        = new WritableHexChannel(
                                () -> channel, HexEncoderImpl::new)) {
                    copy(readable, writable,
                         allocate(current().nextInt(1, 128)));
                }
            }
        }
        final Path decodedPath;
        final byte[] decodedDigest;
        {
            decodedPath = createTempFile(null, null);
            try (WritableByteChannel w = open(decodedPath, WRITE, DSYNC)) {
                final ReadableByteChannel channel
                        = open(encodedPath, READ, DELETE_ON_CLOSE);
                try (ReadableByteChannel r = new ReadableHexChannel(
                        () -> channel, HexDecoderImpl::new)) {
                    copy(r, w, allocate(current().nextInt(1, 128)));
                }
            }
            decodedDigest = digest(algorithm, decodedPath,
                                   allocate(current().nextInt(1, 128)));
        }
        assertEquals(decodedDigest, createdDigest);
        assertFalse(exists(createdPath));
        assertFalse(exists(encodedPath));
        delete(decodedPath);
    }
}
