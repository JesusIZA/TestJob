package ua.jrc.bd.impldao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.jrc.bd.dao.UserDAO;
import ua.jrc.bd.entity.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("H2DAO")
public class H2DAO implements UserDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(User user) {
        String sql = "insert into users (id, user_name, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{user.getId(), user.getName(), user.getPassword()});
    }

    @Override
    public void update(int id, String name, String pass) {
        String sql = "update users set user_name = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{name, pass, id});
    }

    public void insert(List<User> userList) {
        for (User user : userList) {
            insert(user);
        }
    }

    @Override
    public void delete(User user) {
        delete(user.getId());
    }

    public void delete(int id) {
        String sql = "delete from users where id=?";
        int result = jdbcTemplate.update(sql, id);
    }

    @Override
    public User getUserById(int id) {
        String sql = "select * from users where id=?";
        return jdbcTemplate.queryForObject(sql, new Object[] {id}, new UserRowMapper());
    }

    @Override
    public List<User> getUserListByName(String name) {
        String sql = "select * from users where user_name like ?";
        return jdbcTemplate.query(sql, new Object[] {name}, new UserRowMapper());
    }

    @Override
    public List<User> getAllUserList() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    private static final class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("user_name"));
            user.setPassword(rs.getString("password"));
            return user;
        }

    }
}
