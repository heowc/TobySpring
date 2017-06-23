package com.tistory.tobyspring.reflection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class UppercaseAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String ret = (String) methodInvocation.proceed();   // 타겟 오브젝트 전달 필요 없음
                                                            // MethodInvocation에 메소드 정보와 타켓 정보 알고 있음
        return ret.toUpperCase();
    }
}
