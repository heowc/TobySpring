package com.tistory.tobyspring.context;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CreateApplicationContextTest {

    private StaticApplicationContext context;
    /*
     StaticApplicationContext
     => 개발을 하면서 테스트용도 이외에는 쓸 일이 없다. ( 스프링을 이용한 자체 프레임워크 개발에는 사용? )

     GenericApplicationContext
     => 가장 일반적인 애플리케이션 컨텍스트 구현이다. xml 등록 가능 ( GenericXmlApplicationContext 를 더 많이 사용 )

     WebApplicationContext
     => 가장 많이 쓰는 애플리케이션 컨텍스트 구현이다. ( XmlWebApplicationContext 를 더 많이 사용 )

       ※ 웹은 자바 애플리케이션의 main() 메소드가 없기 때문에 서블릿을 만들어 질 때 컨텍스트를 생성해둔 다음
          요청이 서블릿으로 들어올 때마다 getBean()으로 필요한 빈을 가져와 정해진 메소드를 실행해준다.
          서블릿에서 DispatcherServlet 이라는 것을 제공하여 가능하다. (web.xml에 등록)

      */



    @Before
    public void before_setup() {
        context = new StaticApplicationContext(); // IoC 컨테이너
        context.registerPrototype("hello1", Hello.class);
    }

    @Test
    public void test_getBeanByHello1() {
        Hello hello1 = context.getBean("hello1", Hello.class);
        assertThat(hello1, is(notNullValue()));
    }

    @Test
    public void test_registerBeanByHello() {
        BeanDefinition helloDef = new RootBeanDefinition(Hello.class); // 빈 메타정보를 담을 오브젝트 만듬
        helloDef.getPropertyValues()
                .addPropertyValue("name", "Spring"); // 프로퍼티에 들어갈 값을 지정
        context.registerBeanDefinition("hello2", helloDef); // hello2 라는 빈을 등록

        Hello hello1 = context.getBean("hello1", Hello.class);
        Hello hello2 = context.getBean("hello2", Hello.class);
        assertThat(hello2.sayHello(), is("Hello Spring"));

        assertThat(hello1, is(not(hello2)));

        assertThat(context.getBeanFactory().getBeanDefinitionCount(), is(2));
    }

    @Test
    public void test_registerBeanWithDependency() {
        context.registerBeanDefinition("printer",
                 new RootBeanDefinition(StringPrinter.class)); // printer 라는 빈 등록

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        helloDef.getPropertyValues().addPropertyValue("printer",
                new RuntimeBeanReference("printer")); // printer 이라는 빈을 프로퍼티로 등록

        context.registerBeanDefinition("hello", helloDef);

        Hello hello = context.getBean("hello", Hello.class);
        hello.print();

        assertThat(context.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void test_simpleBeanScanning() {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(
                        "com.tistory.tobyspring.context"
                );

        AnnotatedHello hello = ctx.getBean("annotatedHello", AnnotatedHello.class);

        assertThat(hello, is(notNullValue()));
    }
}
