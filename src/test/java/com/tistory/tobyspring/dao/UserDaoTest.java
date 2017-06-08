package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.dao.connection.ConnectionMaker;
import com.tistory.tobyspring.dao.connection.NConnectionMaker;
import com.tistory.tobyspring.domain.User;

import java.sql.SQLException;

public class UserDaoTest {

    public static void main(String [] args) throws SQLException, ClassNotFoundException {
        // 전략 패턴
        ConnectionMaker connectionMaker = new NConnectionMaker();
        UserDao dao = new UserDao(connectionMaker);
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
