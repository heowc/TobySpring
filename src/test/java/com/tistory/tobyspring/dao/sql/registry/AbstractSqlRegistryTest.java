package com.tistory.tobyspring.dao.sql.registry;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public abstract class AbstractSqlRegistryTest {

    UpdatableSqlRegistry sqlRegistry;

    @Before
    public void before_setUp() {
        sqlRegistry = createUpdatableSqlRegistry();

        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

    @Test
    public void test_find() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    private void checkFindResult(String expected1, String expected2, String expected3) {
        assertThat(sqlRegistry.findSql("KEY1"), is(expected1));
        assertThat(sqlRegistry.findSql("KEY2"), is(expected2));
        assertThat(sqlRegistry.findSql("KEY3"), is(expected3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_unknownKey() {
        sqlRegistry.findSql("unknown_key");
    }

    @Test
    public void test_updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2");
        checkFindResult("SQL1", "Modified2", "SQL3");
    }

    @Test
    public void test_updateMulti() {
        Map<String, String> sqlmap = new HashMap();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY3", "Modified3");

        sqlRegistry.updateSql(sqlmap);
        checkFindResult("Modified1", "SQL2", "Modified3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_updateWithNotExisitingKey() {
        sqlRegistry.updateSql("unknown_key", "unknown_sql");
    }
}