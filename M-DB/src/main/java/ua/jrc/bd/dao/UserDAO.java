package ua.jrc.bd.dao;

import ua.jrc.bd.entity.User;

import java.util.List;

public interface UserDAO {

    void insert(User user);

    void update(String login, String name, String password);

    void update(User user);

    void delete(User user);

    User getUserByLogin(String login);

    List<User> getUserListByName(String name);

    List<User> getUserListByLogin(String login);

    List<User> getAllUserList();
}
