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

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.concurrent.ThreadLocalRandom.current;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A module injects HelloWorld instance.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
class HelloWorldGuiceModule extends AbstractModule {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @Override
    protected void configure() {
        bind(HelloWorld.class)
                .to(current().nextBoolean()
                    ? HelloWorldImpl.class : HelloWorldDemo.class);
        bind(HelloWorld.class)
                .annotatedWith(Names.named("demo"))
                .to(HelloWorldDemo.class);
        bind(HelloWorld.class)
                .annotatedWith(Names.named("impl"))
                .to(HelloWorldImpl.class);
        bind(HelloWorld.class)
                .annotatedWith(QualifiedDemo.class)
                .to(HelloWorldDemo.class);
        bind(HelloWorld.class)
                .annotatedWith(QualifiedImpl.class)
                .to(HelloWorldImpl.class);
    }
}
