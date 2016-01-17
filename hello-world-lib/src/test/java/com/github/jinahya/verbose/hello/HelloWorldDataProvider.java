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

import java.util.Arrays;
import java.util.Iterator;
import org.testng.annotations.DataProvider;

/**
 * A class providing data for {@link HelloWorld#set(byte[], int)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class HelloWorldDataProvider {

    @DataProvider
    static Object[][] provideArrayNull() {
        return new Object[][]{
            new Object[]{null, 0},
            new Object[]{null, 1}
        };
    }

    @DataProvider
    static Object[][] provideOffsetNegative() {
        return new Object[][]{
            new Object[]{new byte[HelloWorld.BYTES], -1},
            new Object[]{new byte[HelloWorld.BYTES], -2},
            new Object[]{new byte[HelloWorld.BYTES], -3}
        };
    }

    @DataProvider
    static Iterator<Object[]> provideCapacityNotEnough() {
        return Arrays.asList(
                new Object[]{new byte[HelloWorld.BYTES + 0], 1},
                new Object[]{new byte[HelloWorld.BYTES + 1], 2},
                new Object[]{new byte[HelloWorld.BYTES + 2], 3},
                new Object[]{new byte[HelloWorld.BYTES + 3], 4}
        ).iterator();
    }
}
