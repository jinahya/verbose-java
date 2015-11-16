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
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class HelloWorldDataTest {

    abstract HelloWorld implementation();

    @Test(dataProvider = "arrayNull",
          dataProviderClass = HelloWorldDataProvider.class,
          expectedExceptions = {NullPointerException.class})
    public void setArrayNull(final byte[] array, final int offset) {
        assertFalse(array != null);
        assertTrue(offset >= 0);
        implementation().set(array, offset);
    }

    @Test(dataProvider = "offsetNegative",
          dataProviderClass = HelloWorldDataProvider.class,
          expectedExceptions = {ArrayIndexOutOfBoundsException.class})
    public void setOffsetNegative(final byte[] array, final int offset) {
        assertTrue(array != null);
        assertFalse(offset >= 0);
        assertTrue(offset + HelloWorld.BYTES <= array.length);
        implementation().set(array, offset);
    }

    @Test(dataProvider = "capacityNotEnough",
          dataProviderClass = HelloWorldDataProvider.class,
          expectedExceptions = {ArrayIndexOutOfBoundsException.class})
    public void setCapacityNotEnough(final byte[] array, final int offset) {
        assertTrue(array != null);
        assertTrue(offset >= 0);
        assertFalse(offset + HelloWorld.BYTES <= array.length);
        implementation().set(array, offset);
    }
}
