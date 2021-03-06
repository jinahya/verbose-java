package com.github.jinahya.verbose.hex;

import static com.github.jinahya.verbose.util.IoUtils.copy;
import static com.github.jinahya.verbose.util.MdUtils.digest;
import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
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

    /**
     * Provides algorithm names.
     *
     * @return algorithm names
     */
    @DataProvider
    private Object[][] algorithms() {
        return new Object[][]{{"MD5"}, {"SHA-1"}, {"SHA-256"}};
    }

    /**
     * Tests {@link WritableHexChannel} and {@link ReadableHexChannel} by coping
     * files.
     *
     * @param algorithm the algorithm
     * @throws IOException if an I/O error occurs.
     * @throws NoSuchAlgorithmException if the provided algorithm is unknown.
     */
    @Test(dataProvider = "algorithms")
    public void encodeDecodePath(final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        final Path created = createTempFile(null, null);
        try (FileChannel c = open(created, WRITE, DSYNC)) { // <2>
            final ByteBuffer b = allocate(current().nextInt(1024));
            current().nextBytes(b.array());
            for (; b.hasRemaining(); c.write(b));
        }
        final byte[] createdDigest = digest(
                algorithm, created, allocate(current().nextInt(1, 1024)));
        final Path encoded = createTempFile(null, null);
        try (ReadableByteChannel readable
                = open(created, READ, DELETE_ON_CLOSE)) { // <2>
            final WritableByteChannel channel = open(encoded, WRITE, DSYNC);
            try (WritableByteChannel writable = new WritableHexChannel(
                    () -> channel, HexEncoderImpl::new)) {
                copy(readable, writable, allocate(current().nextInt(1, 1024)));
            }
        }
        final Path decoded = createTempFile(null, null);
        try (WritableByteChannel writable = open(decoded, WRITE, DSYNC)) {
            final ReadableByteChannel channel
                    = open(encoded, READ, DELETE_ON_CLOSE);
            try (ReadableByteChannel readable = new ReadableHexChannel(
                    () -> channel, HexDecoderImpl::new)) {
                copy(readable, writable, allocate(current().nextInt(1, 1024)));
            }
        }
        final byte[] decodedDigest = digest(
                algorithm, decoded, allocate(current().nextInt(1, 1024)));
        assertEquals(decodedDigest, createdDigest);
        assertFalse(exists(created)); // <1>
        assertFalse(exists(encoded)); // <2>
        delete(decoded); // <3>
    }
}
