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

import static com.tistory.tobyspring.service.impl.SimpleUserLevelUpgradePolicy.MIN_LOGINCOUNT_FOR_SLIVER;
import static com.tistory.tobyspring.service.impl.SimpleUserLevelUpgradePolicy.MIN_RECOMMENDCOUNT_FOR_GOLD;
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
            new User("bunjin", "박범진", "p1", Level.BASIC, MIN_LOGINCOUNT_FOR_SLIVER-1, 8),
            new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGINCOUNT_FOR_SLIVER, 8),
            new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMENDCOUNT_FOR_GOLD-1),
            new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMENDCOUNT_FOR_GOLD),
            new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();

        for (User user : userList) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(userList.get(0), false);
        checkLevelUpgraded(userList.get(1), true);
        checkLevelUpgraded(userList.get(2), false);
        checkLevelUpgraded(userList.get(3), true);
        checkLevelUpgraded(userList.get(4), false);
    }

    private void checkLevelUpgraded(User user, boolean isUpgraded) {
        User userUpdate = userDao.get(user.getId());
        if (isUpgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = userList.get(4);       // GOLD LEVEL
        User userWithoutLevel = userList.get(0);
        userWithoutLevel.setLevel(null);            // NULL LEVEL

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }
}