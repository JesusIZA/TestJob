package ua.jrc.bd.entity;

import java.io.Serializable;

/**
 * Created by Jesus on 21.09.2017.
 */

public class User implements Serializable {

    private String login;

    private String name;

    private String password;

    public User() {}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "Login=" + login +
                ", Name='" + name + '\'' +
                ", Password='" + password + '\'' +
                '}';
    }
}
