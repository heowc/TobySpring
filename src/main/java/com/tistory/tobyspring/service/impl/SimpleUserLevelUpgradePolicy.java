package com.tistory.tobyspring.service.impl;

import com.tistory.tobyspring.dao.UserDao;
import com.tistory.tobyspring.domain.Level;
import com.tistory.tobyspring.domain.User;
import com.tistory.tobyspring.service.UserLevelUpgradePolicy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service <BR>
 * 일반적인 유지 레벨 업그레이드 정책 서비스 클래스 <BR>
 */
public class SimpleUserLevelUpgradePolicy implements UserLevelUpgradePolicy {

    /** 실버 등급이 되기 위한 최소 조건 */
    public static final int MIN_LOGINCOUNT_FOR_SLIVER = 50;
    /** 골드 등급이 되기 위한 최소 조건 */
    public static final int MIN_RECOMMENDCOUNT_FOR_GOLD = 30;

    @Autowired
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean isUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC:  return (user.getLoginCount() >= MIN_LOGINCOUNT_FOR_SLIVER);
            case SILVER: return (user.getRecommendCount() >= MIN_RECOMMENDCOUNT_FOR_GOLD);
            case GOLD:   return false;
            default:     throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }
}
