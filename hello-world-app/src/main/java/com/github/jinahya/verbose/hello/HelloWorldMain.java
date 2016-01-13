package com.github.jinahya.verbose.hello;

import java.io.PrintStream;
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
     * Prints {@code hello, world%n} to {@code System.out}.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        new HelloWorldMain().print(System.out);
    }

    private HelloWorldMain() {
        super();
    }

    /**
     * Prints {@code hello, world%n} to given {@code PrintStream}.
     *
     * @param out the {@code PrintStream} to which {@code hello, world%n} is
     * printed
     */
    private void print(final PrintStream out) {
        if (out == null) { // <1>
            throw new NullPointerException("null out");
        }
        final HelloWorld service // <2>
                = load(HelloWorld.class).iterator().next();
        final byte[] array = new byte[HelloWorld.BYTES]; // <3>
        final int offset = 0;
        service.set(array, offset);
        final String string = new String(array, US_ASCII); // <4>
        out.printf("%s%n", string); // <5>
    }
}
