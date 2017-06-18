package com.tistory.tobyspring.service;

import com.tistory.tobyspring.dao.UserDao;
import com.tistory.tobyspring.domain.Level;
import com.tistory.tobyspring.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-context-datasource.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private List<User> userList;

    @Before
    public void before_init() {
        userDao.createTable();

        userList = Arrays.asList(
                new User("bunjin", "박범진", "p1", Level.BASIC, 49, 8),
                new User("joytouch", "강명성", "p2", Level.BASIC, 50, 8),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, 29),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30),
                new User("green", "오민규", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();

        for (User user : userList) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(userList.get(0), Level.BASIC);
        checkLevel(userList.get(1), Level.SILVER);
        checkLevel(userList.get(2), Level.SILVER);
        checkLevel(userList.get(3), Level.GOLD);
        checkLevel(userList.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level level) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(level));
    }
}
