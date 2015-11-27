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

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 * Test class tests {@link HelloWorld#set(byte[], int)} with data provided by
 * {@link HelloWorldDataProvider}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class HelloWorldDataTest {

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
     * {@link HelloWorldDataProvider#arrayNull()}.
     *
     * @param array the array
     * @param offset the offset
     *
     * @see #implementation()
     * @see HelloWorld#set(byte[], int)
     */
    @Test(dataProvider = "arrayNull",
          dataProviderClass = HelloWorldDataProvider.class,
          expectedExceptions = NullPointerException.class)
    public void arrayNull(final byte[] array, final int offset) {
        logger.debug("arrayNull({}, {})", array, offset);
        assertNull(array);
        implementation().set(array, offset);
    }

    /**
     * Expects an {@code ArrayIndexOutOfBoundsException} while invoking
     * {@link HelloWorld#set(byte[], int)} on the instance
     * {@link #implementation()} returns with an array and an offset provided by
     * {@link HelloWorldDataProvider#offsetNegative()}.
     *
     * @param array the array which should not be {@code null}
     * @param offset the offset which should be negative
     *
     * @see #implementation()
     */
    @Test(dataProvider = "offsetNegative",
          dataProviderClass = HelloWorldDataProvider.class,
          expectedExceptions = ArrayIndexOutOfBoundsException.class)
    public void offsetNegative(final byte[] array, final int offset) {
        logger.debug("offsetNegative({}, {})", array, offset);
        assertNotNull(offset);
        assertTrue(offset < 0);
        implementation().set(array, offset);
    }

    /**
     * Expects an {@code ArrayIndexOutOfBoundsException} while invoking
     * {@link HelloWorld#set(byte[], int)} on the instance
     * {@link #implementation()} returns with an array and an offset provided by
     * {@link HelloWorldDataProvider#capacityNotEnough()}.
     *
     * @param array the array which should not be {@code null}
     * @param offset the offset which should be too big so that
     * {@code offset + HelloWorld.BYTES} is greater than {@code array.length}
     *
     * @see #implementation()
     */
    @Test(dataProvider = "capacityNotEnough",
          dataProviderClass = HelloWorldDataProvider.class,
          expectedExceptions = ArrayIndexOutOfBoundsException.class)
    public void capacityNotEnough(final byte[] array, final int offset) {
        logger.debug("capacityNotEnough({}, {})", array, offset);
        assertNotNull(array);
        assertTrue(offset >= 0);
        assertFalse(offset + HelloWorld.BYTES <= array.length);
        implementation().set(array, offset);
    }

    private transient final Logger logger = getLogger(getClass());
}
