package com.tistory.tobyspring;

import com.tistory.tobyspring.dao.UserDao;
import com.tistory.tobyspring.domain.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TobySpringApplication {

    public static void main(String [] args) throws SQLException, ClassNotFoundException {
        createUserTable();

        System.out.println("================================================");
        UserDao dao = new UserDao();
        User user = new User();
        user.setId("wonchul");
        user.setName("허원철");
        user.setPassword("1234");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }

    public static void createUserTable() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
                                                    "test",
                                                    "test");

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
