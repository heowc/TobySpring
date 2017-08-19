<%@ page import="com.tistory.tobyspring.context.Hello" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>Test</title>
</head>
<body>

<%
	ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());

	Hello hello = context.getBean(Hello.class);
	hello.setName("Root Context");

	out.println(hello.sayHello());
%>

</body>
</html>
