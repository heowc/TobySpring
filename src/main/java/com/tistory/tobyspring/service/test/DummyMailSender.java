package com.tistory.tobyspring.service.test;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * 메일 전송 기능이 없는 빈 클래스 <BR>
 */
public class DummyMailSender implements MailSender {

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
        // ...
        System.out.println("Dummy ===> 전송 완료");
    }

    @Override
    public void send(SimpleMailMessage[] simpleMailMessages) throws MailException {
        // ...
    }
}