package com.tistory.tobyspring.dao.sql.registry;

import org.h2.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {

    private Map<String, String> sqlMap = new ConcurrentHashMap<>();

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

    @Override
    public void updateSql(String key, String sql) {
        if(sqlMap.get(key) == null) {
            throw new IllegalArgumentException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }

        sqlMap.replace(key, sql);
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) {
        for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
            updateSql(entry.getKey(), entry.getValue());
        }
    }
}
