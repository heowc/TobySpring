package com.tistory.tobyspring.config;

import com.tistory.tobyspring.dao.UserDao;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

/*
    스프링 IoC (빈 팩토리)
 */
//@Configuration
public class DaoFactory {

    /*
        빈 객체(메소드명으로 등록)
     */
//    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    /*
        spring-jdbc 의 DataSource
     */
//    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl("jdbc:h2:mem:tobyspring;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setUsername("test");
        dataSource.setPassword("test");

        return dataSource;
    }
}