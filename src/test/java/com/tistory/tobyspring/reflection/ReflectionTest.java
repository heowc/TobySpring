package com.tistory.tobyspring.reflection;

import org.junit.Test;

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
}
