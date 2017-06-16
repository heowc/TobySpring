package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.dao.jdbc.JdbcContext;
import com.tistory.tobyspring.dao.statement.StatementStrategy;
import com.tistory.tobyspring.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO (Data Access Object) <BR>
 * 사용자 정보를 DB에 넣고 관리할 수 있는 클래스 <BR>
 */
public class UserDao {

    private DataSource dataSource;

    private JdbcContext jdbcContext;

    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
        전략 패턴과 템플릿 패턴을 섞은 방식을 템플릿/콜백 패턴이라고 한다.
     */
    private void executeSql(final String query) throws SQLException {
        jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement(query);
            }
        });
    }

    private void executeSql(final String query, final String... value) throws SQLException {
        jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement(query);

                int length = value.length;
                for (int i = 0; i < length; i++) {
                    ps.setString((i + 1), value[i]);
                }

                return ps;
            }
        });
    }

    public void add(final User user) throws SQLException {
        executeSql("INSERT INTO USERS(id, name, password) VALUES (?, ?, ?)",
                    user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM USERS WHERE ID = ?"
        );
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        User user = null;

        if( rs.next() ) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if (user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

    public void deleteAll() throws SQLException {
        executeSql("DELETE FROM USERS");
    }

    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM USERS");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }

    public void createTable() throws SQLException {
        executeSql("CREATE TABLE IF NOT EXISTS USERS ( " +
                    "ID VARCHAR(10) PRIMARY KEY, " +
                    "NAME VARCHAR(20) NOT NULL, " +
                    "PASSWORD VARCHAR(10) NOT NULL " +
                    ")");
    }
}
