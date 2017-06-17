package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.domain.User;
import com.tistory.tobyspring.exception.DuplicateUserIdException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO (Data Access Object) <BR>
 * 사용자 정보를 DB에 넣고 관리할 수 있는 클래스 <BR>
 */
public class UserDaoJdbc implements UserDao{

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper =
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int i) throws SQLException {
                        return new User(rs.getString("id"),
                                        rs.getString("name"),
                                        rs.getString("password"));
                    }
                };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(final User user) throws DuplicateUserIdException {
        try {
            jdbcTemplate
                    .update("INSERT INTO USERS (id, name, password) VALUES (?, ?, ?)",
                            user.getId(), user.getName(), user.getPassword());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DuplicateUserIdException(e);
        }
    }

    @Override
    public User get(String id) {
        return jdbcTemplate
                .queryForObject("SELECT * FROM USERS WHERE ID = ?",
                                new Object[] { id },
                                this.userMapper);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate
                .query("SELECT * FROM USERS",
                        this.userMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate
            .execute("DELETE FROM USERS");
    }

    @Override
    public int getCount() {
        return jdbcTemplate
                .queryForInt("SELECT COUNT(*) FROM USERS");
    }

    @Override
    public void createTable() {
        jdbcTemplate
            .execute("CREATE TABLE IF NOT EXISTS USERS ( " +
                        "ID VARCHAR(10) PRIMARY KEY, " +
                        "NAME VARCHAR(20) NOT NULL, " +
                        "PASSWORD VARCHAR(10) NOT NULL " +
                        ")");
    }
}
