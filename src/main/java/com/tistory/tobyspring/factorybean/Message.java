package com.tistory.tobyspring.factorybean;

public class Message {

    String text;

    private Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Message newInstance(String text) {
        return new Message(text);
    }
}
