package com.teamagile.javadrills;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginManagerWIthStaticsTest {

    private static class LoginManagerWIthStaticsOverride extends LoginManagerWIthStatics {
        String written;

        @Override
        void log(String message) {
            written = message;
        }
    }

    @Test
    void addUser_staticLoggerCalled() throws Throwable {
        LoginManagerWIthStaticsOverride lm = new LoginManagerWIthStaticsOverride();

        lm.addUser("a", "pass");

        assertEquals("user added: a, pass", lm.written);
    }
}