package com.tistory.tobyspring.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

public class EmbeddedDbTest {

    EmbeddedDatabase db;
    SimpleJdbcTemplate template;

    @Before
    public void before_setup() {
        db = new EmbeddedDatabaseBuilder()
                .setType(H2)
                .addScript("classpath:sql/sqlmap.sql")
                .addScript("classpath:sql/test-data.sql")
                .build();

        template = new SimpleJdbcTemplate(db);
    }

    @After
    public void after_shutdown() {
        db.shutdown();
    }

    @Test
    public void test_validInitData() {
        assertThat(template.queryForInt(
             "SELECT COUNT(*) FROM SQLMAP"
        ), is(2));

        List<Map<String, Object>> list = template.queryForList(
            "SELECT KEY_, SQL_ FROM SQLMAP ORDER BY KEY_"
        );

        assertThat(list.get(0).get("KEY_"), is("KEY1"));
        assertThat(list.get(0).get("SQL_"), is("SQL1"));
        assertThat(list.get(1).get("KEY_"), is("KEY2"));
        assertThat(list.get(1).get("SQL_"), is("SQL2"));
    }

    @Test
    public void test_insertData() {
        template.update("INSERT INTO SQLMAP (KEY_, SQL_) " +
                            "VALUES ('KEY3', 'SQL3');");

        assertThat(template.queryForInt(
                "SELECT COUNT(*) FROM SQLMAP"
        ), is(3));
    }
}
