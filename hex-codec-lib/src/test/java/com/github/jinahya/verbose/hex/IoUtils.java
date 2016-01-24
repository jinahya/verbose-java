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
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * Utilities for testing hex codec.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class IoUtils {

    static long copy(final InputStream input, final OutputStream output)
            throws IOException {
        long count = 0L;
        final byte[] buffer = new byte[4096]; // <1>
        for (int read; (read = input.read(buffer)) != -1; count += read) { // <2>
            output.write(buffer, 0, read); // <3>
        }
        return count;
    }

    static void copy(final File source, final File target) throws IOException {
        try (InputStream input = new FileInputStream(source)) {
            try (OutputStream output = new FileOutputStream(target)) {
                copy(input, output);
                output.flush(); // <1>
            }
        }
    }

    private static long copy1(final ReadableByteChannel readable,
                              final WritableByteChannel writable)
            throws IOException {
        long count = 0L;
        final ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (readable.read(buffer) != -1) { // <1>
            buffer.flip(); // <2>
            count += writable.write(buffer); // <3>
            buffer.compact(); // <4>
        }
        for (buffer.flip(); buffer.hasRemaining();) { // <5>
            count += writable.write(buffer);
        }
        return count;
    }

    private static long copy2(final ReadableByteChannel readable,
                              final WritableByteChannel writable)
            throws IOException {
        long count = 0L;
        final ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (readable.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) { // <1>
                count += writable.write(buffer);
            }
            buffer.compact(); // <2>
        }
        return count;
    }

    static long copy(final ReadableByteChannel readable,
                     final WritableByteChannel writable)
            throws IOException {
        switch (current().nextInt(2)) {
            case 0:
                return copy1(readable, writable);
            default:
                return copy2(readable, writable);
        }
    }

    private static void copy1(final Path source, final Path target)
            throws IOException {
        try (FileChannel readable = open(source, READ)) {
            try (FileChannel writable
                    = open(target, CREATE, WRITE, TRUNCATE_EXISTING)) {
                copy(readable, writable);
                writable.force(false); // <1>
            }
        }
    }

    private static void copy2(final Path source, final Path target)
            throws IOException {
        try (FileChannel readable = open(source, READ)) {
            try (FileChannel writable
                    = open(target, CREATE, WRITE, TRUNCATE_EXISTING)) {
                for (long count = readable.size(); count > 0L;) {
                    final long transferred = readable.transferTo(
                            readable.position(), count, writable);
                    readable.position(readable.position() + transferred);
                    count -= transferred;
                }
                writable.force(false);
            }
        }
    }

    private static void copy3(final Path source, final Path target)
            throws IOException {
        try (FileChannel readable = open(source, READ)) {
            try (FileChannel writable
                    = open(target, CREATE, WRITE, TRUNCATE_EXISTING)) {
                for (long count = readable.size(); count > 0L;) {
                    final long transferred = writable.transferFrom(
                            readable, writable.position(), count);
                    writable.position(writable.position() + transferred);
                    count -= transferred;
                }
                writable.force(false);
            }
        }
    }

    static void copy(final Path source, final Path target) throws IOException {
        switch (current().nextInt(3)) {
            case 0:
                copy1(source, target);
                break;
            case 1:
                copy2(source, target);
                break;
            default:
                copy3(source, target);
                break;
        }
    }

    private IoUtils() {
        super();
    }

}