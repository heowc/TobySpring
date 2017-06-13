package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.domain.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest {

    @Test
    public void test_addAndGet() throws SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("context-datasource.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);
        dao.createTable();

        /* ============================================================================ */

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        User user = new User();
        user.setId("wonchul");
        user.setName("허원철");
        user.setPassword("1234");

        dao.add(user);
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user.getId());

        assertThat(user.getName(), is(user2.getName()));
        assertThat(user.getPassword(), is(user2.getPassword()));
    }
}