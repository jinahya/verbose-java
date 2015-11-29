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

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.slf4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import static org.slf4j.LoggerFactory.getLogger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @param <T>
 */
abstract class HelloWorldCdiTest<T extends HelloWorldCdiTest<T>>
        extends HelloWorldDependencyInjectionTest {

    public HelloWorldCdiTest(final Class<T> cdi) {
        super();
        this.cdi = cdi;
    }

    @BeforeClass
    protected void startContexts() {
        container = CdiContainerLoader.getCdiContainer();
        container.boot();
        container.getContextControl().startContexts();
    }

    @BeforeClass(dependsOnMethods = "startContexts")
    protected void inject() {
        final BeanManager manager = container.getBeanManager();
        final CreationalContext<T> context
                = manager.createCreationalContext(null);
        final AnnotatedType<T> type = manager.createAnnotatedType(cdi);
        final InjectionTarget<T> target = manager.createInjectionTarget(type);
        target.inject(cdi.cast(this), context);
    }

    @AfterClass
    protected void stopContexts() {
        container.getContextControl().stopContexts();
        container.shutdown();
        logger.debug("weld shutdowned");
    }

    private transient final Logger logger = getLogger(getClass());
    private final Class<T> cdi;
    private transient CdiContainer container;
}
