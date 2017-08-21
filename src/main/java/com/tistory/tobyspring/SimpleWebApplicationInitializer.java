package com.tistory.tobyspring;

import com.tistory.tobyspring.config.WebConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class SimpleWebApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		initRootContext(servletContext);
		initServletContext(servletContext);
	}

	private void initRootContext(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.scan("com.tistory.tobyspring.config");

		ServletContextListener listener = new ContextLoaderListener(context);
		servletContext.addListener(listener);
	}

	private void initServletContext(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.scan("com.tistory.tobyspring.controller");
		context.register(WebConfig.class);

		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("spring", new DispatcherServlet(context));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/app/*");
	}
}
