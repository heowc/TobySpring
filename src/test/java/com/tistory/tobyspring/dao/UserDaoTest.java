package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {

    public static void main(String [] args) throws SQLException, ClassNotFoundException {
        /*
            1. 어노테이션 방식
         */
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        /*
            2. xml 방식
         */
        ApplicationContext context = new GenericXmlApplicationContext("context-datasource.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);
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