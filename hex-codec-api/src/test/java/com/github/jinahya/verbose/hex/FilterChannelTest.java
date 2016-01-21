/*
 * Copyright 2016 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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
package com.github.jinahya.verbose.hex;

import java.io.IOException;
import java.nio.channels.Channel;
import org.testng.annotations.Test;

/**
 * A class for testing {@code FilterChannel}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class FilterChannelTest {

    /**
     * Asserts {@link HexChannel#isOpen()} throws a
     * {@code NullPointerException} when the {@link HexChannel#channel} is
     * {@code null}.
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void assertIsOpenThrowsNullPointerExceptionWhenChannelIsNull() {
        new HexChannel<Channel, Object>(null, null, 0, true) {
        }.isOpen();
    }

    /**
     * Asserts {@link HexChannel#close()} silently returns even if
     * {@link HexChannel#channel} is {@code null}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void assertCloseSilentlyReturnsEvenIfChannelIsNull()
            throws IOException {
        new HexChannel<Channel, Object>(null, null, 0, true) {
        }.close();
    }
}
