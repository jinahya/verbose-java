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
package com.github.jinahya.verbose.percent;

import com.github.jinahya.verbose.hex.HexEncoder;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentEncoderImpl implements PercentEncoder {

    private static final Logger logger
            = getLogger(PercentDecoderImpl.class.getPackage().getName());

    @Override
    public void encodeSingle(final int decoded, final ByteBuffer encoded) {
        if ((decoded >= 0x30 && decoded <= 0x39) // digit
            || (decoded >= 0x41 && decoded <= 0x5A) // upper case alpha
            || (decoded >= 0x61 && decoded <= 0x7A) // lower case alpha
            || decoded == 0x2D // '-'
            || decoded == 0x5F // '_'
            || decoded == 0x2E // '.'
            || decoded == 0x7E) { // '~'
            encoded.put((byte) decoded);
            return;
        }
        encoded.put((byte) 0x25); // '%'
        hexEncoder().encodeSingle(decoded, encoded);
    }

    private HexEncoder hexEncoder() {
        if (hexEncoder == null) {
            final ServiceLoader<HexEncoder> loader
                    = ServiceLoader.load(HexEncoder.class);
            final Iterator<HexEncoder> i = loader.iterator();
            try {
                hexEncoder = i.next();
                logger.log(Level.FINE, "hex decoder loaded: {0}", hexEncoder);
            } catch (final NoSuchElementException nsee) {
                throw new RuntimeException("no instance loaded", nsee);
            }
        }
        return hexEncoder;
    }

    private HexEncoder hexEncoder;
}
