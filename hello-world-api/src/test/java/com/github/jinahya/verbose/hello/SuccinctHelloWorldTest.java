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
package com.github.jinahya.verbose.hello;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import static java.nio.charset.StandardCharsets.US_ASCII;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class SuccinctHelloWorldTest {

    @Test(enabled = false)
    public void start()
            throws URISyntaxException, IOException, InterruptedException {
        final ProcessBuilder builder = new ProcessBuilder(
                "java", SuccinctHelloWorld.class.getName());
        builder.directory(new File(getClass().getResource("/").toURI()).toPath()
                .resolve("..").resolve("classes").toFile());
        final Process process = builder.start();
        final byte[] bytes = new byte[HelloWorld.BYTES];
        new DataInputStream(process.getInputStream()).readFully(bytes);
        assertEquals(bytes, "hello, world".getBytes(US_ASCII));
        final int exit = process.waitFor();
    }

    @Test
    public void call() throws URISyntaxException, IOException {
        final PrintStream out = System.out;
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final PrintStream ps = new PrintStream(baos, true);
            System.setOut(ps);
            SuccinctHelloWorld.main(new String[0]);
            final byte[] bytes = new byte[HelloWorld.BYTES];
            new DataInputStream(new ByteArrayInputStream(baos.toByteArray()))
                    .readFully(bytes);
            assertEquals(bytes, "hello, world".getBytes(US_ASCII));
        } finally {
            System.setOut(out);
        }
    }

    private transient final Logger logger = getLogger(getClass());
}
