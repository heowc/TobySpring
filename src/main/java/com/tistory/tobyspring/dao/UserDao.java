package com.tistory.tobyspring.dao;

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

    /*
        관계설정 책임의 분리 (높은 응집도, 낮은 결합도)
     */
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "INSERT INTO USERS(id, name, password) VALUES (?,?,?)"
        );
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
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
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("DELETE FROM USERS");

        ps.executeUpdate();

        ps.close();
        c.close();
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
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "CREATE TABLE USERS ( " +
                        "ID VARCHAR(10) PRIMARY KEY, " +
                        "NAME VARCHAR(20) NOT NULL, " +
                        "PASSWORD VARCHAR(10) NOT NULL " +
                        ")"
        );

        ps.executeUpdate();

        ps.close();
        c.close();
    }
}
