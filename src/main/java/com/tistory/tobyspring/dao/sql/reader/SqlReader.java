package com.tistory.tobyspring.dao.sql.reader;

import com.tistory.tobyspring.dao.sql.registry.SqlRegistry;

public interface SqlReader {

    void read(SqlRegistry sqlRegistry);
}
