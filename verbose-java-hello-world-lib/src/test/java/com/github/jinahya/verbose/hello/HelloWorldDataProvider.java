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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.testng.annotations.DataProvider;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class HelloWorldDataProvider {

    @DataProvider
    static Object[][] arrayNull() {
        final Object[][] data = new Object[current().nextInt(1, 128)][];
        for (int i = 0; i < data.length; i++) {
            final byte[] array = null;
            final int offset = current().nextInt(Integer.MAX_VALUE);
            data[i] = new Object[]{array, offset};
        }
        return data;
    }

    @DataProvider
    static Object[][] offsetNegative() {
        final Object[][] data = new Object[current().nextInt(1, 128)][];
        for (int i = 0; i < data.length; i++) {
            final byte[] array = new byte[current().nextInt(
                    HelloWorld.BYTES - 1, HelloWorld.BYTES * 2)];
            final int offset = Integer.MIN_VALUE | current().nextInt();
            data[i] = new Object[]{array, offset};
        }
        return data;
    }

    @DataProvider
    static Iterator<Object[]> spaceNotEnough() {
        final int size = current().nextInt(1, 128);
        final List<Object[]> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            final byte[] array
                    = new byte[current().nextInt(HelloWorld.BYTES * 2)];
            final int offset = Math.max(
                    0, current().nextInt(array.length - HelloWorld.BYTES + 1,
                            array.length * 2));
            data.add(new Object[]{array, offset});
        }
        return data.iterator();
    }
}
