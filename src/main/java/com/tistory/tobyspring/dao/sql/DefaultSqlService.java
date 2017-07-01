package com.tistory.tobyspring.dao.sql;

public class DefaultSqlService extends BaseSqlService {

    public DefaultSqlService() {
        setSqlReader(new JaxbSqlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }
}
