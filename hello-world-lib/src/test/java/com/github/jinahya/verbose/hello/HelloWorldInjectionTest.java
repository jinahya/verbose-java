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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Test class for testing {@link HelloWorldImpl} using Dependency Injection.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public abstract class HelloWorldInjectionTest extends HelloWorldDataTest {

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
        assertTrue(namedAsImpl instanceof HelloWorldImpl,
                   namedAsImpl + " is not an instance of HelloWorldImpl");
        assertTrue(namedAsDemo instanceof HelloWorldDemo,
                   namedAsDemo + " is not an instance of HelloWorldDemo");
        assertTrue(qualifiedWithImpl instanceof HelloWorldImpl,
                   qualifiedWithImpl + " is not an instance of HelloWorldImpl");
        assertTrue(qualifiedWithDemo instanceof HelloWorldDemo,
                   qualifiedWithDemo + " is not an instance of HelloWorldDemo");
        if (current().nextBoolean()) {
            logger.debug("selecting a field using reflection");
            final List<Field> fields = new ArrayList<>();
            for (final Field field
                 : HelloWorldInjectionTest.class.getDeclaredFields()) {
                if (!HelloWorld.class.isAssignableFrom(field.getType())) {
                    continue;
                }
                if (field.getAnnotation(Inject.class) == null) {
                    continue;
                }
                fields.add(field);
            }
            if (!fields.isEmpty()) {
                Collections.shuffle(fields);
                final Field field
                        = fields.get(current().nextInt(fields.size()));
                logger.debug("selected: {}", field.getName());
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    return (HelloWorld) field.get(this);
                } catch (final IllegalAccessException iae) {
                    logger.warn("failed to get value from {}", field, iae);
                }
            }
        }
        if (current().nextBoolean()) {
            logger.debug("selecting a field using reflection and stream");
            final List<HelloWorld> values
                    = Arrays.stream(
                            HelloWorldInjectionTest.class.getDeclaredFields())
                    .filter(f -> HelloWorld.class.isAssignableFrom(f.getType()))
                    .filter(f -> f.getAnnotation(Inject.class) != null)
                    .map(f -> {
                        logger.debug("selected: {}", f.getName());
                        if (!f.isAccessible()) {
                            f.setAccessible(true);
                        }
                        try {
                            return (HelloWorld) f.get(this);
                        } catch (final IllegalAccessException iae) {
                            logger.warn("failed to get value from {}", f, iae);
                        }
                        return null;
                    }).collect(toList());
            if (!values.isEmpty()) {
                Collections.shuffle(values);
                return values.get(current().nextInt(values.size()));
            }
        }
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

    void debug(final Logger logger) {
        logger.debug("any: {}", any);
        logger.debug("namedAsImpl: {}", namedAsImpl);
        logger.debug("namedAsDemo: {}", namedAsDemo);
        logger.debug("qualifiedWithImpl: {}", qualifiedWithImpl);
        logger.debug("qualifiedWithDemo: {}", qualifiedWithDemo);
    }

    void debug() {
        debug(logger);
    }

    private transient final Logger logger = getLogger(getClass());

    /**
     * An injected {@code Helloworld} without any qualifiers.
     */
    @Inject
    HelloWorld any;

    /**
     * An injected {@code HelloWorld} which is qualified with
     * {@code @Named("demo")}.
     */
    @Inject
    @Named("demo")
    HelloWorld namedAsDemo;

    /**
     * An injected {@code Helloworld} which is qualified with
     * {@code Named("impl")}.
     */
    @Inject
    @Named("impl")
    HelloWorld namedAsImpl;

    /**
     * An injected {@code HelloWorld} which is qualified with
     * {@code @QualifiedDemo}.
     */
    @Inject
    @QualifiedDemo
    HelloWorld qualifiedWithDemo;

    /**
     * An injected {@code HelloWorld} which is qualified with
     * {@code @QualifiedImpl}.
     */
    @Inject
    @QualifiedImpl
    HelloWorld qualifiedWithImpl;
}
