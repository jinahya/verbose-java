/*
 * Copyright 2015 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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
package com.github.jinahya.verbose.hello;

import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.US_ASCII;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import org.testng.annotations.Test;

/**
 * A test class testing {@link HelloWorldImpl}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldImplTest {

    @Test(expectedExceptions = NullPointerException.class)
    public void expectNullPointerExceptionWhenArrayIsNull() {
        final byte[] array = null;
        final int offset = 0;
        new HelloWorldImpl().set(array, offset);
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void expectIndexOutOfBoundsWhenOffsetIsNegative() {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = -1;
        new HelloWorldImpl().set(array, offset);
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void expectIndexOutOfBoundsExceptionWhenCapacityIsNotEnough() {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = 1;
        new HelloWorldImpl().set(array, offset);
    }

    @Test
    public void expectHelloWorldBytesSetOnArrayStartingAtOffset() {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = 0;
        new HelloWorldImpl().set(array, offset);
        assertEquals(array[offset], 'h');
        assertEquals(array[offset + 0x1], 'e');
        assertEquals(array[offset + 0x2], 'l');
        assertEquals(array[offset + 0x3], 'l');
        assertEquals(array[offset + 0x4], 'o');
        assertEquals(array[offset + 0x5], ',');
        assertEquals(array[offset + 0x6], ' ');
        assertEquals(array[offset + 0x7], 'w');
        assertEquals(array[offset + 0x8], 'o');
        assertEquals(array[offset + 0x9], 'r');
        assertEquals(array[offset + 0xA], 'l');
        assertEquals(array[offset + 0xB], 'd');
    }

    @Test
    public void put() {
        assertThrows(NullPointerException.class,
                     () -> new HelloWorldImpl().put(null));
        final ByteBuffer actual = ByteBuffer.allocate(HelloWorld.BYTES);
        new HelloWorldImpl().put(actual);
        actual.flip();
        final ByteBuffer expected
                = ByteBuffer.wrap("hello, world".getBytes(US_ASCII));
        assertEquals(actual, expected);
    }

    @Test
    public void putWithArrayWrappingBuffer() {
        final byte[] actual = new byte[HelloWorld.BYTES];
        final ByteBuffer buffer = ByteBuffer.wrap(actual);
        new HelloWorldImpl().put(buffer);
        final byte[] expected = "hello, world".getBytes(US_ASCII);
        assertEquals(actual, expected);
    }

    private transient final Logger logger = getLogger(getClass());
}
