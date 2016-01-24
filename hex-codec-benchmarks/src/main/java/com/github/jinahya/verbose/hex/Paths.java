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
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@State(Scope.Benchmark)
public class Paths {

    private static Path mega(final Path path) throws IOException {
        try (RandomAccessFile raw
                = new RandomAccessFile(path.toFile(), "rwd")) {
            raw.setLength(raw.length() + 1048576);
            raw.getFD().sync();
        }
        return path;
    }

    private static Path temp() throws IOException {
        final Path path = Files.createTempFile(null, null);
        path.toFile().deleteOnExit();
        return path;
    }

    public Paths() {
        super();
        try {
            created = mega(temp());
            encoded = mega(mega(temp()));
            decoded = mega(temp());
        } catch (final IOException ieo) {
            throw new RuntimeException(ieo);
        }
    }

    final Path created;

    final Path encoded;

    final Path decoded;

}
