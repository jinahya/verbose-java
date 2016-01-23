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

import static com.github.jinahya.verbose.hex.MdUtils.digest;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * A class testing {@link IoUtils}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class IoUtilsTest {

    @Test(invocationCount = 128)
    public void copyFile() throws IOException, NoSuchAlgorithmException {
        final File source = File.createTempFile("tmp", null);
        source.deleteOnExit();
        try (RandomAccessFile raf = new RandomAccessFile(source, "rwd")) {
            final byte[] bytes = new byte[current().nextInt(1048576)];
            current().nextBytes(bytes);
            raf.write(bytes);
        }
        final File target = File.createTempFile("tmp", null);
        target.deleteOnExit();
        if (current().nextBoolean()) {
            try (RandomAccessFile raf = new RandomAccessFile(target, "rwd")) {
                final byte[] bytes = new byte[current().nextInt(1048576)];
                current().nextBytes(bytes);
                raf.write(bytes);
            }
        }
        IoUtils.copy(source, target);
        for (final String algorithm : new String[]{"MD5", "SHA-1", "SHA-256"}) {
            final byte[] createdDigest = digest(source, algorithm);
            final byte[] decodedDigest = digest(target, algorithm);
            assertEquals(decodedDigest, createdDigest);
        }
    }

    @Test(invocationCount = 128)
    public void copyPath() throws IOException, NoSuchAlgorithmException {
        final Path source = Files.createTempFile(null, null);
        source.toFile().deleteOnExit();
        try (RandomAccessFile raf
                = new RandomAccessFile(source.toFile(), "rwd")) {
            final byte[] bytes = new byte[current().nextInt(1048576)];
            current().nextBytes(bytes);
            raf.write(bytes);
        }
        final Path target = Files.createTempFile(null, null);
        target.toFile().deleteOnExit();
        if (current().nextBoolean()) {
            try (RandomAccessFile raf
                    = new RandomAccessFile(target.toFile(), "rwd")) {
                final byte[] bytes = new byte[current().nextInt(1048576)];
                current().nextBytes(bytes);
                raf.write(bytes);
            }
        }
        IoUtils.copy(source, target);
        for (final String algorithm : new String[]{"MD5", "SHA-1", "SHA-256"}) {
            final byte[] createdDigest = digest(source, algorithm);
            final byte[] decodedDigest = digest(target, algorithm);
            assertEquals(decodedDigest, createdDigest);
        }
    }
}
