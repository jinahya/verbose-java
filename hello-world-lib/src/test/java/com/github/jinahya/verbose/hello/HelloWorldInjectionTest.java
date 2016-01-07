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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * A test class using Dependency Injection.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
abstract class HelloWorldInjectionTest extends HelloWorldDataTest {

    /**
     * {@inheritDoc} The {@code implementation()} method of
     * {@code HelloWorldInjectionTest} class returns the value of a randomly
     * selected field which is one of {@link #any}, {@link #namedAsDemo},
     * {@link #namedAsImpl}, {@link #qualifiedWithDemo}, or
     * {@link #qualifiedWithImpl}.
     *
     * @return {@inheritDoc}
     */
    @Override
    HelloWorld implementation() {
        assertNotNull(any, "any is null");
        assertTrue(namedAsDemo instanceof HelloWorldDemo,
                   namedAsDemo + " is not an instance of HelloWorldDemo");
        assertTrue(namedAsImpl instanceof HelloWorldImpl,
                   namedAsImpl + " is not an instance of HelloWorldImpl");
        assertTrue(qualifiedWithDemo instanceof HelloWorldDemo,
                   qualifiedWithDemo + " is not an instance of HelloWorldDemo");
        assertTrue(qualifiedWithImpl instanceof HelloWorldImpl,
                   qualifiedWithImpl + " is not an instance of HelloWorldImpl");
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

    /**
     * An injectable {@code Helloworld} without any qualifiers.
     */
    @Inject
    HelloWorld any;

    /**
     * An injectable {@code HelloWorld} which is qualified with
     * {@code @Named("demo")}.
     */
    @Inject
    @Named("demo")
    HelloWorld namedAsDemo;

    /**
     * An injectable {@code Helloworld} which is qualified with
     * {@code Named("impl")}.
     */
    @Inject
    @Named("impl")
    HelloWorld namedAsImpl;

    /**
     * An injectable {@code HelloWorld} which is qualified with
     * {@code @QualifiedDemo}.
     */
    @Inject
    @QualifiedDemo
    HelloWorld qualifiedWithDemo;

    /**
     * An injectable {@code HelloWorld} which is qualified with
     * {@code @QualifiedImpl}.
     */
    @Inject
    @QualifiedImpl
    HelloWorld qualifiedWithImpl;
}
