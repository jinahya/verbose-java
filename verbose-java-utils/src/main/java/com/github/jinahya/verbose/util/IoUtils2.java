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

import java.io.IOException;
import static java.lang.invoke.MethodHandles.lookup;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.DSYNC;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 * I/O utilities for {@code java.nio.channels} package.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class IoUtils2 {

    private static final Logger logger
            = getLogger(lookup().lookupClass().getName());

    private static long copy1(final ReadableByteChannel readable,
                              final WritableByteChannel writable,
                              final ByteBuffer buffer)
            throws IOException {
        if (readable == null) {
            throw new NullPointerException("readable is null");
        }
        if (!readable.isOpen()) {
            throw new IllegalArgumentException("readable is not open");
        }
        if (writable == null) {
            throw new NullPointerException("writable is null");
        }
        if (!writable.isOpen()) {
            throw new IllegalArgumentException("writable is not open");
        }
        if (buffer == null) {
            throw new NullPointerException("buffer is null");
        }
        if (buffer.capacity() == 0) {
            throw new IllegalArgumentException("buffer.capacity == 0");
        }
        long count = 0L;
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
                              final WritableByteChannel writable,
                              final ByteBuffer buffer)
            throws IOException {
        if (readable == null) {
            throw new NullPointerException("readable is null");
        }
        if (!readable.isOpen()) {
            throw new IllegalArgumentException("readable is not open");
        }
        if (writable == null) {
            throw new NullPointerException("writable is null");
        }
        if (!writable.isOpen()) {
            throw new IllegalArgumentException("writable is not open");
        }
        if (buffer == null) {
            throw new NullPointerException("buffer is null");
        }
        if (buffer.capacity() == 0) {
            throw new IllegalArgumentException("buffer.capacity == 0");
        }
        long count = 0L;
        while (readable.read(buffer) != -1) {
            buffer.flip(); // limit->position, position->zero
            while (buffer.hasRemaining()) { // <1>
                count += writable.write(buffer);
            }
            buffer.clear(); // position->zero, limit->capacity
        }
        return count;
    }

    static long copy(final ReadableByteChannel readable,
                     final WritableByteChannel writable,
                     final ByteBuffer buffer)
            throws IOException {
        if (readable == null) {
            throw new NullPointerException("readable is null");
        }
        if (!readable.isOpen()) {
            throw new IllegalArgumentException("readable is not open");
        }
        if (writable == null) {
            throw new NullPointerException("writable is null");
        }
        if (!writable.isOpen()) {
            throw new IllegalArgumentException("writable is not open");
        }
        if (buffer == null) {
            throw new NullPointerException("buffer is null");
        }
        if (buffer.capacity() == 0) {
            throw new IllegalArgumentException("buffer.capacity == 0");
        }
        switch (current().nextInt(2)) {
            case 0:
                return copy1(readable, writable, buffer);
            default:
                return copy2(readable, writable, buffer);
        }
    }

    private static void copy1(final Path source, final Path target,
                              final ByteBuffer buffer)
            throws IOException {
        // @todo: validate arguments!
        try (FileChannel readable = open(source, READ);
             FileChannel writable = open(
                     target, CREATE, TRUNCATE_EXISTING, WRITE)) {
            copy(readable, writable, buffer);
            writable.force(false); // <1>
        }
    }

    private static void copy2(final Path source, final Path target,
                              final ByteBuffer buffer)
            throws IOException {
        // @todo: validate arguments!
        try (ReadableByteChannel readable = open(source, READ);
             WritableByteChannel writable = open(
                     target, CREATE, TRUNCATE_EXISTING, WRITE, DSYNC)) {
            copy(readable, writable, buffer);
        }
    }

    private static void copy3(final Path source, final Path target)
            throws IOException {
        // @todo: validate arguments!
        try (FileChannel readable = open(source, READ);
             FileChannel writable = open(
                     target, CREATE, TRUNCATE_EXISTING, WRITE)) {
            final long transferred = readable.transferTo(
                    readable.position(), readable.size(), writable);
            writable.force(false);
        }
    }

    private static void copy4(final Path source, final Path target)
            throws IOException {
        // @todo: validate arguments!
        try (FileChannel readable = open(source, READ);
             FileChannel writable = open(
                     target, CREATE, TRUNCATE_EXISTING, WRITE)) {
            final long transferred = writable.transferFrom(
                    readable, writable.position(), readable.size());
            writable.force(false);
        }
    }

    static void copy(final Path source, final Path target,
                     final ByteBuffer buffer)
            throws IOException {
        // @todo: validate arguments!
        switch (current().nextInt(4)) {
            case 0:
                copy1(source, target, buffer);
                break;
            case 1:
                copy2(source, target, buffer);
                break;
            case 3:
                copy3(source, target);
                break;
            default:
                copy4(source, target);
                break;
        }
    }

    private IoUtils2() {
        super();
    }
}
