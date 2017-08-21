package com.tistory.tobyspring.controller;

import com.tistory.tobyspring.context.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HelloController {

	@Autowired
	private Hello hello;

	@RequestMapping("hello")
	public ModelAndView handleRequest() {
		hello.setName("Servlet Context");
		Map<String, Object> map = new HashMap<>();
		map.put("message", hello.sayHello());
		return new ModelAndView("hello", map);
	}
}
