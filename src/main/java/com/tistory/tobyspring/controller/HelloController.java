package com.tistory.tobyspring.controller;

import com.tistory.tobyspring.context.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class HelloController implements Controller {

    @Autowired
    private Hello hello;

    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        hello.setName("Servlet Context");
        Map<String, Object> map = new HashMap<>();
        map.put("message", hello.sayHello());
        return new ModelAndView("/WEB-INF/view/hello.jsp", map);
    }
}
