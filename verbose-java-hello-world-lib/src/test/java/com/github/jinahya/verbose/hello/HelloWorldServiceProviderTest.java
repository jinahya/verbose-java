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

import static java.lang.invoke.MethodHandles.lookup;
import java.util.Iterator;
import java.util.ServiceLoader;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import org.testng.annotations.BeforeClass;
import static java.util.logging.Logger.getLogger;
import static org.testng.Assert.fail;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldServiceProviderTest extends HelloWorldTest {

    @BeforeClass
    protected void load() {
        final ServiceLoader<HelloWorld> loader
                = ServiceLoader.load(HelloWorld.class);
        logger.log(INFO, "loader: {0}", loader);
        for (final Iterator<HelloWorld> i = loader.iterator(); i.hasNext();) {
            final HelloWorld loaded = i.next();
            logger.log(INFO, "loaded: {0}", loaded);
            if (implementation == null) {
                implementation = loaded;
            }
        }
        logger.log(INFO, "provided implementation: {0}", implementation);
        if (implementation == null) {
            fail("no implementation provided");
        }
    }

    @Override
    HelloWorld implementation() {
        return implementation;
    }

    private transient final Logger logger
            = getLogger(lookup().lookupClass().getCanonicalName());

    private transient HelloWorld implementation;
}
