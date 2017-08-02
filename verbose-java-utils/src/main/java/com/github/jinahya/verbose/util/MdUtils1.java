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
public final class MdUtils1 {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    // -------------------------------------------------------------------------
    private static byte[] digest1(final InputStream input,
                                  final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        final MessageDigest digest = getInstance(algorithm); // <1>
        final byte[] buffer = new byte[4096];
        for (int length; (length = input.read(buffer)) != -1;) {
            digest.update(buffer, 0, length); // <2>
        }
        return digest.digest(); // <3>
    }

    private static byte[] digest2(InputStream input, final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        input = new DigestInputStream(input, getInstance(algorithm)); // <1>
        for (final byte[] b = new byte[4096]; input.read(b) != -1;); // <2>
        return ((DigestInputStream) input).getMessageDigest().digest(); // <3>
    }

    /**
     * Digests all bytes from given stream for specified algorithm.
     *
     * @param input the input stream
     * @param algorithm the algorithm name
     * @return a message digest
     * @throws NoSuchAlgorithmException if {@code algorithm} is unknown.
     * @throws IOException if an I/O error occurs.
     */
    static byte[] digest(final InputStream input, final String algorithm)
            throws NoSuchAlgorithmException, IOException {
        switch (current().nextInt(2)) {
            case 0:
                return digest1(input, algorithm);
            default:
                return digest2(input, algorithm);
        }
    }

    private static byte[] digest1(final File file, final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        try (InputStream stream = new FileInputStream(file)) {
            return digest(stream, algorithm);
        }
    }

    /**
     * Digests all bytes from given file for specified algorithm.
     *
     * @param input the input file
     * @param algorithm the algorithm name
     * @return a message digest
     * @throws NoSuchAlgorithmException if {@code algorithm} is unknown.
     * @throws IOException if an I/O error occurs.
     */
    static byte[] digest(final File file, final String algorithm)
            throws IOException, NoSuchAlgorithmException {
        switch (current().nextInt(1)) {
            default:
                return digest1(file, algorithm);
        }
    }

    // -------------------------------------------------------------------------
    private MdUtils1() {
        super();
    }

}
