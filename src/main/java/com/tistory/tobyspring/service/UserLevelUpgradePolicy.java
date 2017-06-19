package com.tistory.tobyspring.service;

import com.tistory.tobyspring.domain.User;

public interface UserLevelUpgradePolicy {

    boolean isUpgradeLevel(User user);
    void upgradeLevel(User user);
}
