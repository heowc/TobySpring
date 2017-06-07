package com.tistory.tobyspring;

import com.tistory.tobyspring.dao.UserDao;
import com.tistory.tobyspring.domain.User;

import java.sql.SQLException;

public class TobySpringApplication {

    public static void main(String [] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new UserDao();
        dao.createTable();

        /* ============================================================================ */

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
}
