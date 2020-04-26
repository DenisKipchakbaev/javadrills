package com.teamagile.javadrills.unittests;

import com.teamagile.javadrills.LoginManager;
import com.teamagile.javadrills.SlowLogger;
import com.teamagile.javadrills.SlowWebService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;


public class LoginManagerTest {

    private SlowLogger loggerMock = Mockito.mock(SlowLogger.class);
    private SlowWebService webServiceMock = Mockito.mock(SlowWebService.class);

    @Test
    public void isLoginOK_withNoUsers_returnsFalse() {

        LoginManager lm = getLoginManager();

        Boolean result = lm.isLoginOK("b", "pass");

        assertFalse(result);
    }

    private LoginManager getLoginManager() {
        return new LoginManager(loggerMock, webServiceMock);
    }

    @Test
    public void addUser_whenCalled_UserIsAbleToLogin() throws InterruptedException {
        LoginManager lm = getLoginManager();

        lm.addUser("a", "pass");
        Boolean result = lm.isLoginOK("a", "pass");

        assertTrue(result);
    }

    @Test
    public void addUser_withSlowLogger_slowLoggerNotified() throws InterruptedException {
        LoginManager lm = getLoginManager();

        lm.addUser("a", "pass");

        verify(loggerMock).write("logon by user 'a' and password 'pass'");
    }

    @Test
    public void addUser_loggerThrowsError_webServiceNotified() throws InterruptedException {
        LoginManager lm = getLoginManager();
        doThrow(RuntimeException.class).when(loggerMock).write(anyString());

        lm.addUser("a", "pass");

        verify(webServiceMock).notify("the error from the logger");
    }
}
