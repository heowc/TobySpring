package com.tistory.tobyspring.dao.sql.registry;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Map;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {

    private SimpleJdbcTemplate template;
    private TransactionTemplate transactionTemplate;

    public void setDataSource(DataSource dataSource) {
        template = new SimpleJdbcTemplate(dataSource);
        transactionTemplate = new TransactionTemplate(
                new DataSourceTransactionManager(dataSource)
        );
    }

    @Override
    public void registerSql(String key, String sql) {
        template.update("INSERT INTO SQLMAP (KEY_, SQL_) VALUES (?, ?)", key, sql);
    }

    @Override
    public String findSql(String key) {
        try {
            return template.queryForObject("SELECT SQL_ FROM SQLMAP WHERE KEY_ = ?", String.class, key);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }
    }

    @Override
    public void updateSql(String key, String sql) {
        int updatedCount = template.update("UPDATE SQLMAP SET SQL_ = ? WHERE KEY_ = ?", sql, key);

        if(updatedCount == 0) {
            throw new IllegalArgumentException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                for(Map.Entry<String, String> entry : sqlmap.entrySet()) {
                    updateSql(entry.getKey(), entry.getValue());
                }
            }
        });
    }
}
