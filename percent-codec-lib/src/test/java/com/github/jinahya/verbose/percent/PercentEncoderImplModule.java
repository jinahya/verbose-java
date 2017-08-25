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

import com.github.jinahya.verbose.hex.HexEncoder;
import com.google.inject.AbstractModule;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.ServiceLoader.load;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A module binds {@link PercentEncoder} class to {@link PercentEncoderImpl}
 * class.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class PercentEncoderImplModule extends AbstractModule {

    private static final Logger logger = getLogger(lookup().lookupClass());

    // -------------------------------------------------------------------------
    @Override
    protected void configure() {
        bind(PercentEncoder.class).toInstance(new PercentEncoderImpl(
                () -> load(HexEncoder.class).iterator().next()));
    }
}
