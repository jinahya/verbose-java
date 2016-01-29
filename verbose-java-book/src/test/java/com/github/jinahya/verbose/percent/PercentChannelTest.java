/*
 * Copyright 2016 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jinahya.verbose.percent;

import static com.google.common.io.ByteStreams.copy;
import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.size;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.DSYNC;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.ServiceLoader.load;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.io.FileUtils.contentEquals;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class PercentChannelTest {

    @Test(invocationCount = 1)
    public void encodedDecodePath() throws IOException {
        final Path created = createTempFile(null, null);
        created.toFile().deleteOnExit();
        try (WritableByteChannel opened = open(created, WRITE, DSYNC)) {
            final ByteBuffer b = allocate(current().nextInt(1024));
            current().nextBytes(b.array());
            while (b.hasRemaining()) {
                opened.write(b);
            }
        }
        final Path encoded = createTempFile(null, null);
        encoded.toFile().deleteOnExit();
        try (ReadableByteChannel readable = open(created, READ);
             WritableByteChannel writable = new WritablePercentChannel(
                     open(encoded, WRITE, DSYNC),
                     load(PercentEncoder.class).iterator().next())) {
            final long copied = copy(readable, writable);
            assertEquals(copied, size(created));
        }
        final Path decoded = createTempFile(null, null);
        decoded.toFile().deleteOnExit();
        try (ReadableByteChannel readable = new ReadablePercentChannel(
                open(encoded, READ),
                load(PercentDecoder.class).iterator().next());
             WritableByteChannel writable = open(decoded, WRITE, DSYNC)) {
            final long copied = copy(readable, writable);
            assertEquals(copied, size(created));
        }
        contentEquals(decoded.toFile(), created.toFile());
    }
}
