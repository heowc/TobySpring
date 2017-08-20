package com.tistory.tobyspring.config;

import com.tistory.tobyspring.context.Hello;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public Hello hello() {
		return new Hello();
	}
}
