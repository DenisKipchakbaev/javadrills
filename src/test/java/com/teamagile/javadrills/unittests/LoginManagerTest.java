package com.teamagile.javadrills.unittests;

import com.teamagile.javadrills.LoginManager;
import com.teamagile.javadrills.SlowLogger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;


public class LoginManagerTest {

    private SlowLogger loggerMock = Mockito.mock(SlowLogger.class);

    @Test
    public void isLoginOK_withNoUsers_returnsFalse() {

        LoginManager lm = getLoginManager();

        Boolean result = lm.isLoginOK("b", "pass");

        assertFalse(result);
    }

    private LoginManager getLoginManager() {
        return new LoginManager(loggerMock);
    }

    @Test
    public void addUser_whenCalled_UserIsAbleToLogin() {
        LoginManager lm = getLoginManager();

        lm.addUser("a", "pass");
        Boolean result = lm.isLoginOK("a", "pass");

        assertTrue(result);
    }

    @Test
    public void addUser_withSlowLogger_slowLoggerNotified() {
        LoginManager lm = getLoginManager();

        lm.addUser("a", "pass");

        verify(loggerMock).write("logon by user 'a' and password 'pass'");
    }

}
