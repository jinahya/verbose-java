package com.github.jinahya.verbose.hex;

import static com.github.jinahya.verbose.hex.IoUtils.copy;
import static com.github.jinahya.verbose.hex.MdUtils.digest;
import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.FileChannel;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import static java.nio.file.Files.createTempFile;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.DSYNC;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * Test class for testing encoding/decoding.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexChannelTest {

    /**
     * Encodes/decodes a randomly generated path.
     *
     * @throws IOException if an I/O error occurs.
     * @throws NoSuchAlgorithmException if failed to digest paths.
     */
    @Test
    public void encodeDecodePath()
            throws IOException, NoSuchAlgorithmException {
        final Path created = createTempFile(null, null);
        created.toFile().deleteOnExit();
        try (FileChannel c = open(created, WRITE)) {
            final ByteBuffer b = allocate(current().nextInt(1048576));
            current().nextBytes(b.array());
            c.write(b);
            c.force(false);
        }
        final Path encoded = createTempFile(null, null);
        encoded.toFile().deleteOnExit();
        try (ReadableByteChannel readable = open(created, READ)) {
            final WritableByteChannel channel = open(encoded, WRITE, DSYNC);
            final HexEncoder encoder = new HexEncoderImpl();
            final int capacity = current().nextInt(2, 128);
            final boolean direct = current().nextBoolean();
            try (WritableByteChannel writable = new WritableHexChannelEx(
                    channel, encoder, capacity, direct)) {
                copy(readable, writable);
            }
        }
        final Path decoded = createTempFile(null, null);
        decoded.toFile().deleteOnExit();
        try (FileChannel writable = open(decoded, WRITE, DSYNC)) {
            final ReadableByteChannel channel = open(encoded, READ);
            final HexDecoder decoder = new HexDecoderImpl();
            final int capacity = current().nextInt(2, 128);
            final boolean direct = current().nextBoolean();
            try (ReadableByteChannel readable = new ReadableHexChannelEx(
                    channel, decoder, capacity, direct)) {
                copy(readable, writable);
            }
        }
        for (final String algorithm : new String[]{"MD5", "SHA-1", "SHA-256"}) {
            final byte[] createdDigest = digest(created, algorithm);
            final byte[] decodedDigest = digest(decoded, algorithm);
            assertEquals(decodedDigest, createdDigest);
        }
    }
}
