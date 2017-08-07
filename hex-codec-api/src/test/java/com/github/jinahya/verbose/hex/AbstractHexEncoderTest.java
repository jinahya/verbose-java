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
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.testng.annotations.Guice;

/**
 * An abstract class for testing {@link HexEncoder}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Guice(modules = HexEncoderMockModule.class)
abstract class AbstractHexEncoderTest {

    /**
     * Applies given function with an instance of {@link HexEncoder} and returns
     * the result.
     *
     * @param <R> result type parameter
     * @param function the function to apply
     * @return the result of the function applied
     */
    protected <R> R apply(final Function<HexEncoder, R> function) {
        return function.apply(encoder);
    }

    /**
     * Applies given function with an instance of {@link HexEncoder} and
     * specified {@code u} and returns the result.
     *
     * @param <R> result type parameter
     * @param <U> second argument parameter
     * @param function the function to be applied
     * @param u the second argument
     * @return the result of the function
     */
    protected <R, U> R apply(final BiFunction<HexEncoder, U, R> function,
                             final U u) {
        return apply(e -> function.apply(e, u));
    }

    /**
     * Accepts given consumer with an instance of {@link HexEncoder}.
     *
     * @param consumer the consumer
     */
    protected void accept(final Consumer<HexEncoder> consumer) {
        apply(e -> {
            consumer.accept(e);
            return null;
        });
    }

    /**
     * Accepts given consumer with an instance of {@link HexEncoder} and
     * specified second argument.
     *
     * @param <U> second argument parameter type
     * @param consumer the consumer to be accepted
     * @param u the second argument
     */
    protected <U> void accept(final BiConsumer<HexEncoder, U> consumer,
                              final U u) {
        accept(e -> consumer.accept(e, u));
    }

    @Inject
    private HexEncoder encoder;
}
