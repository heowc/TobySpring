package com.tistory.tobyspring.controller;

import com.tistory.tobyspring.context.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HelloController {

	@Autowired
	private Hello hello;

	@RequestMapping("hello")
	public ModelAndView handleRequest(HttpServletRequest request) {
		hello.setName("Servlet Context");
		Map<String, Object> map = new HashMap<>();
		map.put("message", hello.sayHello());

		HttpSession session = request.getSession();
		session.setAttribute("id", "heowc");

		return new ModelAndView("hello", map);
	}


	@RequestMapping("session")
	public ModelAndView session(HttpServletRequest request) {
		HttpSession session = request.getSession();

		String id = (String) session.getAttribute("id");
		if (id == null || "".equals(id)) {
			return new ModelAndView("redirect:/hello");
		}

		return new ModelAndView("hello");
	}
}
