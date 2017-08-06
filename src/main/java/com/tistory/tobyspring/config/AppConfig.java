package com.tistory.tobyspring.config;

import com.tistory.tobyspring.context.Hello;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "com.tistory.tobyspring"
            , excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AppConfig.class)
)
public class AppConfig {

    @Bean
    public Hello hello() {
        return new Hello();
    }
}
