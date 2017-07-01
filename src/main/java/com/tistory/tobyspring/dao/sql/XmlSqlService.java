package com.tistory.tobyspring.dao.sql;

import com.tistory.tobyspring.dao.xml.jaxb.SqlType;
import com.tistory.tobyspring.dao.xml.jaxb.Sqlmap;
import org.h2.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {

    private Map<String, String> sqlMap = new HashMap<>();

    private String sqlmapFile;

    private SqlRegistry sqlRegistry;

    private SqlReader sqlReader;

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    @PostConstruct // 빈의 초기화 메소드로 지정
    public void loadSql() {
        sqlReader.read(sqlRegistry);
    }

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
    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);

            Unmarshaller unmarshaller = context.createUnmarshaller();

            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
                    getClass().getResource(sqlmapFile) // /sqlmap/sqlmap.xml
            );

            for (SqlType sql : sqlmap.getSql()) {
                sqlRegistry.registerSql(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getSql(String key) {
        return sqlRegistry.findSql(key);
    }
}
