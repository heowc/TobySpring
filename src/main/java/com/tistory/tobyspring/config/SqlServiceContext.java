package com.tistory.tobyspring.config;

import com.tistory.tobyspring.dao.sql.OxmSqlService;
import com.tistory.tobyspring.dao.sql.SqlService;
import com.tistory.tobyspring.dao.sql.registry.EmbeddedDbSqlRegistry;
import com.tistory.tobyspring.dao.sql.registry.SqlRegistry;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.castor.CastorMarshaller;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

//@Configuration
public class SqlServiceContext {

//    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
        return sqlService;
    }

//    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

//    @Bean
    public Unmarshaller unmarshaller() {
        CastorMarshaller castorMarshaller = new CastorMarshaller();
        castorMarshaller.setMappingLocation(new ClassPathResource("sqlmap/mapping.xml"));
        return castorMarshaller;
    }

//    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(H2)
                .addScript("sql/sqlmap.sql")
//                .addScript("sql/test-data.sql")
                .build();
    }
}
