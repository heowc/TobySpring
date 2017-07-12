package com.tistory.tobyspring.service.test;

import com.tistory.tobyspring.domain.User;
import com.tistory.tobyspring.exception.TestUserLevelUpgradePolicyException;
import com.tistory.tobyspring.service.UserLevelUpgradePolicy;
import com.tistory.tobyspring.service.impl.SimpleUserLevelUpgradePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestUserLevelUpgradePolicy extends SimpleUserLevelUpgradePolicy {

    private final String id = "madnite1";

    @Override
    public boolean isUpgradeLevel(User user) {
        return super.isUpgradeLevel(user);
    }

    @Override
    public void upgradeLevel(User user) {
        if(id.equals(user.getId())) {
            throw new TestUserLevelUpgradePolicyException();
        }
        super.upgradeLevel(user);
    }
}
