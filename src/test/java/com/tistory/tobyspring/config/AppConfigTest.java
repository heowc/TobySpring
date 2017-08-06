package com.tistory.tobyspring.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class AppConfigTest {

    @Autowired
    private ListableBeanFactory beanFactory;

    @Test
    public void test() throws Exception {
        for ( String beanName : beanFactory.getBeanDefinitionNames() ) {
            System.out.println(beanName);
        }
    }
}
