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

import com.google.inject.AbstractModule;
import static java.lang.invoke.MethodHandles.lookup;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A module binds {@link HexDecoder} to {@link HexDecoderMock}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class HexDecoderMockModule extends AbstractModule {

    private static final Logger logger = getLogger(lookup().lookupClass());

    /**
     * Binds injection points of {@link HexDecoder} to a mock instance.
     */
    @Override
    protected void configure() {
        bind(HexDecoder.class).to(HexDecoderMock.class);
    }
}
