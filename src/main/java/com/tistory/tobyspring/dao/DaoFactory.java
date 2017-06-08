package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.dao.connection.ConnectionMaker;
import com.tistory.tobyspring.dao.connection.NConnectionMaker;

/*
    제어의 역전
 */
public class DaoFactory {

    public UserDao userDao() {
        return new UserDao(getConnectionMaker());
    }

    private ConnectionMaker getConnectionMaker() {
        return new NConnectionMaker();
    }
}
