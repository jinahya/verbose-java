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

import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocateDirect;
import java.nio.channels.FileChannel;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import org.openjdk.jmh.annotations.Benchmark;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class WritableHexChannelBenchmark {

    private static final ByteBuffer buffer = allocateDirect(128);

    private void copy(final ReadableByteChannel readable,
                      final WritableByteChannel writable)
            throws IOException {
        while (readable.read(buffer) != -1) {
            buffer.flip();
            writable.write(buffer);
            buffer.compact();
        }
        for (buffer.flip(); buffer.hasRemaining();) {
            writable.write(buffer);
        }
        buffer.clear();
    }

    private static final int count = 1024;

    @Benchmark
    public void mark1(final Paths paths) throws IOException {
        try (FileChannel readable = open(paths.created, READ)) {
            final FileChannel channel = open(paths.encoded, WRITE);
            final HexEncoder encoder = new HexEncoderImpl();
            try (WritableByteChannel writable
                    = new WritableHexChannel_O(channel, encoder)) {
                for (int i = 0; i < count; i++) {
                    readable.position(0L);
                    channel.position(0L);
                    copy(readable, writable);
                    channel.force(false);
                }
            }
        }
    }

    @Benchmark
    public void buffer0x40(final Paths paths) throws IOException {
        try (FileChannel readable = open(paths.created, READ)) {
            final FileChannel channel = open(paths.encoded, WRITE);
            final HexEncoder encoder = new HexEncoderImpl();
            try (WritableByteChannel writable
                    = new WritableHexChannel(channel, encoder, 0x40, false)) {
                for (int i = 0; i < count; i++) {
                    readable.position(0L);
                    channel.position(0L);
                    copy(readable, writable);
                    channel.force(false);
                }
            }
        }
    }

    @Benchmark
    public void buffer0x80(final Paths paths) throws IOException {
        try (FileChannel readable = open(paths.created, READ)) {
            final FileChannel channel = open(paths.encoded, WRITE);
            final HexEncoder encoder = new HexEncoderImpl();
            try (WritableByteChannel writable
                    = new WritableHexChannel(channel, encoder, 0x80, false)) {
                for (int i = 0; i < count; i++) {
                    readable.position(0L);
                    channel.position(0L);
                    copy(readable, writable);
                    channel.force(false);
                }
            }
        }
    }

    @Benchmark
    public void buffer0xC0(final Paths paths) throws IOException {
        try (FileChannel readable = open(paths.created, READ)) {
            final FileChannel channel = open(paths.encoded, WRITE);
            final HexEncoder encoder = new HexEncoderImpl();
            try (WritableByteChannel writable
                    = new WritableHexChannel(channel, encoder, 0xC0, false)) {
                for (int i = 0; i < count; i++) {
                    readable.position(0L);
                    channel.position(0L);
                    copy(readable, writable);
                    channel.force(false);
                }
            }
        }
    }

    //@Benchmark
    public void buffer0x40d(final Paths paths) throws IOException {
        try (FileChannel readable = open(paths.created, READ)) {
            final FileChannel channel = open(paths.encoded, WRITE);
            final HexEncoder encoder = new HexEncoderImpl();
            try (WritableByteChannel writable
                    = new WritableHexChannel(channel, encoder, 0x40, true)) {
                for (int i = 0; i < count; i++) {
                    readable.position(0L);
                    channel.position(0L);
                    copy(readable, writable);
                    channel.force(false);
                }
            }
        }
    }

    //@Benchmark
    public void buffer0x80d(final Paths paths) throws IOException {
        try (FileChannel readable = open(paths.created, READ)) {
            final FileChannel channel = open(paths.encoded, WRITE);
            final HexEncoder encoder = new HexEncoderImpl();
            try (WritableByteChannel writable
                    = new WritableHexChannel(channel, encoder, 0x80, true)) {
                for (int i = 0; i < count; i++) {
                    readable.position(0L);
                    channel.position(0L);
                    copy(readable, writable);
                    channel.force(false);
                }
            }
        }
    }

    //@Benchmark
    public void buffer0xC0d(final Paths paths) throws IOException {
        try (FileChannel readable = open(paths.created, READ)) {
            final FileChannel channel = open(paths.encoded, WRITE);
            final HexEncoder encoder = new HexEncoderImpl();
            try (WritableByteChannel writable
                    = new WritableHexChannel(channel, encoder, 0xC0, true)) {
                for (int i = 0; i < count; i++) {
                    readable.position(0L);
                    channel.position(0L);
                    copy(readable, writable);
                    channel.force(false);
                }
            }
        }
    }

}
