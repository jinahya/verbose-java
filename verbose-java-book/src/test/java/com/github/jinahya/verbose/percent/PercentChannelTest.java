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
package com.github.jinahya.verbose.percent;

import static com.google.common.io.ByteStreams.copy;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import static java.lang.reflect.Proxy.newProxyInstance;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.size;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.DSYNC;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.ServiceLoader.load;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.io.FileUtils.contentEquals;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

public class PercentChannelTest {

    private static <T extends WritableByteChannel> T nonBlocking(
            final Class<T> type, final T channel) {
        final Method WRITE_METHOD;
        try {
            WRITE_METHOD = WritableByteChannel.class.getMethod(
                    "write", ByteBuffer.class);
        } catch (final NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
        final InvocationHandler handler = (p, m, a) -> {
            if (WRITE_METHOD.equals(m)) {
                final ByteBuffer src = (ByteBuffer) a[0];
                final int limit = src.limit();
                if (src.hasRemaining()) {
                    src.limit(limit - current().nextInt(src.remaining()));
                }
                final int written = channel.write(src);
                src.limit(limit);
                return written;
            }
            return m.invoke(channel, a);
        };
        final Object proxy = newProxyInstance(
                type.getClassLoader(), new Class<?>[]{type}, handler);
        return type.cast(proxy);
    }

    private static <T extends ReadableByteChannel> T nonBlocking(
            final Class<T> type, final T channel) {
        final Method READ_METHOD;
        try {
            READ_METHOD = ReadableByteChannel.class.getMethod(
                    "read", ByteBuffer.class);
        } catch (final NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
        final InvocationHandler handler = (p, m, a) -> {
            if (READ_METHOD.equals(m)) {
                final ByteBuffer dst = (ByteBuffer) a[0];
                final int limit = dst.limit();
                if (dst.hasRemaining()) {
                    dst.limit(limit - current().nextInt(dst.remaining()));
                }
                final int read = channel.read(dst);
                dst.limit(limit);
                return read;
            }
            return m.invoke(channel, a);
        };
        final Object proxy = newProxyInstance(
                type.getClassLoader(), new Class<?>[]{type}, handler);
        return type.cast(proxy);
    }

    @Test(invocationCount = 1)
    public void encodedDecodePath() throws IOException {
        final Path created = createTempFile(null, null);
        created.toFile().deleteOnExit();
        try (WritableByteChannel opened = open(created, WRITE, DSYNC)) {
            final ByteBuffer b = allocate(current().nextInt(1024));
            current().nextBytes(b.array());
            while (b.hasRemaining()) {
                opened.write(b);
            }
        }
        final Path encoded = createTempFile(null, null);
        encoded.toFile().deleteOnExit();
        try (ReadableByteChannel readable = open(created, READ);
             WritableByteChannel writable = nonBlocking(
                     WritableByteChannel.class, new WritablePercentChannel(
                             open(encoded, WRITE, DSYNC),
                             load(PercentEncoder.class).iterator().next()))) {
            final long copied = copy(readable, writable);
            assertEquals(copied, size(created));
        }
        final Path decoded = createTempFile(null, null);
        decoded.toFile().deleteOnExit();
        try (ReadableByteChannel readable = nonBlocking(
                ReadableByteChannel.class, new ReadablePercentChannel(
                        open(encoded, READ),
                        load(PercentDecoder.class).iterator().next()));
             WritableByteChannel writable = open(decoded, WRITE, DSYNC)) {
            final long copied = copy(readable, writable);
            assertEquals(copied, size(created));
        }
        contentEquals(decoded.toFile(), created.toFile());
    }
}
