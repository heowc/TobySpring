package com.tistory.tobyspring.dao.sql;

import com.tistory.tobyspring.dao.sql.reader.SqlReader;
import com.tistory.tobyspring.dao.sql.registry.SqlRegistry;

public class BaseSqlService implements SqlService {

    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

//    @PostConstruct
    public void loadSql() {
        sqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) {
        return sqlRegistry.findSql(key);
    }
}
