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

import org.testng.annotations.Test;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * A test class testing {@link HelloWorldImpl}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldImplTest {

    @Test(expectedExceptions = {NullPointerException.class})
    public void expectingNullPointerExceptionWhenArrayIsNull() {
        final byte[] array = null;
        final int offset = 0;
        assertFalse(array != null);
        assertTrue(offset >= 0);
        //assertTrue(offset + HelloWorld.BYTES <= array.length);
        new HelloWorldImpl().set(array, offset);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void expectingIllegalArgumentExceptionWhenOffsetIsNegative() {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = -1;
        assertTrue(array != null);
        assertFalse(offset >= 0);
        assertTrue(offset + HelloWorld.BYTES <= array.length);
        new HelloWorldImpl().set(array, offset);
    }

    @Test(expectedExceptions = ArrayIndexOutOfBoundsException.class)
    public void expectIllegalArgumentExceptionWhenCapacityIsNotEnough() {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = 1;
        assertTrue(array != null);
        assertTrue(offset >= 0);
        assertFalse(offset + HelloWorld.BYTES <= array.length);
        new HelloWorldImpl().set(array, offset);
    }
}
