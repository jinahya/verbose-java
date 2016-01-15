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
package com.github.jinahya.verbose.hex;

import static com.github.jinahya.verbose.hex.HexCodecTests.fillFile;
import static com.github.jinahya.verbose.hex.HexCodecTests.tempFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.io.FileUtils.contentEquals;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexCodecChannelTest {

    private static long copy(final ReadableByteChannel readable,
                             final WritableByteChannel writable)
            throws IOException {
        long count = 0;
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        for (int read; (read = readable.read(buffer)) != -1;) {
            for (buffer.flip(); buffer.hasRemaining();) {
                writable.write(buffer);
            }
            buffer.compact();
            count += read;
        }
        return count;
    }

    @Test
    public void buffered() throws IOException {
    }

    @Test
    public void file() throws IOException {
        final Path createdPath = fillFile(tempFile()).toPath();
        final Path encodedPath = tempFile().toPath();
        try (FileChannel readable = open(createdPath, READ)) {
            try (WritableByteChannel writable = new HexEncoderChannel(
                    open(encodedPath, WRITE), new HexEncoderDemo(),
                    current().nextInt(2, 128), current().nextBoolean())) {
                copy(readable, writable);
            }
        }
        final Path decodedPath = tempFile().toPath();
        try (ReadableByteChannel readable = new HexDecoderChannel(
                open(encodedPath, READ), new HexDecoderDemo(),
                current().nextInt(2, 128), current().nextBoolean())) {
            try (WritableByteChannel writable = open(decodedPath, WRITE)) {
                copy(readable, writable);
            }
        }
        contentEquals(decodedPath.toFile(), createdPath.toFile());
    }

    private transient final Logger logger = getLogger(getClass());

}
