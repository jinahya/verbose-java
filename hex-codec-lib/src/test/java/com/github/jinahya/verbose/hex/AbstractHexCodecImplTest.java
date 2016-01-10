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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class AbstractHexCodecImplTest {

    @BeforeClass
    protected void inejct() {
        decoderDelegate.inejct();
        encoderDelegate.inejct();
    }

    protected HexDecoder getDecoder() {
        return decoderDelegate.getDecoder();
    }

    protected void acceptDecoder(final Consumer<HexDecoder> consumer) {
        decoderDelegate.acceptDecoder(consumer);
    }

    protected <T> T applyDecoder(final Function<HexDecoder, T> function) {
        return decoderDelegate.applyDecoder(function);
    }

    protected HexEncoder getEncoder() {
        return encoderDelegate.getEncoder();
    }

    protected void acceptEncoder(final Consumer<HexEncoder> consumer) {
        encoderDelegate.acceptEncoder(consumer);
    }

    protected <T> T applyEncoder(final Function<HexEncoder, T> function) {
        return encoderDelegate.applyDecoder(function);
    }

    protected void acceptCodec(
            final BiConsumer<HexEncoder, HexDecoder> consumer) {
        consumer.accept(encoderDelegate.getEncoder(),
                        decoderDelegate.getDecoder());
    }

    protected <T> T applyEncoder(
            final BiFunction<HexEncoder, HexDecoder, T> function) {
        return function.apply(encoderDelegate.getEncoder(),
                              decoderDelegate.getDecoder());
    }

    private final AbstractHexDecoderTest decoderDelegate = new AbstractHexDecoderTest() {
    };

    private final AbstractHexEncoderTest encoderDelegate = new AbstractHexEncoderTest() {
    };
}
