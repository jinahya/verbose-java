package com.github.jinahya.verbose.hello;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.ServiceLoader.load;

/**
 * A class whose {@code main} method prints {@code hello, world%n} to
 * {@code System.out}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldMain {

    /**
     * Prints {@code hello, world} to {@code System.out}.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final HelloWorld loaded = load(HelloWorld.class).iterator().next();
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = 0;
        loaded.set(array, offset);
        final String string = new String(array, US_ASCII);
        System.out.printf("%s%n", string);
    }
}
