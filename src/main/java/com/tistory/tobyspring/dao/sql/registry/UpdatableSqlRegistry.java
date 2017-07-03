package com.tistory.tobyspring.dao.sql.registry;

import java.util.Map;

// sql를 update 가능한 SqlRegistry
public interface UpdatableSqlRegistry extends SqlRegistry {

    void updateSql(String key, String sql);
    void updateSql(Map<String, String> sqlmap);
}
