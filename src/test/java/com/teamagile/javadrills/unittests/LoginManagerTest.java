package com.teamagile.javadrills.unittests;

import com.teamagile.javadrills.LoginManager;
import com.teamagile.javadrills.SlowLogger;
import com.teamagile.javadrills.SlowWebService;
import com.teamagile.javadrills.TraceMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class LoginManagerTest {

    private final SlowLogger loggerMock = Mockito.mock(SlowLogger.class);
    private final SlowWebService webServiceMock = Mockito.mock(SlowWebService.class);

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

        ArgumentCaptor<TraceMessage> traceMessageCaptor = ArgumentCaptor.forClass(TraceMessage.class);
        verify(loggerMock).write(traceMessageCaptor.capture());
        TraceMessage traceMessage = traceMessageCaptor.getValue();
        assertEquals("logon by user 'a' and password 'pass'", traceMessage.getText());
        assertEquals(100, traceMessage.getSeverity());
    }

    @Test
    public void addUser_loggerThrowsError_webServiceNotified() throws InterruptedException {
        LoginManager lm = getLoginManager();
        doThrow(RuntimeException.class).when(loggerMock).write(any(TraceMessage.class));

        lm.addUser("a", "pass");

        verify(webServiceMock).notify("the error from the logger");
    }

    @Test
    public void changePassword_existingUser_loggerNotified() throws InterruptedException {
        LoginManager lm = getLoginManager();
        lm.addUser("a", "pass1");
        clearInvocations(loggerMock);

        lm.changePassword("a", "pass1", "pass2");

        verify(loggerMock).write("user changed passwords:a");
    }
}
