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

import static com.github.jinahya.verbose.security.MdUtils.digest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.WRITE;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * A class for testing {@link MdUtils}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class MdUtilsTest {

    @DataProvider
    private Object[][] algorithms() {
        return new Object[][]{{"MD5"}, {"SHA-1"}, {"SHA-256"}};
    }

    @Test(dataProvider = "algorithms", invocationCount = 128)
    public static void digestFile(final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        final File file = File.createTempFile("tmp", null);
        file.deleteOnExit();
        try (OutputStream s = new FileOutputStream(file)) {
            final byte[] b = new byte[current().nextInt(1048576)];
            current().nextBytes(b);
            s.write(b);
            s.flush();
        }
        final byte[] digest = digest(file, algorithm);
    }

    @Test(dataProvider = "algorithms", invocationCount = 128)
    public static void digestPath(final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        final Path path = Files.createTempFile(null, null);
        try (FileChannel c = FileChannel.open(path, WRITE)) { // <2>
            final ByteBuffer b = allocate(current().nextInt(1048576));
            current().nextBytes(b.array());
            for (; b.hasRemaining(); c.write(b));
            c.force(false);
        }
        final byte[] digest = digest(path, algorithm);
    }
}
