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

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * A class for testing {@link HelloWorld#set(byte[], int)} with data provided by
 * {@link HelloWorldDataProvider}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
abstract class HelloWorldDataTest {

    /**
     * Returns an instance of {@link HelloWorld} to test.
     *
     * @return an instance of {@link HelloWorld}.
     */
    abstract HelloWorld implementation();

    /**
     * Expects a {@code NullPointerException} while invoking
     * {@link HelloWorld#set(byte[], int)} on the instance
     * {@link #implementation()} returns with an array and an offset provided by
     * {@link HelloWorldDataProvider#provideArrayNull()}.
     *
     * @param array the array
     * @param offset the offset
     *
     * @see #implementation()
     * @see HelloWorld#set(byte[], int)
     */
    @Test(dataProvider = "provideArrayNull",
          dataProviderClass = HelloWorldDataProvider.class,
          expectedExceptions = NullPointerException.class)
    public void testArrayNull(final byte[] array, final int offset) {
        implementation().set(array, offset);
    }

    /**
     * Expects an {@code IndexOutOfBoundsException} while invoking
     * {@link HelloWorld#set(byte[], int)} on the instance
     * {@link #implementation()} returns with an array and an offset provided by
     * {@link HelloWorldDataProvider#provideOffsetNegative()}.
     *
     * @param array the array which should not be {@code null}
     * @param offset the offset which should be negative
     *
     * @see #implementation()
     */
    @Test(dataProvider = "provideOffsetNegative",
          dataProviderClass = HelloWorldDataProvider.class,
          expectedExceptions = IndexOutOfBoundsException.class)
    public void testOffsetNegative(final byte[] array, final int offset) {
        implementation().set(array, offset);
    }

    /**
     * Expects an {@code IndexOutOfBoundsException} while invoking
     * {@link HelloWorld#set(byte[], int)} on the instance
     * {@link #implementation()} returns with an array and an offset provided by
     * {@link HelloWorldDataProvider#provideCapacityNotEnough()}.
     *
     * @param array the array which should not be {@code null}
     * @param offset the offset which should be too big so that
     * {@code offset + HelloWorld.BYTES} is greater than {@code array.length}
     *
     * @see #implementation()
     */
    @Test(dataProvider = "provideCapacityNotEnough",
          dataProviderClass = HelloWorldDataProvider.class,
          expectedExceptions = IndexOutOfBoundsException.class)
    public void testCapacityNotEnough(final byte[] array, final int offset) {
        implementation().set(array, offset);
    }

//    /**
//     * Expects an {@code IndexOutOfBoundsException} while invoking
//     * {@link HelloWorld#set(byte[], int)} on the instance
//     * {@link #implementation()} returns with an array and an offset provided by
//     * {@link HelloWorldDataProvider#provideValid()}.
//     *
//     * @param array the array which should not be {@code null}
//     * @param offset the offset which should be equals or greater than zero and
//     * should be equals or less than {@code array.length - HelloWorld.BYTES}
//     *
//     * @see #implementation()
//     */
//    @Test(dataProvider = "provideValid",
//          dataProviderClass = HelloWorldDataProvider.class)
//    public void testValid(final byte[] array, final int offset) {
//        implementation().set(array, offset);
//        assertEquals(array[offset + 0x0], 'h'); // NOSONAR <1>
//        assertEquals(array[offset + 0x1], 'e');
//        assertEquals(array[offset + 0x2], 'l');
//        assertEquals(array[offset + 0x3], 'l');
//        assertEquals(array[offset + 0x4], 'o');
//        assertEquals(array[offset + 0x5], ',');
//        assertEquals(array[offset + 0x6], ' ');
//        assertEquals(array[offset + 0x7], 'w');
//        assertEquals(array[offset + 0x8], 'o');
//        assertEquals(array[offset + 0x9], 'r');
//        assertEquals(array[offset + 0xA], 'l');
//        assertEquals(array[offset + 0xB], 'd');
//    }
}
