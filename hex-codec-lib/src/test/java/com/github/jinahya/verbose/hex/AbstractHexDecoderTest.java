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

import java.util.function.Consumer;
import java.util.function.Function;
import javax.inject.Inject;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class AbstractHexDecoderTest {

    @BeforeClass
    protected void inejct() {
//        decoder = DaggerHexDecoderComponent.create().decoder();
        DaggerHexDecoderComponent.create().injectMembers(this);
        assertNotNull(decoder);
    }

    protected HexDecoder getDecoder() {
        return decoder;
    }

    protected void acceptDecoder(final Consumer<HexDecoder> consumer) {

        consumer.accept(getDecoder());
    }

    protected <T> T applyDecoder(final Function<HexDecoder, T> function) {
        return function.apply(getDecoder());
    }

    @Inject
    HexDecoder decoder;

}