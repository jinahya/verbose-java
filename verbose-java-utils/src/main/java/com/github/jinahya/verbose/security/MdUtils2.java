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
package com.github.jinahya.verbose.security;

import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.READ;
import java.security.MessageDigest;
import static java.security.MessageDigest.getInstance;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;

public final class MdUtils2 {

    private static byte[] digest1(final ReadableByteChannel channel,
                                  final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest = getInstance(algorithm);
        for (final ByteBuffer b = allocate(4096); channel.read(b) != -1;) {
            b.flip();
            digest.update(b); // <1>
            b.clear();
        }
        return digest.digest();
    }

    static byte[] digest(final ReadableByteChannel channel,
                         final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        switch (current().nextInt(1)) {
            default:
                return digest1(channel, algorithm);
        }
    }

    private static byte[] digest1(final Path path, final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        try (ReadableByteChannel channel = open(path, READ)) {
            return digest(channel, algorithm);
        }
    }

    static byte[] digest(final Path path, final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        switch (current().nextInt(1)) {
            default:
                return digest1(path, algorithm);
        }
    }

    private MdUtils2() {
        super();
    }

}