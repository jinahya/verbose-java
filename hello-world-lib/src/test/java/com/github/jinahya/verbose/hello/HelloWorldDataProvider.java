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
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import org.testng.annotations.DataProvider;

/**
 * A class providing data for testing {@link HelloWorld#set(byte[], int)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class HelloWorldDataProvider {

    /**
     * Provides data set of {@code null} arrays.
     *
     * @return a data set
     */
    @DataProvider
    static Object[][] provideArrayNull() {
        return new Object[][]{
            new Object[]{null, 0},
            new Object[]{null, 1}
        };
    }

    /**
     * Provides data set of negative {@code offset}s.
     *
     * @return a data set
     */
    @DataProvider
    static Object[][] provideOffsetNegative() {
        return new Object[][]{
            new Object[]{new byte[HelloWorld.BYTES - 0], -1},
            new Object[]{new byte[HelloWorld.BYTES - 1], -2},
            new Object[]{new byte[HelloWorld.BYTES - 2], -3}
        };
    }

    /**
     * Provides data set of insufficient array capacities.
     *
     * @return a data set
     */
    @DataProvider
    static Iterator<Object[]> provideCapacityNotEnough() {
        return Arrays.asList(
                new Object[]{new byte[HelloWorld.BYTES + 0], 1}, // <1>
                new Object[]{new byte[HelloWorld.BYTES + 1], 2},
                new Object[]{new byte[HelloWorld.BYTES + 2], 3},
                new Object[]{new byte[HelloWorld.BYTES + 3], 4}
        ).iterator();
    }

//    /**
//     * Provides valid test data for {@link HelloWorld#set(byte[], int) }.
//     *
//     * @return an iterator of test data
//     */
//    @DataProvider
//    static Iterator<Object[]> provideValid() {
//        return IntStream
//                .range(0, 16) // <1>
//                .mapToObj(i -> new Object[]{new byte[HelloWorld.BYTES + i], i}) // <2>
//                .collect(toList()) // <3>
//                .iterator(); // <4>
//    }
}
