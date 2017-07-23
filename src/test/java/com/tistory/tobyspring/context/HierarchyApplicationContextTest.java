package com.tistory.tobyspring.context;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HierarchyApplicationContextTest {

    /*
        ApplicationContext는 계층 구조를 갖을 수 있다.
        해당 컨텍스트에서 원하는 빈을 발견하지 못하면, 자신의 부모 컨텍스트에서 빈을 찾는다.
     */

    private StaticApplicationContext parentContext;
    private StaticApplicationContext childContext;

    @Before
    public void before_setup() {
        parentContext = new StaticApplicationContext();
        childContext = new StaticApplicationContext(parentContext);

        setupParent();
        setupChild();
    }

    private void setupParent() {
        parentContext.registerBeanDefinition("printer",
                                            new RootBeanDefinition(StringPrinter.class));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Parent");
        helloDef.getPropertyValues().addPropertyValue("printer",
                                                        new RuntimeBeanReference("printer"));
        parentContext.registerBeanDefinition("hello", helloDef);
    }

    private void setupChild() {
        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Child");
        helloDef.getPropertyValues().addPropertyValue("printer",
                                                        new RuntimeBeanReference("printer"));
        childContext.registerBeanDefinition("hello", helloDef);
    }

    @Test
    public void test_parent() {
        Hello hello = parentContext.getBean("hello", Hello.class);
        hello.print();

        assertThat(hello.sayHello(), is("Hello Parent"));
    }

    @Test
    public void test_child() {
        Hello hello = childContext.getBean("hello", Hello.class);
        hello.print();

        assertThat(hello.sayHello(), is("Hello Child"));
    }

}
