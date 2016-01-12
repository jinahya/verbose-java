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
package com.github.jinahya.verbose.percent;

import java.util.function.Consumer;
import java.util.function.Function;
import javax.inject.Inject;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
abstract class PercentEncoderTest {

    protected void accept(final Consumer<PercentEncoder> consumer) {
        consumer.accept(percentEncoder());
    }

    protected <T> T apply(final Function<PercentEncoder, T> function) {
        return function.apply(percentEncoder());
    }

    protected PercentEncoder percentEncoder() {
        return percentEncoder;
    }

    @Inject
    private PercentEncoder percentEncoder;
}
