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
package com.github.jinahya.verbose.io;

import static com.github.jinahya.verbose.io.IoUtils.copy;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.Channels.newChannel;
import java.nio.channels.FileChannel;
import static java.nio.channels.FileChannel.open;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.size;
import java.nio.file.Path;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * A class testing {@link IoUtils}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class IoUtilsTest {

    @Test(invocationCount = 128)
    public void copyStreams() throws IOException {
        final byte[] bytes = new byte[current().nextInt(1024)];
        current().nextBytes(bytes);
        try (InputStream input = new ByteArrayInputStream(bytes);
             OutputStream output = new ByteArrayOutputStream(bytes.length)) {
            copy(input, output);
            output.flush();
        }
    }

    @Test(invocationCount = 128)
    public void copyFiles() throws IOException {
        final File source = File.createTempFile("tmp", null);
        try (OutputStream o = new FileOutputStream(source)) {
            final byte[] bytes = new byte[current().nextInt(1024)];
            current().nextBytes(bytes);
            o.write(bytes);
            o.flush();
        }
        final File target = File.createTempFile("tmp", null);
        try (OutputStream o = new FileOutputStream(target)) {
            final byte[] bytes = new byte[current().nextInt(1024)];
            current().nextBytes(bytes);
            o.write(bytes);
            o.flush();
        }
        copy(source, target);
        assertEquals(target.length(), source.length());
        try (InputStream ti = new FileInputStream(target);
             InputStream si = new FileInputStream(source)) {
            assertEquals(ti.read(), si.read());
        }
        source.delete();
        target.delete();
    }

    @Test(invocationCount = 128)
    public void copyChannels() throws IOException {
        final byte[] bytes = new byte[current().nextInt(1024)];
        current().nextBytes(bytes);
        try (ReadableByteChannel readable
                = newChannel(new ByteArrayInputStream(bytes));
             WritableByteChannel writable
             = newChannel(new ByteArrayOutputStream(bytes.length))) {
            copy(readable, writable);
        }
    }

    @Test(invocationCount = 128)
    public void copyPaths() throws IOException {
        final Path source = Files.createTempFile(null, null);
        try (FileChannel c = open(source, WRITE)) {
            final ByteBuffer b = allocate(1024);
            current().nextBytes(b.array());
            while (b.hasRemaining()) {
                c.write(b);
            }
            c.force(false);
        }
        final Path target = Files.createTempFile(null, null);
        try (FileChannel c = open(target, WRITE)) {
            final ByteBuffer b = allocate(1024);
            current().nextBytes(b.array());
            while (b.hasRemaining()) {
                c.write(b);
            }
            c.force(false);
        }
        copy(source, target);
        assertEquals(size(target), size(target));
        try (FileChannel tc = open(target, READ);
             FileChannel sc = open(source, READ)) {
            final ByteBuffer tb = allocate(1);
            final ByteBuffer sb = allocate(1);
            assertEquals(tc.read(tb), 1);
            assertEquals(sc.read(sb), 1);
            assertEquals(tb, sb);
            tb.clear();
            sb.clear();
        }
        delete(source);
        delete(target);
    }
}
