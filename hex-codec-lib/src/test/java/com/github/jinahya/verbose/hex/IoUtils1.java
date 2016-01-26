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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class IoUtils1 {

    static long copy(final InputStream input, final OutputStream output)
            throws IOException {
        long count = 0L;
        final byte[] buffer = new byte[4096]; // <1>
        for (int length; (length = input.read(buffer)) != -1; count += length) { // <2>
            output.write(buffer, 0, length); // <3>
        }
        return count;
    }

    static void copy(final File source, final File target) throws IOException {
        try (InputStream input = new FileInputStream(source);
             OutputStream output = new FileOutputStream(target)) {
            copy(input, output);
            output.flush(); // <1>
        }
    }

    private IoUtils1() {
        super();
    }

}
