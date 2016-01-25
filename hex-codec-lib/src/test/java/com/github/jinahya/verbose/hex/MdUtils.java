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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.READ;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Utilities for testing hex codec.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class MdUtils {

    private static byte[] digest1(final InputStream input,
                                  final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest // <1>
                = MessageDigest.getInstance(algorithm);
        final byte[] buffer = new byte[4096];
        for (int read; (read = input.read(buffer)) != -1;) {
            digest.update(buffer, 0, read); // <2>
        }
        return digest.digest(); // <3>
    }

    private static byte[] digest2(InputStream input, final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        input = new DigestInputStream( // <1>
                input, MessageDigest.getInstance(algorithm));
        for (final byte[] b = new byte[4096]; input.read(b) != -1;); // <2>
        return ((DigestInputStream) input).getMessageDigest().digest();
    }

    static byte[] digest(final InputStream input, final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        switch (current().nextInt(2)) {
            case 0:
                return digest1(input, algorithm);
            default:
                return digest2(input, algorithm);
        }
    }

    static byte[] digest(final File file, final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        try (InputStream stream = new FileInputStream(file)) {
            return digest(stream, algorithm);
        }
    }

    static byte[] digest(final ReadableByteChannel channel,
                         final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest = MessageDigest.getInstance(algorithm);
        final ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (channel.read(buffer) != -1) { // <1>
            buffer.flip();
            digest.update(buffer); // <2>
            buffer.clear();
        }
        return digest.digest();
    }

    static byte[] digest(final Path path, final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        try (ReadableByteChannel channel = open(path, READ)) {
            return digest(channel, algorithm);
        }
    }

    private MdUtils() {
        super();
    }

}
