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

import com.github.jinahya.verbose.hex.HexDecoder;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import static java.util.logging.Logger.getLogger;
import static java.util.logging.Logger.getLogger;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentDecoderImpl implements PercentDecoder {

    private static final Logger logger
            = getLogger(PercentDecoderImpl.class.getPackage().getName());

    @Override
    public int decodeSingle(final ByteBuffer encoded) {
        final byte e = encoded.get();
        if ((e >= 0x30 && e <= 0x39) // digit
            || (e >= 0x41 && e <= 0x5A) // upper case alpha
            || (e >= 0x61 && e <= 0x7A) // lower case alpha
            || e == 0x2D // '-'
            || e == 0x5F // '_'
            || e == 0x2E // '.'
            || e == 0x7E) { // '~'
            return e;
        }
        assert e == 0x25; // '%'
        return hexDecoder().decodeSingle(encoded);
    }

    private HexDecoder hexDecoder() {
        if (hexDecoder == null) {
            final ServiceLoader<HexDecoder> loader
                    = ServiceLoader.load(HexDecoder.class);
            final Iterator<HexDecoder> i = loader.iterator();
            try {
                hexDecoder = i.next();
                logger.log(Level.FINE, "hex decoder loaded: {0}", hexDecoder);
            } catch (final NoSuchElementException nsee) {
                throw new RuntimeException("no instance loaded", nsee);
            }
        }
        return hexDecoder;
    }

    private HexDecoder hexDecoder;
}
