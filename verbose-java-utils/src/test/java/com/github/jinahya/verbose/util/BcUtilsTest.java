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
import static java.nio.ByteBuffer.allocate;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.testng.annotations.Test;
import static com.github.jinahya.verbose.util.BcUtils.nonBlocking;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import static java.lang.invoke.MethodHandles.lookup;
import static java.nio.channels.Channels.newChannel;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Tests {@link BcUtils}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class BcUtilsTest {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @Test
    public static void nonBlockingWritableByteChannel() throws IOException {
        final WritableByteChannel blocking
                = newChannel(new ByteArrayOutputStream());
        final WritableByteChannel nonblocking
                = nonBlocking(WritableByteChannel.class, blocking);
        final int written = nonblocking.write(allocate(current().nextInt(128)));
    }

    @Test
    public static void nonBlockingReadableByteChannel() throws IOException {
        final ReadableByteChannel blocking
                = newChannel(new ByteArrayInputStream(
                        new byte[current().nextInt(1024)]));
        final ReadableByteChannel nonblocking
                = nonBlocking(ReadableByteChannel.class, blocking);
        final int read = nonblocking.read(allocate(current().nextInt(128)));
    }
}
