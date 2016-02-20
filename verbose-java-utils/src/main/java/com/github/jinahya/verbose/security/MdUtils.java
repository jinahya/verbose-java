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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class MdUtils {

    public static byte[] digest(final InputStream stream,
                                final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        switch (current().nextInt(1)) {
            default:
                return MdUtils1.digest(stream, algorithm);
        }
    }

    public static byte[] digest(final File file, final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        switch (current().nextInt(1)) {
            default:
                return MdUtils1.digest(file, algorithm);
        }
    }

    public static byte[] digest(final ReadableByteChannel channel,
                                final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        switch (current().nextInt(1)) {
            default:
                return MdUtils2.digest(channel, algorithm);
        }
    }

    public static byte[] digest(final Path path, final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        switch (current().nextInt(1)) {
            default:
                return MdUtils2.digest(path, algorithm);
        }
    }

    private MdUtils() {
        super();
    }
}
