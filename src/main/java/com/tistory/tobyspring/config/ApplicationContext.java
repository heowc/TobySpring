package com.tistory.tobyspring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Driver;

/*
    - 2.5 부터 애노테이션으로 빈 관리 가능
    - 3.1 부터 실제 내부 소스에도 적용됨
    - 보다 직관적
    - <context:annotation-config/> 제거 가능. 빈 후처리 자동 등록.
 */

@Configuration
@EnableTransactionManagement // 3.1 부터 자주 사용 되는 전용 태그를 @Enable~ 대체 애노테이션 제공
@ComponentScan(basePackages = "com.tistory.tobyspring") // 기본적으로 프로젝트 내의 모든 클래스패스를 다 뒤지기 때문에
// 특정 패키지 안에서만 찾도록 기준이 되는 패키지를 지정
@Import({SqlServiceContext.class, ProductApplicationContext.class, TestApplicationContext.class})
@PropertySource(value = "classpath:/properties/database.properties") // 프로퍼티 접근
                                                                                    // @Value 와 Environment 방법 존재
public class ApplicationContext {

//    @Resource  : 빈의 아이디를 기준
//    @Autowired : 빈의 타입을 기준

    @Value("${datasource.driver-class}")
    private Class<? extends Driver> driverClass;

    @Value("${datasource.url}")
    private String url;

    @Value("${datasource.username}")
    private String username;

    @Value("${datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

    // @Value 사용 시, 필요한 bean 설정, static으로 해야한다.
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
