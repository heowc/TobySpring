package com.tistory.tobyspring.service;

import com.tistory.tobyspring.domain.User;

public interface UserService {

    void upgradeLevels();
    void add(User user);
}
