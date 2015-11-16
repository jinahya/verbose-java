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

import static java.util.Objects.isNull;
import java.util.function.BiPredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public final class HelloWorldSetParameterPredicates {

    public static final Predicate<byte[]> ARRAY_NOT_NULL = (a) -> !isNull(a);

    public static final IntPredicate OFFSET_POSITIVE = (o) -> o >= 0;

    public static final BiPredicate<byte[], Integer> ENOUGHT_SPACE
            = (a, o) -> o + HelloWorld.BYTES <= a.length;

    private HelloWorldSetParameterPredicates() {

        super();
    }

}
