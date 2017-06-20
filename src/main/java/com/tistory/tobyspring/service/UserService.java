package com.tistory.tobyspring.service;

import com.tistory.tobyspring.domain.User;

public interface UserService {

    void upgradeLevels() throws Exception;
    void add(User user);
}
