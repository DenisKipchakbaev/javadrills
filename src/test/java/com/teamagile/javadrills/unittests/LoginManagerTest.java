package com.teamagile.javadrills.unittests;

import com.teamagile.javadrills.LoginManager;
import com.teamagile.javadrills.SlowLogger;
import com.teamagile.javadrills.SlowWebService;
import com.teamagile.javadrills.TraceMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

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
        LoginManager loginManagerSpy = spy(lm);
        doReturn(getFakeDateTimeFormatted()).when(loginManagerSpy).getCurrentDateTime();

        loginManagerSpy.addUser("a", "pass");

        ArgumentCaptor<TraceMessage> traceMessageCaptor = ArgumentCaptor.forClass(TraceMessage.class);
        verify(loggerMock).write(traceMessageCaptor.capture());
        TraceMessage traceMessage = traceMessageCaptor.getValue();
        assertEquals("1970-01-01T00:00:00 - logon by user 'a' and password 'pass'", traceMessage.getText());
        assertEquals(100, traceMessage.getSeverity());
    }

    private String getFakeDateTimeFormatted() {
        return LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0)
                .format(DateTimeFormatter.ISO_DATE_TIME);
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

    @Test
    public void changePassword_existingUserAndWrongPassword_logError() throws InterruptedException {
        LoginManager lm = getLoginManager();
        lm.addUser("a", "pass1");
        clearInvocations(loggerMock);

        lm.changePassword("a", "badpass", "pass2");

        ArgumentCaptor<TraceMessage> traceMessageCaptor = ArgumentCaptor.forClass(TraceMessage.class);
        verify(loggerMock).write(traceMessageCaptor.capture());
        TraceMessage traceMessage = traceMessageCaptor.getValue();
        assertEquals("password not changed", traceMessage.getText());
    }
}
