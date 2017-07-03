package com.tistory.tobyspring.dao.sql;

import com.tistory.tobyspring.dao.sql.reader.JaxbSqlReader;
import com.tistory.tobyspring.dao.sql.registry.HashMapSqlRegistry;

public class DefaultSqlService extends BaseSqlService {

    public DefaultSqlService() {
        setSqlReader(new JaxbSqlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }
}
