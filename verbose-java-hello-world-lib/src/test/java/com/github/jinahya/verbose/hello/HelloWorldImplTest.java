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
import java.util.Arrays;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

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

    @Test(expectedExceptions = ArrayIndexOutOfBoundsException.class)
    public void expectIllegalArgumentExceptionWhenOffsetIsNegative() {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = -1;
        new HelloWorldImpl().set(array, offset);
    }

    @Test(expectedExceptions = ArrayIndexOutOfBoundsException.class)
    public void expectIllegalArgumentExceptionWhenCapacityIsNotEnough() {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = 1;
        new HelloWorldImpl().set(array, offset);
    }

    @Test
    public void set() {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = 0;
        new HelloWorldImpl().set(array, offset);
        assertTrue(Arrays.equals(array, "hello, world".getBytes(US_ASCII)));
    }

    @Test
    public void setWithArrayBuffer() {
        final byte[] actual = new byte[HelloWorld.BYTES];
        new HelloWorldImpl().put(ByteBuffer.wrap(actual));
        final byte[] expected = "hello, world".getBytes(US_ASCII);
        assertEquals(actual, expected);
    }

    @Test
    public void setWithBuffer() {
        final ByteBuffer actual = ByteBuffer.allocate(HelloWorld.BYTES);
        new HelloWorldImpl().put(actual);
        final ByteBuffer expected = ByteBuffer.allocate(HelloWorld.BYTES);
        expected.put("hello, world".getBytes(US_ASCII));
        assertTrue(actual.flip().equals(expected.flip()));
    }

    private transient final Logger logger = getLogger(getClass());
}
