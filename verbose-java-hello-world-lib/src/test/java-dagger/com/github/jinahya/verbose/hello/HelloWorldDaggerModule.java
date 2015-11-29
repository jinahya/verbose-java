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

import dagger.Module;
import dagger.Provides;
import static java.util.concurrent.ThreadLocalRandom.current;
import javax.inject.Named;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Module(injects = HelloWorldDaggerTest.class)
public class HelloWorldDaggerModule {

    @Provides
    HelloWorld providesHelloWorld() {
        return current().nextBoolean()
               ? new HelloWorldImpl() : new HelloWorldDemo();
    }

    @Provides
    @Named("impl")
    HelloWorld providesHelloWorldnamedAsImpl() {
        return new HelloWorldImpl();
    }

    @Provides
    @Named("demo")
    HelloWorld providesHelloWorldNamedAsDemo() {
        return new HelloWorldDemo();
    }

    @Provides
    @Impl
    HelloWorld providesHelloWorldQualifiedWithImpl() {
        return new HelloWorldImpl();
    }

    @Provides
    @Demo
    HelloWorld providesHelloWorldQualifiedWithDemo() {
        return new HelloWorldDemo();
    }
}
