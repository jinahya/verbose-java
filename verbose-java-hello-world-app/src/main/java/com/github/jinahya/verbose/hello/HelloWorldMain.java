package com.github.jinahya.verbose.hello;

import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
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

    public static void main(final String[] args) {
        final byte[] array = new byte[HelloWorld.BYTES];
        final int offset = 0;
        LOADED.set(array, offset);
        System.out.printf("%s\n", new String(array));
    }

}
