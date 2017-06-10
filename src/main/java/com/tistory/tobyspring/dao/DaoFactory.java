package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.dao.connection.ConnectionMaker;
import com.tistory.tobyspring.dao.connection.NConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    스프링 IoC (빈 팩토리)
 */
@Configuration
public class DaoFactory {

    /*
        빈 객체(메소드명으로 등록)
     */
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new NConnectionMaker();
    }
}
