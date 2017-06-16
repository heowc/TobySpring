package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.domain.User;
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
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public void add(final User user) throws SQLException {
        jdbcTemplate.update("INSERT INTO USERS (id, name, password) VALUES (?, ?, ?)",
                            user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {
        return jdbcTemplate
                .queryForObject("SELECT * FROM USERS WHERE ID = ?",
                                new Object[] {id},
                                new RowMapper<User>() {
                                    @Override
                                    public User mapRow(ResultSet rs, int i) throws SQLException {
                                        return new User(rs.getString("id"),
                                                        rs.getString("name"),
                                                        rs.getString("password"));
                                    }
                                });
    }

    public List<User> getAll() throws SQLException {
        return jdbcTemplate
                .query("SELECT * FROM USERS",
                        new RowMapper<User>() {
                            @Override
                            public User mapRow(ResultSet rs, int i) throws SQLException {
                                return new User(rs.getString("id"),
                                        rs.getString("name"),
                                        rs.getString("password"));
                            }
                        });
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.execute("DELETE FROM USERS");
    }

    public int getCount() throws SQLException {
        return jdbcTemplate
                .queryForInt("SELECT COUNT(*) FROM USERS");
    }

    public void createTable() throws SQLException {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS USERS ( " +
                                "ID VARCHAR(10) PRIMARY KEY, " +
                                "NAME VARCHAR(20) NOT NULL, " +
                                "PASSWORD VARCHAR(10) NOT NULL " +
                                ")");
    }
}
