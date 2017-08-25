package com.github.jinahya.verbose.hello;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ServiceLoader;
import static java.util.ServiceLoader.load;

/**
 * A class whose {@code main} method prints {@code hello, world%n} to
 * {@code System.out}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
public class HelloWorldMain {

    /**
     * Prints {@code hello, world} followed by a (system dependent) new line
     * character to {@code System.out}. This method loads an instance of
     * {@link HelloWorld} using {@link ServiceLoader#load(java.lang.Class)} and
     * invokes {@link HelloWorld#write(java.io.OutputStream)} with
     * {@link System#out} and invokes {@link PrintStream#println()} on the
     * result.
     *
     * @param args command line arguments
     * @throws IOException if an I/O error occurs.
     * @see HelloWorld#set(byte[], int)
     */
    public static void main(final String... args) throws IOException {
        load(HelloWorld.class).iterator().next() // <1>
                .write(System.out) // <2>
                .println(); // <3>
    }
}
