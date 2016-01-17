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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexCodecTests {

    static File tempFile() throws IOException {
        final File file = File.createTempFile("hex", null);
        file.deleteOnExit();
        return file;
    }

    static File fillFile(final File file) throws IOException {
        try (OutputStream output = new FileOutputStream(file)) {
            final byte[] array = new byte[current().nextInt(128)];
            final int count = current().nextInt(128);
            for (int i = 0; i < count; i++) {
                current().nextBytes(array);
                output.write(array);
            }
            output.flush();
        }
        return file;
    }

    private HexCodecTests() {
        super();
    }

}
