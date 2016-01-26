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
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.AsynchronousFileChannel;
import static java.nio.channels.AsynchronousFileChannel.open;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import java.util.concurrent.ExecutionException;
import static java.util.concurrent.ThreadLocalRandom.current;
import java.util.concurrent.atomic.LongAdder;

/**
 * Utilities for testing hex codec.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
final class IoUtils3 {

    private static void copy1(final AsynchronousFileChannel rchannel,
                              final LongAdder rposition,
                              final AsynchronousFileChannel wchannel,
                              final LongAdder wposition)
            throws IOException, InterruptedException, ExecutionException {
        final ByteBuffer buffer = allocate(4096);
        for (int read;; rposition.add(read)) {
            read = rchannel.read(buffer, rposition.longValue()).get();
            if (read == -1) {
                break;
            }
            buffer.flip();
            wposition.add(wchannel.write(buffer, wposition.longValue()).get());
            buffer.compact();
        }
        for (buffer.flip(); buffer.hasRemaining();) {
            wposition.add(wchannel.write(buffer, wposition.longValue()).get());
        }
    }

    static void copy(final AsynchronousFileChannel rchannel,
                     final LongAdder rposition,
                     final AsynchronousFileChannel wchannel,
                     final LongAdder wposition)
            throws IOException, InterruptedException, ExecutionException {
        switch (current().nextInt(1)) {
            default:
                copy1(rchannel, rposition, wchannel, wposition);
                break;
        }
    }

    static void copy(final Path source, final Path target)
            throws IOException, InterruptedException, ExecutionException {
        try (AsynchronousFileChannel rchannel = open(source, READ);
             AsynchronousFileChannel wchannel = open(
                     target, WRITE, CREATE, TRUNCATE_EXISTING)) {
            copy(rchannel, new LongAdder(), wchannel, new LongAdder());
        }
    }

    private IoUtils3() {
        super();
    }

}
