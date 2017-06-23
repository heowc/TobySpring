package com.tistory.tobyspring.factorybean;

import org.springframework.beans.factory.FactoryBean;

/**
 * 팩토리 빈 <BR>
 * 다이나믹 프록시 오브젝트는 일반적인 스프링의 빈으로 등록이 불가능 하다. <BR>
 */
public class MessageFactoryBean implements FactoryBean<Message> {

    String text;

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Message getObject() throws Exception {
        return Message.newInstance(text);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
