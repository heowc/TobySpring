package com.tistory.tobyspring.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 다이나믹 프록스 <BR>
 *
 *
 */
public class UppercaseHandler implements InvocationHandler {

    Hello hello;

    public UppercaseHandler(Hello hello) {
        this.hello = hello;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String ret = (String)method.invoke(hello, args);
        return ret.toUpperCase();
    }
}
