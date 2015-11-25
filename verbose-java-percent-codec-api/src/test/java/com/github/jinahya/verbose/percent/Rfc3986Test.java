package com.github.jinahya.verbose.percent;

import static java.lang.invoke.MethodHandles.lookup;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import org.testng.annotations.Test;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class Rfc3986Test {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @Test
    public void assertMatchesWithUnreservedCharacters() {
        Rfc3986Constants.UNRESERVED_CHARACTERS.stream()
                .map(Object::toString).forEach(s -> {
            final Matcher m
                    = Rfc3986Constants.UNRESERVED_CHARACTERS_PATTERN.matcher(s);
            assertTrue(m.matches());
        });
    }

    @Test
    public void assertNotMatchesWithReservedCharacters() {
        Rfc3986Constants.RESERVED_CHARACTERS.stream()
                .map(Object::toString).forEach(s -> {
            final Matcher m
                    = Rfc3986Constants.UNRESERVED_CHARACTERS_PATTERN.matcher(s);
            assertFalse(m.matches());
        });
    }

    @Test
    public void assertEachReservedCharacterIsAReservedCharacter() {
        Rfc3986Constants.RESERVED_CHARACTERS.forEach(c -> {
            assertTrue(Rfc3986Utilities.isReservedCharacter(c));
        });
    }

    @Test
    public void assertEachReserviceCharacterIsNotAnUnreservedCharacter() {
        Rfc3986Constants.RESERVED_CHARACTERS.forEach(c -> {
            assertFalse(Rfc3986Utilities.isUnreservedCharacter(c));
        });
    }

    @Test(invocationCount = 128)
    public void assertEachUnreservedCharacterIsAnUnreservedCharacter() {
        Rfc3986Constants.UNRESERVED_CHARACTERS.forEach(c -> {
            assertTrue(Rfc3986Utilities.isUnreservedCharacter(c));
        });
    }

    @Test(invocationCount = 128)
    public void assertEachUnreservedCharacterIsNotAReservedCharacter() {
        Rfc3986Constants.UNRESERVED_CHARACTERS.forEach(c -> {
            assertFalse(Rfc3986Utilities.isReservedCharacter(c));
        });
    }
}
