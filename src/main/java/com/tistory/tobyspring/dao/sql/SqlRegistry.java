package com.tistory.tobyspring.dao.sql;

public interface SqlRegistry {
    void registerSql(String key, String sql);

    String findSql(String key);
}
