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

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldImpl implements HelloWorld {

    @Override
    public void set(final byte[] array, final int offset) {
        if (array == null) {
            throw new NullPointerException("null array");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset(" + offset + ") < 0");
        }
        if (offset + HelloWorld.BYTES > array.length) {
            throw new ArrayIndexOutOfBoundsException(
                    "offset(" + offset + ") + " + HelloWorld.BYTES
                    + " > array.length(" + array.length + ")");
        }
        array[0x0] = 0x68; // 'h'
        array[0x1] = 0x65; // 'e'
        array[0x2] = 0x6C; // 'l'
        array[0x3] = 0x6C; // 'l'
        array[0x4] = 0x6F; // 'o'
        array[0x5] = 0x2C; // ','
        array[0x6] = 0x20; // ' '
        array[0x7] = 0x77; // 'w'
        array[0x8] = 0x6F; // 'o'
        array[0x9] = 0x72; // 'r'
        array[0xA] = 0x6C; // 'l';
        array[0xB] = 0x64; // 'd';
    }
}
