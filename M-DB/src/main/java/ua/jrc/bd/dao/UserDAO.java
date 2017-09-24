package ua.jrc.bd.dao;

import ua.jrc.bd.entity.User;

import java.util.List;

public interface UserDAO {

    void insert(User user);

    void update(int id, String name, String pass);

    void delete(User user);

    User getUserById(int id);

    List<User> getUserListByName(String name);

    List<User> getAllUserList();
}
