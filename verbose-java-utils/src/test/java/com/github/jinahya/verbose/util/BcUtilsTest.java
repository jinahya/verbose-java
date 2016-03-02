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

import static com.github.jinahya.verbose.util.BcUtils.nonBlocking;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import org.testng.annotations.Test;

/**
 * Tests {@link BcUtils}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BcUtilsTest {

    @Test
    public static void nonBlockingWritableByteChannel() throws IOException {
        final WritableByteChannel blocking = mock(WritableByteChannel.class);
        doAnswer(i -> {
            final ByteBuffer src = i.getArgumentAt(0, ByteBuffer.class);
            src.position(src.limit());
            return null;
        }).when(blocking).write(any(ByteBuffer.class));
        final WritableByteChannel nonblocking
                = nonBlocking(WritableByteChannel.class, blocking);
        final int written = nonblocking.write(allocate(current().nextInt(128)));
    }

    @Test
    public static void nonBlockingReadableByteChannel() throws IOException {
        final ReadableByteChannel blocking = mock(ReadableByteChannel.class);
        doAnswer(i -> {
            final ByteBuffer dst = i.getArgumentAt(0, ByteBuffer.class);
            dst.position(dst.limit());
            return null;
        }).when(blocking).read(any(ByteBuffer.class));
        final ReadableByteChannel nonblocking
                = nonBlocking(ReadableByteChannel.class, blocking);
        final int read = nonblocking.read(allocate(current().nextInt(128)));
    }

    @Test
    public void construct() throws ReflectiveOperationException {
        final Constructor<BcUtils> constructor
                = BcUtils.class.getDeclaredConstructor();
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        final BcUtils instance = constructor.newInstance();
    }
}
