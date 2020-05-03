package com.teamagile.javadrills;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
            TraceMessage traceMessage = new TraceMessage(
                    String.format("%s - logon by user '%s' and password '%s'", getCurrentDateTime(), user, pass),
                    100);
            logger.write(traceMessage);
        } catch (RuntimeException e) {
            webService.notify("the error from the logger");
        }
    }

    public String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
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

    public void changePassword(final String user, final String currentPassword, String newPassword) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.username.equals(user) && u.password.equals(currentPassword))
                .findAny();
        if (userOpt.isPresent()) {
            logger.write("user changed passwords:" + user);
        } else {
            logger.write(new TraceMessage("password not changed", 0));
        }
    }
}
