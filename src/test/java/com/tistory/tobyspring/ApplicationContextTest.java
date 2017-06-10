package com.tistory.tobyspring;

import com.tistory.tobyspring.config.DaoFactory;
import com.tistory.tobyspring.dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class ApplicationContextTest {

    public static void main(String [] args) {
        // userDao1 != userDao2
        DaoFactory factory = new DaoFactory();
        UserDao userDao1 = factory.userDao();
        UserDao userDao2 = factory.userDao();

        System.out.println(userDao1);
        System.out.println(userDao2);

        // userDao3 == userDao4
        ApplicationContext context = new GenericXmlApplicationContext("context-datasource.xml");
        UserDao userDao3 = context.getBean("userDao", UserDao.class);
        UserDao userDao4 = context.getBean("userDao", UserDao.class);

        System.out.println(userDao3);
        System.out.println(userDao4);
    }
}
