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

import com.google.inject.Inject;
import java.util.function.Consumer;
import java.util.function.Function;
import org.testng.annotations.Guice;

/**
 * An abstract class for testing {@link HexDecoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = HexDecoderDemoModule.class)
class AbstractHexDecoderTest {

    /**
     * Accepts given consumer with an instance of {@link HexDecoder}.
     *
     * @param consumer the consumer
     */
    protected void accept(final Consumer<HexDecoder> consumer) {
        consumer.accept(decoder);
    }

    /**
     * Apply given function with an {@link HexDecoder} and returns the result.
     *
     * @param <R> result type parameter
     * @param function the function
     * @return the result the function returns
     */
    protected <R> R apply(final Function<HexDecoder, R> function) {
        return function.apply(decoder);
    }

    @Inject
    private HexDecoder decoder;
}
