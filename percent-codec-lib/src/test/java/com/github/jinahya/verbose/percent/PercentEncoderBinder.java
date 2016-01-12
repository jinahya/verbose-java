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

import static java.util.Objects.requireNonNull;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T> percent encoder type
 */
abstract class PercentEncoderBinder<T extends PercentEncoder>
        extends AbstractBinder {

    public PercentEncoderBinder(final Class<T> serviceType) {
        super();

        this.serviceType = requireNonNull(serviceType, "null serviceType");
    }

    @Override
    protected void configure() {
        bind(serviceType).to(PercentEncoder.class);
    }

    protected final Class<T> serviceType;
}
