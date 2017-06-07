package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.domain.User;

import java.sql.*;

/**
 * DAO (Data Access Object)
 * 사용자 정보를 DB에 넣고 관리할 수 있는 클래스
 */
public class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

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

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM USERS WHERE ID = ?"
        );
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

    public void createTable() throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

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

    /*
        관심사의 분리, 메소드 추출
     */
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
                                                    "test",
                                                    "test");
        return c;
    }
}
