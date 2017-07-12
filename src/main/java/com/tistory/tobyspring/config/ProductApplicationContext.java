package com.tistory.tobyspring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@Profile("product")
@PropertySource(name = "mail", value = "classpath:properties/mail.properties")
public class ProductApplicationContext {

    @Value("#{mail['mail.host']}")
    private String host;

    @Value("#{mail['mail.port']}")
    private Integer port;

    @Value("#{mail['mail.username']}")
    private String username;

    @Value("#{mail['mail.password']}")
    private String password;

    @Bean
    public MailSender mailSender() {
        System.out.println(host);
        System.out.println(port);
        System.out.println(username);
        System.out.println(password);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setJavaMailProperties(javaMailProperties());
        return mailSender;
    }

    @Bean
    public Properties javaMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.starttls.enable", "true");
        return properties;
    }
}
