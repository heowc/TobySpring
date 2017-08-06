package com.tistory.tobyspring.service.test;

import com.tistory.tobyspring.domain.User;
import com.tistory.tobyspring.service.impl.SimpleUserService;

import java.util.List;

/**
 * Service <BR>
 * Test 유저 비즈니스 로직 처리 클래스 <BR>
 */
//@Service
//@Profile("test")
public class TestUserService extends SimpleUserService {

    @Override
    public List<User> getAll() {
        for (User user : super.getAll()) {
            update(user);
        }
        return null;
    }
}