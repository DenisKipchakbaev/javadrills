package com.teamagile.javadrills;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LoginManager {

    private final Logger logger;
    private final SlowWebService webService;
    private List<User> users = new ArrayList<User>();

    public LoginManager(Logger logger, SlowWebService webService) {
        this.logger = logger;
        this.webService = webService;
    }

    public void addUser(String user, String pass) throws InterruptedException {
        users.add(new User(user, pass));
        try {
            TraceMessage traceMessage = new TraceMessage();
            traceMessage.setText(String.format("logon by user '%s' and password '%s'", user, pass));
            traceMessage.setSeverity(100);
            logger.write(traceMessage);
        } catch (RuntimeException e) {
            webService.notify("the error from the logger");
        }
    }

    public Boolean isLoginOK(String user, String pass) {
        for (Iterator<User> i = users.iterator(); i.hasNext(); ) {
            User item = i.next();
            if (item.username == user && item.password == pass) {
                return true;
            }

        }
        return false;
    }

    public void changePassword(String user, String currentPassword, String newPassword) {
        logger.write("user changed passwords:" + user);
    }
}
