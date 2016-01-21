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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 * A class for testing {@link HelloWorldDataProvider}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldDataProviderTest {

    /**
     * Checks {@link HelloWorldDataProvider#provideArrayNull()} method provides
     * appropriate data sets.
     *
     * @param array provided array
     * @param offset provided offset
     */
    @Test(dataProvider = "provideArrayNull",
          dataProviderClass = HelloWorldDataProvider.class)
    public static void checkArrayNull(final byte[] array, final int offset) {
        assertFalse(array != null);
        assertTrue(offset >= 0);
        //assertTrue(offset + HelloWorld.BYTES <= array.length); // NOSONAR
    }

    /**
     * Checks {@link HelloWorldDataProvider#provideOffsetNegative()} method
     * provides appropriate data sets.
     *
     * @param array provided array
     * @param offset provided offset
     */
    @Test(dataProvider = "provideOffsetNegative",
          dataProviderClass = HelloWorldDataProvider.class)
    public static void checkOffsetNegative(final byte[] array,
                                           final int offset) {
        assertTrue(array != null);
        assertFalse(offset >= 0);
        assertTrue(offset + HelloWorld.BYTES <= array.length);
    }

    /**
     * Checks {@link HelloWorldDataProvider#provideCapacityNotEnough()} method
     * provides appropriate data sets.
     *
     * @param array provided array
     * @param offset provided offset
     */
    @Test(dataProvider = "provideCapacityNotEnough",
          dataProviderClass = HelloWorldDataProvider.class)
    public static void checkCapacityNotEnough(final byte[] array,
                                              final int offset) {
        assertTrue(array != null);
        assertTrue(offset >= 0);
        assertFalse(offset + HelloWorld.BYTES <= array.length);
    }
}
