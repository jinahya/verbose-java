/*
 * Copyright 2017 Jin Kwon &lt;onacit at gmail.com&gt;.
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
package com.github.jinahya.verbose.percent;

import com.google.inject.Inject;
import java.nio.channels.ReadableByteChannel;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * Test class for {@link WritablePercentChannel}.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Guice(modules = {PercentDecoderMockModule.class})
public class ReadablePercentChannelTest {

    @Test
    public void assertANewlyCreatedInstanceIsOpen() {
        assertTrue(
                new ReadablePercentChannel<>(
                        () -> (ReadableByteChannel) null,
                        () -> (PercentDecoder) null
                ).isOpen()
        );
        assertTrue(
                new ReadablePercentChannel<>(
                        () -> mock(ReadableByteChannel.class),
                        () -> mock(PercentDecoder.class)
                ).isOpen()
        );
    }

    @Inject
    private PercentDecoder decoder;
}
