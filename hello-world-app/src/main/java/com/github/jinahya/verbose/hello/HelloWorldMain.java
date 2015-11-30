package com.github.jinahya.verbose.hello;

import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.stream.IntStream;

/**
 * A class whose {@code main} method prints {@code hello, world} to
 * {@code System.out}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldMain {

    private static final HelloWorld LOADED;

    static {
        try {
            LOADED = ServiceLoader.load(HelloWorld.class).iterator().next();
        } catch (final NoSuchElementException nsee) {
            throw new InstantiationError("no implementation loaded");
        }
    }

    /**
     * Prints {@code hello, world} to {@code System.out}.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = 0;
        LOADED.set(array, offset);
        //System.out.printf("%s\n", new String(array, US_ASCII));
        IntStream.range(0, array.length).forEach(
                i -> System.out.printf("%c", (char) (array[i] & 0xFF))
        );
        System.out.println();
    }
}
