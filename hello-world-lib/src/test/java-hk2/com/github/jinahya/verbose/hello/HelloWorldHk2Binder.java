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
import org.glassfish.hk2.api.AnnotationLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldHk2Binder extends AbstractBinder {

    class DemoLiteral extends AnnotationLiteral<QualifiedDemo>
            implements QualifiedDemo {

        private static final long serialVersionUID = -103484319922866187L;

    }

    class ImplLiteral extends AnnotationLiteral<QualifiedImpl>
            implements QualifiedImpl {

        private static final long serialVersionUID = -4180074935060055710L;

    }

    @Override
    protected void configure() {
        if (current().nextBoolean()) {
            bind(HelloWorldImpl.class).to(HelloWorld.class);
        } else {
            bind(HelloWorldDemo.class).to(HelloWorld.class);
        }
        bind(HelloWorldImpl.class).named("impl").to(HelloWorld.class);
        bind(HelloWorldDemo.class).named("demo").to(HelloWorld.class);
        bind(HelloWorldImpl.class)
                .qualifiedBy(new ImplLiteral())
                .to(HelloWorld.class);
        bind(HelloWorldDemo.class)
                .qualifiedBy(new DemoLiteral())
                .to(HelloWorld.class);
        logger.debug("configured");
    }

    private transient final Logger logger = getLogger(getClass());
}
