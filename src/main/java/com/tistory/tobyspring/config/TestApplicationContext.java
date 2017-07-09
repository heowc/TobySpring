package com.tistory.tobyspring.config;

import com.tistory.tobyspring.dao.UserDao;
import com.tistory.tobyspring.dao.UserDaoJdbc;
import com.tistory.tobyspring.dao.sql.OxmSqlService;
import com.tistory.tobyspring.dao.sql.SqlService;
import com.tistory.tobyspring.dao.sql.registry.EmbeddedDbSqlRegistry;
import com.tistory.tobyspring.dao.sql.registry.SqlRegistry;
import com.tistory.tobyspring.factorybean.Message;
import com.tistory.tobyspring.factorybean.MessageFactoryBean;
import com.tistory.tobyspring.service.UserLevelUpgradePolicy;
import com.tistory.tobyspring.service.UserService;
import com.tistory.tobyspring.service.test.DummyMailSender;
import com.tistory.tobyspring.service.test.TestUserLevelUpgradePolicy;
import com.tistory.tobyspring.service.test.TestUserService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

/*
    - 2.5 부터 애노테이션으로 빈 관리 가능
    - 3.1 부터 실제 내부 소스에도 적용됨
    - 보다 직관적
    - <context:annotation-config/> 제거 가능. 빈 후처리 자동 등록.
 */
@Configuration
//@ImportResource("/test-datasource-context.xml") // 해당 xml에서 빈을 가져올 수 있음
@EnableTransactionManagement // 3.1 부터 자주 사용 되는 전용 태그를 @Enable~ 대체 애노테이션 제공
public class TestApplicationContext {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setUsername("test");
        dataSource.setPassword("test");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

    @Bean
    public UserDao userDao() {
        UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
        userDaoJdbc.setDataSource(dataSource());
        userDaoJdbc.setSqlService(sqlService());

        return userDaoJdbc;
    }

    @Bean
    public UserService userService() {
        TestUserService userService = new TestUserService();
        userService.setUserDao(userDao());
        userService.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
        return userService;
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy() {
        TestUserLevelUpgradePolicy userLevelUpgradePolicy = new TestUserLevelUpgradePolicy("madnite1");
        userLevelUpgradePolicy.setUserDao(userDao());
        userLevelUpgradePolicy.setMailSender(mailSender());
        return userLevelUpgradePolicy;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public FactoryBean<Message> message() {
        MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
        messageFactoryBean.setText("Factory Bean");
        return messageFactoryBean;
    }

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
        return sqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        CastorMarshaller castorMarshaller = new CastorMarshaller();
        castorMarshaller.setMappingLocation(new ClassPathResource("sqlmap/mapping.xml"));
        return castorMarshaller;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(H2)
                .addScript("sql/sqlmap.sql")
//                .addScript("sql/test-data.sql")
                .build();
    }
}
