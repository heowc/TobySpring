package com.tistory.tobyspring.service.test;

import com.tistory.tobyspring.domain.User;
import com.tistory.tobyspring.service.impl.SimpleUserService;

/**
 * Service <BR>
 * Test 유저 비즈니스 로직 처리 클래스 <BR>
 */
public class TestUserService extends SimpleUserService {

    @Override
    public void upgradeLevels() {
        super.upgradeLevels();
    }

    @Override
    public void add(User user) {
        super.add(user);
    }
}