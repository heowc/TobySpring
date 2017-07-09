package com.tistory.tobyspring.dao.sql.registry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractSqlRegistryTest {

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}

