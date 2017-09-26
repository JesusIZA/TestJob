package ua.jrc.bd.impldao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.jrc.bd.dao.UserDAO;
import ua.jrc.bd.entity.User;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("H2DAO")
public class H2DAO implements UserDAO, Serializable{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(User user) {
        String sql = "insert into USERS (LOGIN, USER_NAME, PASSWORD) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{user.getLogin(), user.getName(), user.getPassword()});
    }

    @Override
    public void update(String login, String name, String pass) {
        String sql = "update USERS set  USER_NAME = ?, PASSWORD = ? WHERE LOGIN = ?";
        jdbcTemplate.update(sql, new Object[]{ name, pass, login});
    }

    @Override
    public void update(User user) {
        String sql = "update USERS set USER_NAME = ?, PASSWORD = ? WHERE LOGIN = ?";
        jdbcTemplate.update(sql, new Object[]{ user.getName(), user.getPassword(), user.getLogin()});
    }

    public void insert(List<User> userList) {
        for (User user : userList) {
            insert(user);
        }
    }

    @Override
    public void delete(User user) {
        delete(user.getLogin());
    }

    public void delete(String login) {
        String sql = "delete from USERS where LOGIN=?";
        int result = jdbcTemplate.update(sql, login);
    }

    @Override
    public User getUserByLogin(String login) {
        String sql = "select * from USERS where LOGIN=?";
        return jdbcTemplate.queryForObject(sql, new Object[] {login}, new UserRowMapper());
    }

    @Override
    public List<User> getUserListByName(String name) {
        String sql = "select * from USERS where USER_NAME like ?";
        return jdbcTemplate.query(sql, new Object[] {name}, new UserRowMapper());
    }

    @Override
    public List<User> getUserListByLogin(String login) {
        String sql = "select * from USERS where LOGIN like ?";
        return jdbcTemplate.query(sql, new Object[] {login}, new UserRowMapper());
    }

    @Override
    public List<User> getAllUserList() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    private static final class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setLogin(rs.getString("LOGIN"));
            user.setName(rs.getString("USER_NAME"));
            user.setPassword(rs.getString("PASSWORD"));
            return user;
        }

    }
}
