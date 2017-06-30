package com.tistory.tobyspring.dao.sql;

import com.tistory.tobyspring.dao.xml.jaxb.SqlType;
import com.tistory.tobyspring.dao.xml.jaxb.Sqlmap;
import org.h2.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService {

    private Map<String, String> sqlMap = new HashMap<>();

    public XmlSqlService() {
        String contextPath = Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);

            Unmarshaller unmarshaller = context.createUnmarshaller();

            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
                    getClass().getResource("/sqlmap/sqlmap.xml")
            );

            for (SqlType sql : sqlmap.getSql()) {
                sqlMap.put(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String key) {
        String sql = sqlMap.get(key);

        if(StringUtils.isNullOrEmpty(sql)) {
            throw new IllegalArgumentException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }

        return sql;
    }
}
