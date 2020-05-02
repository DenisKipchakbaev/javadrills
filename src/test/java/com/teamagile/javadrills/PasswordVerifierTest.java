package com.teamagile.javadrills;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordVerifierTest {

    @Test
    public void verify_stringLengthMoreThan8_ok() {
        new PasswordVerifier("a1Aabcdefg").verify();
    }

    @Test
    public void verify_stringLengthLessThan8_throwsException() {
        PasswordVerificationException e = assertThrows(
                PasswordVerificationException.class,
                () -> new PasswordVerifier("aaa").verify());

        assertTrue(e.getMessage().contains("password should be larger than 8 chars"));
    }

    @Test
    public void verify_null_throwsException() {
        PasswordVerificationException e = assertThrows(
                PasswordVerificationException.class,
                () -> new PasswordVerifier(null).verify());

        assertEquals("password should not be null", e.getMessage());
    }

    @Test
    public void verify_noUppercaseLetter_throwsException() {
        PasswordVerificationException e = assertThrows(PasswordVerificationException.class,
                () -> new PasswordVerifier("000").verify());

        assertTrue(e.getMessage().contains("password should have one uppercase letter at least"));
    }

    @Test
    public void verify_lastLetterUppercase_ok() {
        new PasswordVerifier("1abcdefgI").verify();
    }

    @Test
    public void verify_noLowercaseLetter_throwsException() {
        PasswordVerificationException e = assertThrows(PasswordVerificationException.class,
                () -> new PasswordVerifier("ABCD").verify());

        assertTrue(e.getMessage().contains("password should have one lowercase letter at least"));
    }

    @Test
    public void verify_noNumber_throwsException() {
        PasswordVerificationException e = assertThrows(PasswordVerificationException.class,
                () -> new PasswordVerifier("abc").verify());

        assertTrue(e.getMessage().contains("password should have one number at least"));
    }

    @Test
    public void verify_notNullAndLargeEnoughAndAllLowercase_ok() {
        new PasswordVerifier("abcdefghi").verify();
    }

    @Test
    public void verify_notNullAndLargeEnoughAndAllUppercase_ok() {
        new PasswordVerifier("ABCDEFGHI").verify();
    }

    @Test
    public void verify_notNullAndUppercaseAndContainsDigit_ok() {
        new PasswordVerifier("1ABC").verify();
    }

    @Test
    public void verify_shortAndNoUppercase_throwsExceptionWithListOfUnmetConditions() {
        PasswordVerificationException e = assertThrows(PasswordVerificationException.class,
                () -> new PasswordVerifier("abc").verify());

        assertTrue(e.getMessage().contains("password should have one number at least"));
        assertTrue(e.getMessage().contains("password should have one uppercase letter at least"));
        assertTrue(e.getMessage().contains("password should be larger than 8 chars"));
        assertFalse(e.getMessage().contains("password should have one lowercase letter at least"));
        assertFalse(e.getMessage().contains("password should not be null"));
    }


}