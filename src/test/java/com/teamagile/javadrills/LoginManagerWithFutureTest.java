package com.teamagile.javadrills;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginManagerWithFutureTest {

    private static class LoginManagerWithFutureOverride extends LoginManagerWithFuture {
        String written;

        @Override
        void writeLog(String message) {
            written = message;
        }
    }

    @Test
    void addUser_callsNewLoggerWrite() throws Throwable {
        LoginManagerWithFutureOverride lm = new LoginManagerWithFutureOverride();

        lm.addUser("a", "pass");

        assertEquals("user added: a, pass", lm.written);
    }
}