package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.domain.User;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest {

    private static UserDao dao;

    @BeforeClass
    public static void beforeClass_init() throws SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("context-datasource.xml");
        dao = context.getBean("userDao", UserDao.class);
        dao.createTable();
    }

    @Test
    public void test_addAndGet() throws SQLException {
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

    @Test
    public void test_count() throws SQLException {
        User user1 = new User("wonchul", "허원철", "1234");
        User user2 = new User("naeun", "전나은", "1234");
        User user3 = new User("toby", "이일민", "1234");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void test_getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id"); // Incorrect result size: expected 1, actual 0
    }
}