package com.tistory.tobyspring.context;

//@Configuration
public class Config {

//    @Bean
    public Hello hello(/*Printer printer*/) {
        Hello hello = new Hello();
        hello.setPrinter(printer());
        return hello;
    }

//    @Bean
    public Printer printer() {
        return new StringPrinter();
    }
}
