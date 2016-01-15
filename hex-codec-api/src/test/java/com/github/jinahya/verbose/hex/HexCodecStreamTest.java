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

import static com.github.jinahya.verbose.hex.HexCodecTests.fillFile;
import static com.github.jinahya.verbose.hex.HexCodecTests.tempFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.io.FileUtils.contentEquals;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HexCodecStreamTest {

    @Test
    public void buffered() throws IOException {
        final byte[] createdBytes = new byte[current().nextInt(1024)];
        current().nextBytes(createdBytes);
        final InputStream createdStream
                = new ByteArrayInputStream(createdBytes);
        final ByteArrayOutputStream encodedStream
                = new ByteArrayOutputStream(createdBytes.length << 1);
        try (OutputStream encoderStream
                = new HexEncoderStream(encodedStream, new HexEncoderDemo())) {
            IOUtils.copy(createdStream, encoderStream);
            encoderStream.flush();
        }
        final byte[] encodedBytes = encodedStream.toByteArray();
        final ByteArrayOutputStream decodedStream
                = new ByteArrayOutputStream(encodedBytes.length >> 1);
        try (InputStream decoderStream = new HexDecoderStream(
                new ByteArrayInputStream(encodedBytes), new HexDecoderDemo())) {
            IOUtils.copy(decoderStream, decodedStream);
            decodedStream.flush();
        }
        final byte[] decodedBytes = decodedStream.toByteArray();
        assertEquals(decodedBytes, createdBytes);
    }

    @Test
    public void file() throws IOException {
        final File createdFile = fillFile(tempFile());
        final File encodedFile = tempFile();
        try (InputStream input = new FileInputStream(createdFile)) {
            try (OutputStream output = new HexEncoderStream(
                    new FileOutputStream(encodedFile), new HexEncoderDemo())) {
                IOUtils.copy(input, output);
                output.flush();
            }
        }
        final File decodedFile = tempFile();
        try (InputStream input = new FileInputStream(encodedFile)) {
            try (OutputStream output = new HexEncoderStream(
                    new FileOutputStream(decodedFile), new HexEncoderDemo())) {
                IOUtils.copy(input, output);
                output.flush();
            }
        }
        contentEquals(decodedFile, createdFile);
    }

    private transient final Logger logger = getLogger(getClass());

}
