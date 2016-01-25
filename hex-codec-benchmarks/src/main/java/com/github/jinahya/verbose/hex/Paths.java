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

import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.FileChannel;
import static java.nio.channels.FileChannel.open;
import java.nio.file.Files;
import static java.nio.file.Files.delete;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@State(Scope.Benchmark)
public class Paths {

    private static Path mega(final Path path) throws IOException {
        try (FileChannel channel = open(path, WRITE, APPEND)) {
            final ByteBuffer buffer = allocate(1048576);
            current().nextBytes(buffer.array());
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            channel.force(false);
        }
        return path;
    }

    private static Path temp() throws IOException {
        final Path path = Files.createTempFile(null, null);
        path.toFile().deleteOnExit();
        return path;
    }

    @Setup
    public void setup() throws IOException {
        created = mega(temp());
        encoded = mega(mega(temp()));
        decoded = mega(temp());
    }

    @TearDown
    public void tearDown() throws IOException {
        delete(created);
        delete(encoded);
        delete(decoded);
    }

    Path created;

    Path encoded;

    Path decoded;

}
