package core.dao;

import core.jdbc.ConnectionManager;
import core.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoV2 {

    private final JdbcTemplate template;

    public UserDaoV2(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        template.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        template.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {

        String sql = "SELECT * FROM USERS";
        List<User> users = template.query(sql, new UserMapper());
        return users;
    }

    public User findByUserId(String userId) throws SQLException {

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        // queryForObject의 두 번째 인자로 SQL 파라미터를 배열 형태로 전달
        User user = template.queryForObject(sql, new Object[]{userId}, new UserMapper());
        return user;

    }

    /*
        RowMapper : 쿼리 결과를 Java 객체로 매핑 하는데 사용
     */
    private static final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));
        }
    }

}
