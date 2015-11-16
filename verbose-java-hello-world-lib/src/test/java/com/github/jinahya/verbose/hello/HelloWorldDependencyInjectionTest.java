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

import com.google.inject.Guice;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.inject.Inject;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldDependencyInjectionTest extends HelloWorldTest {

    @BeforeClass
    protected void inject() {

        Guice.createInjector((b) -> {

            //b.bind(HelloWorld.class).to(HelloWorldImpl.class);
            //b.bind(HelloWorld.class).to(HelloWorldDemo.class);
            //b.bind(HelloWorld.class)
            //    .annotatedWith(named("impl"))
            //   .to(HelloWorldImpl.class);
            //b.bind(HelloWorld.class)
            //    .annotatedWith(named("demo"))
            //    .to(HelloWorldDemo.class);
            b.bind(HelloWorld.class)
                    .annotatedWith(Impl.class)
                    .to(HelloWorldImpl.class);

            b.bind(HelloWorld.class)
                    .annotatedWith(Demo.class)
                    .to(HelloWorldDemo.class);
        }).injectMembers(this);

        logger.log(INFO, "implementation injected: {0}", implementation);
    }

    @Override
    HelloWorld implementation() {

        return implementation;
    }

    private transient final Logger logger
            = getLogger(lookup().lookupClass().getCanonicalName());

    @Inject
    //@Named("impl")
    //@Named("demo")
    @Impl
    //@Demo
    private transient HelloWorld implementation;

}
