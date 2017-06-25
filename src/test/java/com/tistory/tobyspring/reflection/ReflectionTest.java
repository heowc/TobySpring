package com.tistory.tobyspring.reflection;

import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReflectionTest {

    @Test
    public void test_invokeMethod() throws Exception {
        String name = "Spring";

        assertThat(name.length(), is(6));

        Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer)lengthMethod.invoke(name), is(6));


        assertThat(name.charAt(0), is('S'));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character)charAtMethod.invoke(name, 0), is('S'));
    }

    @Test
    public void test_simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("WonChul"), is("Hello WonChul"));
        assertThat(hello.sayHi("WonChul"), is("Hi WonChul"));
        assertThat(hello.sayThankYou("WonChul"), is("Thank You WonChul"));
    }

    @Test
    public void test_uppercaseProxy() {
        Hello hello = new HelloUppercase(new HelloTarget());
        assertThat(hello.sayHello("WonChul"), is("HELLO WONCHUL"));
        assertThat(hello.sayHi("WonChul"), is("HI WONCHUL"));
        assertThat(hello.sayThankYou("WonChul"), is("THANK YOU WONCHUL"));
    }

    @Test
    public void test_uppercaseDynamicProxy() {
        Hello hello = (Hello) Proxy.newProxyInstance(
                                        getClass().getClassLoader(), // 클래스 로더 제공
                                        new Class[] { Hello.class }, // 구현할 인터페이스
                                        new UppercaseHandler(new HelloTarget()) // 부가 기능과 위임해줄 오브젝트
                                    );

        assertThat(hello.sayHello("WonChul"), is("HELLO WONCHUL"));
        assertThat(hello.sayHi("WonChul"), is("HI WONCHUL"));
        assertThat(hello.sayThankYou("WonChul"), is("THANK YOU WONCHUL"));
    }

    /**
     * Spring - Proxy Factory Bean </br>
     * <pre>
     * * Advice : 부가 기능을 제공하는 오브젝트
     * * Pointcut : 메소드 선정 알고리즘
     *
     * ProxyFactoryBean(생성) -> 다이나믹 프록시
     *                       -> 포인트컷(기능부가 대상 확인)
     *                       -> 어드바이스(선정된 메소드 요청)
     *                       -> 콜백
     *                       -> 타킷오브젝트(위임)
     * </pre>
     */
    @Test
    public void test_springProxyFactoryBean() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget()); // 타겟 설정
        proxyFactoryBean.addAdvice(new UppercaseAdvice()); // 부가 기능을 할 어드바이스 추가 (여러개 가능)

        Hello hello = (Hello) proxyFactoryBean.getObject(); // 생성된 프록스 초기화

        assertThat(hello.sayHello("WonChul"), is("HELLO WONCHUL"));
        assertThat(hello.sayHi("WonChul"), is("HI WONCHUL"));
        assertThat(hello.sayThankYou("WonChul"), is("THANK YOU WONCHUL"));
    }

    /**
     * 어드바이저 = 포인트컷 + 어드바이서
     */
    @Test
    public void test_springProxyFactoryBeanWithAdvisor() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget()); // 타겟 설정

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut(); // 메소드명 포인트컷 설정 객체 초기화
        pointcut.setMappedName("sayH*"); // sayH로 시작하는 메소드명

        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice())); // 어드바이저 설정

        Hello hello = (Hello) proxyFactoryBean.getObject(); // 생성된 프록스 초기화

        assertThat(hello.sayHello("WonChul"), is("HELLO WONCHUL"));
        assertThat(hello.sayHi("WonChul"), is("HI WONCHUL"));
        assertThat(hello.sayThankYou("WonChul"), is("Thank You WonChul")); // toUppercase() 안됨
    }

    @Test
    public void test_advisorAndClassNamePointcut() {
        // 포인트컷 준비
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloW");
                    }
                };
            }
        };
        classMethodPointcut.setMappedName("sayH*");

        checkAdviced(new HelloTarget(), classMethodPointcut, false);

        class HelloWorld extends HelloTarget {}
        checkAdviced(new HelloWorld(), classMethodPointcut, true);

        class HelloWonchul extends HelloTarget {}
        checkAdviced(new HelloWonchul(), classMethodPointcut, true);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean isAdviced) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(target); // 타겟 설정
        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice())); // 어드바이저 설정
        Hello hello = (Hello) proxyFactoryBean.getObject(); // 생성된 프록스 초기화

        if (isAdviced) {
            assertThat(hello.sayHello("WonChul"), is("HELLO WONCHUL"));
            assertThat(hello.sayHi("WonChul"), is("HI WONCHUL"));
            assertThat(hello.sayThankYou("WonChul"), is("Thank You WonChul"));
        } else {
            assertThat(hello.sayHello("WonChul"), is("Hello WonChul"));
            assertThat(hello.sayHi("WonChul"), is("Hi WonChul"));
            assertThat(hello.sayThankYou("WonChul"), is("Thank You WonChul"));
        }
    }
}
