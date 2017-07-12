package com.tistory.tobyspring.config;

import com.tistory.tobyspring.factorybean.Message;
import com.tistory.tobyspring.factorybean.MessageFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
//@ImportResource("/test-datasource-context.xml") // 해당 xml에서 빈을 가져올 수 있음
@Profile("test")
public class TestApplicationContext {

    @Bean
    public FactoryBean<Message> message() {
        MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
        messageFactoryBean.setText("Factory Bean");
        return messageFactoryBean;
    }
}
