package com.tistory.tobyspring.dao.sql;

import com.tistory.tobyspring.dao.xml.jaxb.SqlType;
import com.tistory.tobyspring.dao.xml.jaxb.Sqlmap;
import org.springframework.oxm.Unmarshaller;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

/**
 * OXM 서비스 추상화 </br>
 *
 */
public class OxmSqlService implements SqlService {

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmapFile(String sqlmapFile) {
        oxmSqlReader.setSqlmapFile(sqlmapFile);
    }

    @PostConstruct
    public void loadSql() {
        oxmSqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) {
        return sqlRegistry.findSql(key);
    }

    private class OxmSqlReader implements SqlReader {

        private Unmarshaller unmarshaller;

        private String sqlmapFile;

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmapFile(String sqlmapFile) {
            this.sqlmapFile = sqlmapFile;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                Source xmlSource = new StreamSource(
                        getClass().getResource("/sqlmap/sqlmap.xml").openStream()
                );

                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

                for (SqlType sql : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(sqlmapFile + "에 대한 AFile을 찾을 수 없습니다.");
            }
        }
    }

}
