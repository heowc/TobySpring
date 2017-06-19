package com.tistory.tobyspring.service.impl;

import com.tistory.tobyspring.dao.UserDao;
import com.tistory.tobyspring.domain.Level;
import com.tistory.tobyspring.domain.User;
import com.tistory.tobyspring.service.UserService;

import java.util.List;

/**
 * Service <BR>
 * 비즈니스 로직 처리 클래스 <BR>
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void upgradeLevels() {
        List<User> userList = userDao.getAll();

        for (User user : userList) {
            Boolean isChanged;

            if (user.isUpgradeBasic()) {
                user.setLevel(Level.SILVER);
                isChanged = true;
            }

            else if (user.isUpgradeSliver()) {
                user.setLevel(Level.GOLD);
                isChanged = true;
            }

            else if (user.isGold()) {
                isChanged = false;
            }

            else {
                isChanged = false;
            }

            if (isChanged) {
                userDao.update(user);
            }
        }
    }

    @Override
    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
