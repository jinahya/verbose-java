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
package com.github.jinahya.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Utilities for testing hex codec.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class IoUtils {

    public static long copy(final InputStream input, final OutputStream output)
            throws IOException {
        switch (current().nextInt(1)) {
            default:
                return IoUtils1.copy(input, output);
        }
    }

    public static void copy(final File source, final File target)
            throws IOException {
        switch (current().nextInt(1)) {
            default:
                IoUtils1.copy(source, target);
        }
    }

    public static long copy(final ReadableByteChannel readable,
                            final WritableByteChannel writable)
            throws IOException {
        switch (current().nextInt(1)) {
            default:
                return IoUtils2.copy(readable, writable);
        }
    }

    public static void copy(final Path source, final Path target)
            throws IOException {
        switch (current().nextInt(1)) {
            default:
                IoUtils2.copy(source, target);
                break;
        }
    }

    private IoUtils() {
        super();
    }

}
