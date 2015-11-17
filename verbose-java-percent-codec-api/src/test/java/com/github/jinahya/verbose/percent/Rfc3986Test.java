package com.github.jinahya.verbose.percent;

import static java.lang.invoke.MethodHandles.lookup;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class Rfc3986Test {

    private static final Logger logger = getLogger(lookup().lookupClass());

    @Test
    public void assertMatchesWithUnreservedCharacters() {
        Rfc3986.UNRESERVED_CHARACTERS.stream()
                .map(Object::toString).forEach(s -> {
            final Matcher m = Rfc3986.RESERVED_CHARACTERS_PATTERN.matcher(s);
            assertTrue(m.matches());
        });
    }

    @Test
    public void assertNotMatchesWithReservedCharacters() {
        Rfc3986.RESERVED_CHARACTERS.stream()
                .map(Object::toString).forEach(s -> {
            final Matcher m = Rfc3986.RESERVED_CHARACTERS_PATTERN.matcher(s);
            assertFalse(m.matches());
        });
    }

    @Test
    public void isUnresevedCharacterWithUnreservedCharacters() {
        Rfc3986.UNRESERVED_CHARACTERS.forEach(c -> {
            assertTrue(Rfc3986.isUnreservedCharacter(c));
        });
    }

    @Test
    public void isUnresevedCharacterWithReservedCharacters() {
        Rfc3986.RESERVED_CHARACTERS.forEach(c -> {
            assertFalse(Rfc3986.isUnreservedCharacter(c));
        });
    }

    @Test(invocationCount = 128)
    public void requireUnresevedCharacterWithUnresevedCharacters() {
        Rfc3986.UNRESERVED_CHARACTERS.stream().findAny()
                .ifPresent(Rfc3986::requireUnreservedCharacter);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
          invocationCount = 128)
    public void requireUnresevedCharacterWithResevedCharacters() {
        Rfc3986.RESERVED_CHARACTERS.stream().findAny()
                .ifPresent(Rfc3986::requireUnreservedCharacter);
    }
}
