/*
 * Copyright 2016 Jin Kwon &lt;jinahya_at_gmail.com&gt;.
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
package com.github.jinahya.verbose.hex;

import static java.lang.invoke.MethodHandles.lookup;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class InvokedMethodListener implements IInvokedMethodListener {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @Override
    public void beforeInvocation(final IInvokedMethod method,
                                 final ITestResult testResult) {
        //logger.debug("invoking {}, {}", method, testResult);
    }

    @Override
    public void afterInvocation(final IInvokedMethod method,
                                final ITestResult testResult) {
        //logger.debug("invoked {}, {}", method, testResult);
    }
}
