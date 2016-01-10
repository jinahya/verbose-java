package com.github.jinahya.verbose.percent;

import static java.lang.invoke.MethodHandles.lookup;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

public class Rfc3986Test {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @Test
    public void assertMatchesWithUnreservedCharacters() {
        PercentConstants.UNRESERVED_CHARACTERS.stream()
                .map(Object::toString).forEach(s -> {
            final Matcher m
                    = PercentConstants.UNRESERVED_CHARACTERS_PATTERN.matcher(s);
            assertTrue(m.matches());
        });
    }

    @Test
    public void assertNotMatchesWithReservedCharacters() {
        PercentConstants.RESERVED_CHARACTERS.stream()
                .map(Object::toString).forEach(s -> {
            final Matcher m
                    = PercentConstants.UNRESERVED_CHARACTERS_PATTERN.matcher(s);
            assertFalse(m.matches());
        });
    }

    @Test
    public void assertEachReservedCharacterIsAReservedCharacter() {
        PercentConstants.RESERVED_CHARACTERS.forEach(c -> {
            assertTrue(PercentUtilities.isReservedCharacter(c));
        });
    }

    @Test
    public void assertEachReserviceCharacterIsNotAnUnreservedCharacter() {
        PercentConstants.RESERVED_CHARACTERS.forEach(c -> {
            assertFalse(PercentUtilities.isUnreservedCharacter(c));
        });
    }

    @Test(invocationCount = 128)
    public void assertEachUnreservedCharacterIsAnUnreservedCharacter() {
        PercentConstants.UNRESERVED_CHARACTERS.forEach(c -> {
            assertTrue(PercentUtilities.isUnreservedCharacter(c));
        });
    }

    @Test(invocationCount = 128)
    public void assertEachUnreservedCharacterIsNotAReservedCharacter() {
        PercentConstants.UNRESERVED_CHARACTERS.forEach(c -> {
            assertFalse(PercentUtilities.isReservedCharacter(c));
        });
    }
}
