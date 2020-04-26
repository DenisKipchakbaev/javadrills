package com.teamagile.javadrills;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LoginManager {

    private final Logger logger;
    private List<User> users = new ArrayList<User>();

    public LoginManager(Logger logger) {
        this.logger = logger;
    }

    public void addUser(String user, String pass) {
        users.add(new User(user, pass));
        logger.write(String.format("logon by user '%s' and password '%s'", user, pass));
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
}
