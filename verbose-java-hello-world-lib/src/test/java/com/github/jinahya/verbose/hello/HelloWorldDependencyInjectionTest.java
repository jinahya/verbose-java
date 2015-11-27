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

import static java.util.concurrent.ThreadLocalRandom.current;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Test class for testing {@link HelloWorldImpl} using Dependency Injection.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class HelloWorldDependencyInjectionTest
        extends HelloWorldDataTest {

    @Override
    HelloWorld implementation() {
        switch (current().nextInt(5)) {
            case 0:
                logger.debug("selecting any");
                return any;
            case 1:
                logger.debug("selecting namedAsImpl");
                return namedAsImpl;
            case 2:
                logger.debug("selecting namedAsDemo");
                return namedAsDemo;
            case 3:
                logger.debug("selecting qualifiedWithImpl");
                return qualifiedWithImpl;
            default:
                logger.debug("selecting qualifiedWithDemo");
                return qualifiedWithDemo;
        }
    }

    private transient final Logger logger = getLogger(getClass());

    @Inject
    HelloWorld any;

    @Inject
    @Named("impl")
    HelloWorld namedAsImpl;

    @Inject
    @Named("demo")
    HelloWorld namedAsDemo;

    @Inject
    @Impl
    HelloWorld qualifiedWithImpl;

    @Inject
    @Demo
    HelloWorld qualifiedWithDemo;
}
