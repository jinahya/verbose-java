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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.fail;

/**
 * Test class uses {@link ServiceLoader} for {@link #implementation()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldServiceTest extends HelloWorldDataTest {

    @Override
    HelloWorld implementation() {
        if (implementation == null) {
            final ServiceLoader<HelloWorld> loader
                    = ServiceLoader.load(HelloWorld.class);
            final Iterator<HelloWorld> iterator = loader.iterator();
            try {
                implementation = iterator.next();
                logger.debug("implementation loaded: {}", implementation);
            } catch (final NoSuchElementException nsee) {
                fail("failed to load an implementation", nsee);
            }
        }
        return implementation;
    }

    private transient final Logger logger = getLogger(getClass());

    private HelloWorld implementation;
}
