package com.tistory.tobyspring.aop.pointcut.test;

public interface TargetInterface {

    void hello();
    void hello(String message);
    int minus(int a, int b) throws RuntimeException;
    int plus(int a, int b);
}
