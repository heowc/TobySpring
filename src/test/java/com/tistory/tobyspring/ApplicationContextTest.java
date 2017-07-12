package com.tistory.tobyspring;

import com.tistory.tobyspring.config.TestApplicationContext;
import com.tistory.tobyspring.dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationContext.class)
public class ApplicationContextTest {

    @Autowired ApplicationContext context;
    @Autowired UserDao userDao;

    @Before
    public void before_init() throws Exception {
        System.out.println(this);
        System.out.println(this.context);
        System.out.println(this.userDao);
    }

    @Test
    public void test_notNullContext() throws Exception {
        assertThat(this.context, is(notNullValue()));
    }

    @Test
    public void test_notNullDao() throws Exception {
        assertThat(this.userDao, is(notNullValue()));
    }

    @Test
    public void test_equalsDao() throws Exception {
        UserDao userDao = this.context.getBean("userDaoJdbc", UserDao.class);

        assertThat(this.userDao, is(userDao));
    }
}