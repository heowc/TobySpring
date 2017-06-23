package com.tistory.tobyspring.factorybean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-context-datasource.xml")
public class FactoryBeanTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void test_getMessageFromFactoryBean() {
        Object message = context.getBean("message"); // 제네릭 타입 객체 반환
        assertThat(message instanceof Message, is(true));
        assertThat(((Message)message).getText(), is("Factory Bean") );
    }

    @Test
    public void test_getMessageFactoryBean() {
        Object message = context.getBean("&message"); // 해당 Bean 객체 반환
        assertThat(message instanceof MessageFactoryBean, is(true));
    }
}
