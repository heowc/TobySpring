package com.tistory.tobyspring.dao.sql.registry;

import org.h2.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class HashMapSqlRegistry implements SqlRegistry {

    private Map<String, String> sqlMap = new HashMap<>();

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) {
        String sql = sqlMap.get(key);

        if(StringUtils.isNullOrEmpty(sql)) {
            throw new IllegalArgumentException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }

        return sql;
    }
}
