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
package com.github.jinahya.verbose.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.invoke.MethodHandles.lookup;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import static java.security.MessageDigest.getInstance;
import java.security.NoSuchAlgorithmException;
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * Utilities for {@link MessageDigest} with {@code java.io} package.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class MdUtils1 {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    // -------------------------------------------------------------------------
    private static byte[] digest1(final String algorithm,
                                  final InputStream input, final byte[] buffer)
            throws NoSuchAlgorithmException, IOException {
        // @todo: validate arguments!
        final MessageDigest digest = getInstance(algorithm); // <1>
        for (int length; (length = input.read(buffer)) != -1;
             digest.update(buffer, 0, length)); // <2>
        return digest.digest(); // <3>
    }

    private static byte[] digest2(final String algorithm, InputStream input,
                                  final byte[] buffer)
            throws NoSuchAlgorithmException, IOException {
        // @todo: validate arguments
        input = new DigestInputStream(input, getInstance(algorithm)); // <1>
        while (input.read(buffer) != -1); // <2>
        return ((DigestInputStream) input).getMessageDigest().digest(); // <3>
    }

    static byte[] digest(final String algorithm, final InputStream input,
                         final byte[] buffer)
            throws NoSuchAlgorithmException, IOException {
        switch (current().nextInt(2)) {
            case 0:
                return digest1(algorithm, input, buffer);
            default:
                return digest2(algorithm, input, buffer);
        }
    }

    private static byte[] digest1(final String algorithm, final File file,
                                  final byte[] buffer)
            throws IOException, NoSuchAlgorithmException {
        // @todo: validate arguments
        try (InputStream input = new FileInputStream(file)) {
            return digest(algorithm, input, buffer); // <1>
        }
    }

    static byte[] digest(final String algorithm, final File file,
                         final byte[] buffer)
            throws IOException, NoSuchAlgorithmException {
        // @todo: validate arguments
        switch (current().nextInt(1)) {
            default:
                return digest1(algorithm, file, buffer);
        }
    }

    // -------------------------------------------------------------------------
    private MdUtils1() {
        super();
    }
}
