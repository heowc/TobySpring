package com.tistory.tobyspring.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

/**
 * 트랜잭션 프록시 팩토리 빈 <BR>
 * <PRE>
 * 장점
 * 1. 재사용 가능
 * 2. 코드 중복 제거
 *
 * 단점
 * 1. 메소드 단위로만 가능
 * 2. 여러가지 프록시 추가 시, 복잡해짐
 * </PRE>
 */
public class TransactionProxyFactoryBean implements FactoryBean<Object> {

    private Object target;
    private PlatformTransactionManager transactionManager;
    private String pattern;
    private Class<?> serviceInterface;

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @Override
    public Object getObject() throws Exception {
        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(target);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern("upgradeLevels");

        return Proxy.newProxyInstance(
                        getClass().getClassLoader(),
                        new Class[] { serviceInterface },
                        txHandler
                    );
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
