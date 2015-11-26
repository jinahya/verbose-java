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

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.name.Names;
import java.util.concurrent.ThreadLocalRandom;
import static java.util.concurrent.ThreadLocalRandom.current;
import javax.inject.Inject;
import org.testng.annotations.BeforeClass;
import javax.inject.Named;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldDependencyInjectionTest extends HelloWorldDataTest {

    @BeforeClass
    protected void inject() {
        final Module m1 = new Module() {
            @Override
            public void configure(final Binder binder) {
                binder.bind(HelloWorld.class).to(
                        current().nextBoolean()
                        ? HelloWorldImpl.class : HelloWorldDemo.class);
            }
        };
        final Module m2 = b -> {
            b.bind(HelloWorld.class)
                    .annotatedWith(Names.named("impl"))
                    .to(HelloWorldImpl.class);
        };
        final Module m3 = b -> {
            b.bind(HelloWorld.class)
                    .annotatedWith(Names.named("demo"))
                    .to(HelloWorldDemo.class);
        };
        final Module m4 = b -> {
            b.bind(HelloWorld.class)
                    .annotatedWith(Impl.class)
                    .to(HelloWorldImpl.class);
        };
        final Module m5
                = b -> b.bind(HelloWorld.class)
                .annotatedWith(Demo.class)
                .to(HelloWorldDemo.class);
        Guice.createInjector(m1, m2, m3, m4, m5).injectMembers(this);
    }

    @Override
    HelloWorld implementation() {
        switch (ThreadLocalRandom.current().nextInt(5)) {
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

    private final Logger logger = getLogger(getClass());

    @Inject
    private HelloWorld any;

    @Inject
    @Named("impl")
    private HelloWorld namedAsImpl;

    @Inject
    @Named("demo")
    private HelloWorld namedAsDemo;

    @Inject
    @Impl
    private HelloWorld qualifiedWithImpl;

    @Inject
    @Demo
    private HelloWorld qualifiedWithDemo;
}
