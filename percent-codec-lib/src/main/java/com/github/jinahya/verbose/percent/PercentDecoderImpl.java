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
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import static java.util.ServiceLoader.load;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class implementing {@code PercentDecoder}. This class uses an instance of
 * {@link HexDecoder} loaded with {@link ServiceLoader#load(java.lang.Class)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class PercentDecoderImpl implements PercentDecoder {

    @Override
    public int decodeOctet(final ByteBuffer encoded) {
        final byte e = encoded.get();
        if (e == 0x25) { // '%' <1>
            return hexDecoder().decodeOctet(encoded); // <2>
        }
        return e; // <3>
    }

    /**
     * Returns an instance of {@link HexDecoder}.
     *
     * @return an instance of {@link HexDecoder}
     */
    protected HexDecoder hexDecoder() {
        if (hexDecoder == null) {
            try {
                hexDecoder = load(HexDecoder.class).iterator().next();
                logger.log(Level.FINE, "hex decoder loaded: {0}", hexDecoder);
            } catch (final NoSuchElementException nsee) {
                throw new RuntimeException("no hex decoder loaded", nsee);
            }
        }
        return hexDecoder;
    }

    private transient final Logger logger
            = Logger.getLogger(getClass().getName());

    private HexDecoder hexDecoder;
}
