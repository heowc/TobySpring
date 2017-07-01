package com.tistory.tobyspring.dao.sql;

import com.tistory.tobyspring.dao.xml.jaxb.SqlType;
import com.tistory.tobyspring.dao.xml.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class JaxbSqlReader implements SqlReader {

    private static final String DEFAULT_SQLMAP_FILE = "/sqlmap/sqlmap.xml";

    private String sqlmapFile = DEFAULT_SQLMAP_FILE;

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
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
}
