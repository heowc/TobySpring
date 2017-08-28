
## Application Context

### 종류

- StaticApplicationContext
	- 개발을 하면서 테스트용도 이외에는 쓸 일이 없다. ( 스프링을 이용한 자체 프레임워크 개발에는 사용? )

- GenericApplicationContext
	- 가장 일반적인 애플리케이션 컨텍스트 구현이다. xml 등록 가능 ( GenericXmlApplicationContext 를 더 많이 사용 )

- WebApplicationContext
	- 가장 많이 쓰는 애플리케이션 컨텍스트 구현이다. ( XmlWebApplicationContext 를 더 많이 사용 )

		> 웹은 자바 애플리케이션의 main() 메소드가 없기 때문에 서블릿을 만들어 질 때 컨텍스트를 생성해둔 다음
			요청이 서블릿으로 들어올 때마다 getBean()으로 필요한 빈을 가져와 정해진 메소드를 실행해준다.
			서블릿에서 DispatcherServlet 이라는 것을 제공하여 가능하다. (web.xml 에 등록)

### 특징

- ApplicationContext 는 계층 구조를 갖을 수 있다.
	- 해당 컨텍스트에서 원하는 빈을 발견하지 못하면, 자신의 부모 컨텍스트에서 빈을 찾는다.

### Web Application

1. 웹 모듈 안에 컨테이너를 두는 방법
	- 독립적으로 배치 가능한 웹 모듈(war) 형태로 애플리케이션
	- 웹 애플리케이션은 여러 개의 서블릿을 가질 수 있다.
	- 몇 개의 서블릿이 중앙집중식으로 모든 요청을 다 받아서 처리하는 방식 => 프론트 컨트롤러 패턴
2. 엔터프라이즈 애플리케이션 레벨에 두는 방법

	> 일반적으로는 두가지 방식을 모두 사용해 컨테이너를 만든다.
		두 개의 컨테이너, 즉 WebApplicationContext 가 만들어진다.
		두개의 서블릿이 하나의 웹 애플리케이션에서 사용되는 경우, 두 서블릿의 컨텍스트에서 공통적으로 사용하는 별도의 컨텍스트를 만들어준다.

## IoC/DI

### IoC Container

- IoC 컨테이너의 가장 기본적인 역할은 코드를 대신해서 애플리케이션을 구성하는 오브젝트를 생성하고 관리하는 것이다.
- 컨테이너는 빈 설정 메타정보를 통해 빈의 클래스와 이름을 제공받는다. 빈을 만들기 위한 설정 메타정보는 파일이나 애노테이션 같은 리소스로부터 전용 리더를 읽혀서 BeanDefinition 타입의 오브젝트로 변환된다.

	```text
	XML 문서  ↘
	애노테이션 → BeanDefinition → IoC 컨테이너
	자바 코드  ↗
	```

- 몇 가지 필수항목을 젱하면 컨테이너에 미리 설정된 디폴트 값이 적용된다.
- 빈 설정 메타정보 항목 중에서 가장 중요한 것은 클래스 이름이다.

### Bean

- 빈 등록 방법
	1. `<bean>`
		- 가장 처음 접할 때 바람직한 방법 중 하나 이다.
		- 자주 사용하는 기술이라면, bean 태그 대신 간결한 커스텀 태그를 만드는 것이 좋다.

	2. 자동인식을 이용한 빈 등록
		- 스프링의 빈 스캐너는 지정된 클래스패스 아래에 있는 모든 패키지의 클래스를 대상으로 필터를 적용하여 빈 등록을 위한 클래스들을 선별해낸다.
		- 디폴트 필터에 적용되는 애노테이션을 스프링에서는 스테레오타입 애노테이션이라고 부른다 ( @Component, @Service, @Repository, ... )
		- XML 을 이용한 빈 스캐너 등록 ( 빈 스캐너를 내장한 애플리케이션 컨텍스트 사용 )

			```xml
			<context:component-scan base-package="com.tistory.tobyspring" />
			```

3. @Configuration 클래스의 @Bean 메소드
	- 컴파일러나 IDE를 통한 타입 검증이 가능하다.
	- 이해하기 쉽다.

- Scope
	- @Scope 나 xml 속성값 scope 변경 가능하다.
	- 기본적으로 singleton 이다. ( singleton, prototype, request, session, globalsession )
	- prototype 은 오브젝트 중심 아키텍처에 적합(?)할 수 있다.
	- session 은 DL 방식으로는 Provider 나 ObjectFactory 에 넣어두고 사용할 수 있다. DI 방식으로는 프록시 설정을 변경하여 사용할 수 있다.

- Meta 정보
	- 식별자 ( id, name 을 이용 )
		- name 경우, 별칭처럼 사용하여 다양하게 참조될 수 있다.

	- 초기화 메소드

		1. InitializingBean 구현
		2. xml 속성값 init-method 지정   
		3. __@PostConstruct__
		4. @Bean(initMethod)

	- 제거 메소드

	   1. DisposableBean 구현
	   2. xml 속성값 destroy-method 지정
	   3. __@PreDestroy__
	   4. @Bean(destroyMethod)

- Bean 종류
	- 애플리케이션 로직 빈
		- 비즈니스 로직을 담당하는 빈
	- 애플리케이션 인프라 빈
		- 스프링에서 제공하지 않는 부분을 빈으로 만드는 것
		- ex) DataSource, DataSourceTransactionManager
	- 컨테이너 인프라 빈
		- 빈 등록&생성, 환경설정, 초기화
		- @PostConstruct

			> @Configuration, @Bean, @PostConstruct 은 스프링 기본 스펙이 아니다.
				빈 후처리기에 의해 등록 되는 것이기 때문에, 빈 후처리기를 등록해주는 <context:annotation-config /> 를 추가해줘야 한다.
				만약 <context:component-config basepackages="..." /> 를 등록해줬다면, component scan 과정에서 annotation-config 에서
				등록해주는 빈을 등록 해주기 때문에 <context:annotation-config /> 를 생략해도 된다.

		- @ComponentScan, @Import, @ImportResource, @Enable~~

- 런타임 환경 추상화
	- bean 태그에 profile 적용
	- 활성화 profile 적용방법
		- WAS 레벨 : JVM Option에 `-Dspring.profiles.active={profile}`
		- servlet 레벨
			```xml
			<webapp>
				<!-- ... -->
				<context-param>
					<param-name>spring.profiles.active</param-name>
					<param-value>{profile}</param-value>
				</context-param>

				<servlet>
					<!-- ... -->
					<init-param>
						<param-name>spring.profiles.active</param-name>
						<param-value>{profile}</param-value>
					</init-param>
					<!-- ... -->
				</servlet>
				<!-- ... -->
			</webapp>
			```
		- bean 레벨 : `<beans profile={profile}` , `@Profile("{profile}")`

	- 프로퍼티
		- java  : `new Properties().load(new FileInputStream("~"));`
		- XML ① : `<util:properties location="~" />`
		- XML ② : `<context:propertyplaceholder location="~" />`

		※ 프로퍼티는 ISO-8859-1 인코딩만 지원 => `new Properties().loadFromXML("~") 사용`

		- 환경변수, 시스템 프로퍼티, jndi에 유용하게 사용

## 데이터 액세스 기술

### DAO

- 인터페이스로 구현한다.
- 필요한 메소드만 public 접근자 사용
- 복구 불가능한 예외는 RuntimeException 사용
- DataSource
	- SimpleDriverDataSource (매번 커넥션 만들어 사용)
	- SimpleConnectionDataSource (하나의 물리적인 커넥션 사용) -> sync 오류 발생
	=> 위 두가지는 테스트용도로만 사용한다.
- DB 커넥션 풀을 사용한다.
	- 오픈소스 DB 커넥션 풀(아파치 DBCP(2), c3p0 JDBC, 상용 DB 커넥션풀)
	- JNDI,WAS DB 풀

### JDBC

- 기본이 되는 low-level API
- 스프링 JDBC는 단순하고 템플릿/콜백을 지원한다.
	- SimpleJdbcTemplate
		- 위치 치환자 가능 ( ? 사용)
		- 이름 치환자 가능 ( Map이나 MapSourceParameterSource 사용, BeanPropertySqlParameterSource 사용 => domain 이나 dto 클래스 필드 명(setter/getter))
	- SimpleJdbcInsert, SimpleJdbcCall
- 특징
	- 단일 로우 조회 시, 결과가 없으면 EmptyResultDataAccessException 발생
	- 다중 컬럼 조회 시, RowMapper 나 BeanPropertySqlParameterSource 사용
	- SimpleJdbcInsert 는 테이블 별로 만들어서 사용
	- SimpleJdbcCall 은 저장 프로시저나 저장 펑션에 사용
- 스프링 JDBC 설계법
	- DataSource 를 DI로 갖고, JdbcTemplate 와 JdbcInsert, JdbcCall을 생성
		`why ?) JdbcInsert, JdbcCall은 DAO마다 다른 오브젝트를 갖는 경향이 있다.`
	- JdbcDaoSupport 제공

### ibatis ( mybatis )

- SQL Mapper
- SQL을 별도의 파일로 분리 가능하다.
- 오브젝트와 SQL간의 자동 매핑이 이루어진다.
- SqlMapClient 핵심 인터페이스
	- JDBC에서 필수로 사용되는 Connection, Statement, ResultSet과 동급 오브젝트
	- SqlMapClientBuilder 로 생성 가능(팩토리빈)
- 공통 설정 XML과 매핑 정보 XML 이 필요하다.
	- 공통 설정 XML
		- 데이터소스 => 스프링빈 이용 가능
		- 트랜잭션 => 스프링빈 이용 가능
		- 매핑리소스
		- 프로퍼티
		- 타입 별칭 핸들러
	- 매핑 정보 XML
		- SQL문
		- 자바 오브젝트
- 스프링에서는 SqlMapClient 를 받아 사용할 수 있는 SqlMapClientTemplate 를 제공
- SqlMapClientDaoSupport 제공

### JPA

- 영속성 관리와 O/R 매핑을 위한 표준 기술
- 오브젝트 중심인 언어에서의 불푠함 해소
- EntityManager 핵심 인터페이스
- EntityManagerFactory 로 생성 가능(팩토리빈)
	- LocaleEntityManagerFactoryBean => 권장하지 않음
	- Java EE에서 제공하는 JPA Provider(JNDI)
	- 스프링에서 제공하는 LocalContainerEntityManagerFactoryBean
- 바이트 코드를 직접 조작하여 확장된 기능 추가
	- 빌드 중 변경
	- 런타임 시, 바이트 코드를 메모리에 로딩하면서 변경
		- 로드타임 위빙
		- 해당 클래스를 "로드타임 위버"라고 불린다.
- JPA는 반드시 트랜잭션 안에서 동작
- JpaTempate, JpaDaoSupport 제공

### 하이버네이트

- 가장 성공한 ORM
- SessionFactory 핵심 인터페이스
	- LocalSessionFactoryBean
	- AnnotationSessionFactoryBean
- 트랜잭션 매니저
	- HibernateTransactionManager
	- JtaTransactionManager
- HibernateTemplate, Session 제공

### 트랜잭션

- 선언적 트랜잭션 가능
	- 코드 내에서 직접 트랜잭션을 관리하고 트랜잭션 정보를 파라미터로 넘겨서 사용하지 않아도 된다.
	- 트랜잭션 스크립트 방식(하나의 트랜잭션 안에서 동작해야 하는 코드를 한 군데 모아서 만드는 방식)의 코드를 탈피할 수 있다.
- 고급 기능(JTA, Entity Bean)을 간단한 톰캣 서버에서 동작하는 가벼운 애플리케이션에도 적용해준다.
- 스프링에서 제공하는 트랜잭션 서비스는 추상화와 동기화이다.
	- 동기화는 추상화와 데이터 액세스 기술을 위한 탬플릿과 더불어 선언적 트랜잭션을 가능하게 해주는 핵심기능이다.
	- 데이터 액세스 기술
		- PlatformTransactionManager : 추상화의 핵심 인터페이스
		- 종류
			- DataSourceTransactionManager
			- JpaTransactionManager
			- HibernateTransactionManager
			- JmsTransactionManager
			- CciTransactionManager
			- JtaTransactionManager
- aop와 tx 네임스페이스, @Transactional

## 스프링 MVC

- 종류
	- 스프링 웹 프레임워크
		- 스프링 서블릿/스프링 MVC : DispatcherServlet 핵심 기술
		- 스프링 포틀릿 : JSR-168, 286 자바 표준 기술
	- 스프링 포트폴리오 웹 프레임워크
		- Spring Web Flow
		- Spring JavaScript
		- Spring Faces
		- Spring Web Service
		- Spring BlazeDS Integration
	- 그 외
		- JSP/Servlet
		- 스트럿츠1
		- 스트럿츠2
		- Tapestry 3, 4
		- JSF/Seam

- 유연성과 확장성을 갖춘 범용적 프레임워크
- M(정보를 담는 모델), V(화면 출력 뷰), C(제어 로직 컨트롤러)
- MVC 아키텍처는 보통 프론트 컨트롤러 패턴과 함께 적용된다.
	```text
		HTTP 요청 → DispatcherServlet ↔ Controller
		                   ↑↓               ↓
		HTTP 응답 ←        View            Model
	```
	1. DispatcherServlet 의 HTTP 요청 접수
		- web.xml 에 정의

	2. DispatcherServlet 에서 컨트롤러로 HTTP 요청 위임
		- 핸들러 매핑 전략 ( 컨트롤러 = 핸들러 )

	3. 컨트롤러의 모델 생성과 정보 등록
		- 사용자 요청 해석
		- 서비스 계층 위임
		- 결과 모델 생성
		- 뷰 종류 결정

	4. 컨트롤러의 결과 리턴 : 모델과 뷰
		- 뷰 리졸러가 이를 이용햐 뷰 오브젝트를 생성

	5. DispatcherServlet 의 뷰 호출과 모델 참조
		- 클라이언트에게 전달할 최종 결과물( JSP, PDA / RSS, JSON ) 생성 요청
		- 최종적으로 HttpServletResponse 에 담김.

	6. HTTP 응답 돌려주기
		- HttpServletResponse 를 서블릿 컨테이너에 전달

- HandlerMapping
	- 컨트롤러 결정 로직을 담당
	- BeanNameUrlHandlerMapping / DefaultAnnotationHandlerMapping

- HandlerAdapter
	- DispatcherServlet 이 선택할 컨트롤러를 호출할 때 사용
	- HttpRequestHandlerAdapter / SimpleControllerHandlerAdapter / AnnotationMethodHandlerAdapter
	- 핸들로 매핑과 어댑터는 관련이 없을 수 있다.
	- 하지만 @RequestMapping / @Controller 경우, DefaultAnnotationHandlerMapping 로 핸들러를 지정하고, AnnotationMethodHandlerAdapter 로 대응되어 호출된다.

- HandlerExceptionResolver
	- 예외가 발생했을 때 처리하는 로직
	- DispatcherServlet 을 통해 처리돼야 한다.
	- AnnotationMethodHandlerExceptionResolver / ResponseStatusExceptionResolver / DefaultHandlerExceptionResolver

- ViewResolver
	- 뷰 이름을 참고하여, 적절한 뷰 오브젝트를 찾아주는 전략 오브젝트.
	- InternalResourceViewResolver

	1. LocaleResolver
		- 지역 정보를 결정 해주는 전략
		- AcceptHeaderLocaleResolver => HTTP 헤더 정보로 지역 정보 설정
		- HTTP 헤더, 세션, URL 파라미터, 쿠키, XML 등 이용 가능

	2. ThemeResolver
		- 테마 정보 결정해주는 전략

	3. RequestToViewNameTranslator
		- 뷰 이름이나 뷰 오브젝트를 제공해주지 않았을 경우, URL 을 참고하여 자동으로 뷰 이름을 생성
		- DefaultRequestToViewNameTranslator

	- DispatcherServlet 을 직접 DI 할 수 없다.

### 스프링 웹 애플리케이션 환경 구성

- 생성 방법
	1. Spring MVC Project
	2. Dynamic Web Project

- 웹 애플리케이션 구성 방법 ( 3가지 )
	1. 루트 컨텍스트와 서블릿 컨텍스트
		- 서블릿 컨텍스트는 루트 컨텍스트를 부모로 갖는다.
		- 서블릿 컨텍스트
			- 핸들러 어댑터 : SimpleControllerHandlerAdapter
			- 핸들러 매핑 : BeanNameUrlHandlerMapping
			- 뷰 리졸버 : InternalResourceViewResolver

- 서블릿 Mock 테스트
	- MockHttpServletRequest
	- MockHttpServletResponse
	- MockHttpSession
	- MockServletConfig / MockServletContext
	- ConfigurableDispatcherServlet
	- AbstractDispatcherServletTest

- 컨트롤러

	- 서블릿 컨테이너가 요청 애트리뷰트로 전달해주는 것들을 참고 해야한다.
	- 컨트롤러 종류와 핸들러 어댑터
		- Servlet / SimpleServletHandlerAdapter
		- 기존 코드 유지 용이

		- Controller / SimpleControllerHandlerAdapter
			- handlerRequest 를 응용한 AbstractController 권장
			- 세션 동기화 보장 가능
			- HTTP 메소드 지정 가능
			- 각종 헤더 프로퍼티 활성화 가능

		- AnnotationMethodHandlerAdapter
			- 컨트롤러 타입 지정되어 있지 않음 ( 애노테이션 정보와 메소드 이름, 파라미터, 리턴 타입 )
			- 컨트롤러 하나가 하나 이상의 URL 매핑 가능
			- DefaultAnnotationHandlerMapping 핸들러 매핑과 함께 사용해야 한다.
			- 가장 인기 있는 컨트롤러 작성 방법

	- 핸들러 매핑
		- HTTP 요청정보를 이용해서 이를 처리할 핸들러 오브젝트, 즉 컨트롤러를 찾아주는 기능
		- BeanNameUrlHandlerMapping / DefaultAnnotationHandlerMapping 이 기본적으로 등록된다.
		- 종류
			- BeanNameUrlHandlerMapping
			- 빈의 이름에 들어있는 URL 을 HTTP 요청의 URL 과 비교해서 일치하는 빈을 찾아준다.

		- ControllerBeanNameHandlerMapping
			- 빈의 아이디나 빈의 이름을 이용해 매핑해주는 핸들러 매핑 전략

		- ControllerClassNameHandlerMapping
			- 빈 이름 대신 클래스 이름을 URL 에 매핑해주는 핸들러 매핑 클래스

		- SimpleUrlHandlerMapping
			- URL 과 컨트롤러의 매핑정보를 한곳에 모아놓을 수 있는 핸들러 매핑 전략

		- DefaultAnnotationHandlerMapping
			- @RequestMapping 이라는 애노테이션을 컨트롤러 클래스나 메소드에 직접 부여하고 이를 이용해 매핑하는 전략

	- 핸들러 인터셉터
		- DispatcherServlet 이 컨트롤러를 호출하기 전과 후에 요청과 응답을 참조하거나 가공할 수 있는 일종의 필터
		- HandlerInterceptor 인터페이스 구현
			- preHandle : 컨트롤러 호출 전
			- postHandle : 컨트롤러 호출 후 ( preHandle 에서 false 를 호출했다면, 호출 안됨)
			- afterCompletion : 모든 작업이 다 완료된 후에 실행
		- 컨트롤러에 공통적으로 적용할 부가기능 추가에 용이

- 뷰
	- 표현 로직 컴포넌트
	- DispatcherServlet 에 돌려주는 방법
		1. View 타입 오브젝트
		2. 뷰 이름
	- 뷰
		- 스프링에서 제공하는 View 인터페이스를 구현해야 한다.
		- InternalResourceView
			- RequestDispatcher 의 forward() 와 include() 이용
			- 다른 서블릿을 이용하여 그 결과를 현재 서블릿의 결과로 사용하거나 추가하는 방식

		- JstlView
			- InternalResourceView 의 서브클래스이다.
			- 지역화된 메시지를 사용 가능

		- RedirectView
			- HttpServletResponse 의 sendRedirect()를 호출해주는 기능

		- VelocityView, FreeMarkerView
			- 밸로시티와 프리마터라는 자바 템플릿 엔진을 뷰로 사용하게 해준다.
			- VelocityViewResolver 와 FreeMarkerViewResolver 를 통해 자동으로 뷰가 만들어져 사용되게 하는 편이 좋다.
			- JSP 에 비해 문법이 강력하고 속도가 빠른 템플릿이다.
			- 서블릿을 구동시켜야하는 JSP 에 반해 별도의 템플릿 엔진으로 뷰를 생성하기 떄문에 단위 테스트도 쉽다.

		- MarshallingView
			- OXM 추상화 기능 뷰
			- XML 로 변환 하여 뷰의 결과로 사용할 수 있다.

		- AbstractExcelView, AbstractJExcelView, AbstractPdfView
			- 엑셀과 PDF 문서를 만들어주는 뷰

		- AbstractAtomFeedView, AbstractRssFeedView
			- application/atom+xml 과 application/rss+xml 타입의 피드 문서 생성

		- XsltView, TilesView, AbstractJasperReportsView
			- xslt 변환을 이용한 뷰 생성, Tiles 뷰 생성, 리포트 작성용 프레임워크인 JasperReports 를 이용해 CSV, HTML, PDF, Excel 작성.

		- MappingJacksonJsonView
			- JSON 타입의 콘텐트를 작성해주는 뷰

	- 뷰 리졸버
		- 뷰 이름의 경우, 뷰 리졸버가 필요 ( 논리적 뷰 -> 실질적 뷰 오브젝트 )
		- ViewResolver 인터페이스를 구현해야 한다.
		- InternalResourceViewResolver
			- 뷰 리졸버를 지정하지 않았을 때, 자동등록되는 디폴트 뷰 리졸버

		- VelocityViewResolver, FreeMarkerViewResolver
			- ~~Configurer 경로 지정해줘야한다.

		- ResourceBundleViewResolver, XmlViewResolver, BeanNameViewResolver
			- 여러 가지 종류의 뷰를 혼용하거나 뷰의 종류를 코드 밖에서 변경해줘야 하는 경우 사용
			- ResourceBundleViewResolver 는 클래스패스의 views.properties 를 찾아 사용한다.
			- XmlViewResolver 은 /WEB-INF/views.xml 를 찾아 사용한다.
			- BeanNameViewResolver 은 뷰 이름과 동일한 빈 이름을 가진 빈을 찾아서 뷰로 사용하게 된다.

		- ContentNegotiatingViewResolver
			- 스프링 3.0에서 추가된 뷰 리졸버
			- 뷰 리졸버를 결정해주는 리졸버
			- 미디어 타입 결정
				1. URL 의 확장자를 사용하는 방법
				2. favorParameter = true, format 파라미터 이용
				3. ignoreAcceptHeader = false, Accept 해더 이용
				4. defaultContentType 이용
			- 뷰 리졸버 위임을 통한 후보 뷰 선정
				- viewResolvers 로 사용할 뷰 리졸버 지정
			- 미디어 타입 비교를 통한 최종 뷰 선정
				- 미디어 타입과 뷰 리졸버에서 찾은 후보 뷰 목록을 매칭해서 뷰 결정

- 기타 전략
	- 핸들러 예외 리졸버 ( HandlerExceptionResolver )
		- 해당 리졸버가 등록되어 있다면, web.xml 에 `<error-page>`를 등록한 것 보다 우선적으로 처리된다.
		- HandlerExceptionResolver 인터페이스를 구현해야 한다.
		- 기본적으로 4개의 전략을 제공한다. ( 3개는 기본적으로 등록되어 있다. )
			- AnnotationMethodHandlerExceptionResolver
				- 특정 Exception 에 대해 특별한 처리가 필요한 경우
				- @ExceptionHandler 사용

			- ResponseStatusExceptionResolver
				- HTTP 500 에러 대신 의미 있는 HTTP 응답 상태 값을 보여주는 것
				- @ResponseStatus 사용

			- DefaultHandlerExceptionResolver
				- 스프링 내부적으로 발생하는 주요 예외를 처리해주는 표준 예외처리 로직

			- SimpleMappingExceptionResolver
				- web.xml 에 `<error-page>`와 비슷하게 예를 처리할 뷰를 지정할 수 있게 해준다.

	- 지역정보 리졸버 ( LocaleResolver )
		- 지역정보를 결정하는 전략
		- 브라우저의 기본 설정따라 보내지는 AcceptHeaderLocaleResolver 를 디폴트로 사용된다.
		- 사용자가 직접 변경하도록 만드려면 SessionLocaleResolver 나 CookieLocaleResolver 를 사용한다.

	- 멀티파트 리졸버 ( CommonsMultipartResolver )
		- 파일 업로드와 같이 멀티파트 포맷의 요청정보를 처리하는 전략 설정
		- apapche commons 의 fileUpload 만 지원한다.
		- 디폴트로 등록되는 것이 없다.
		- 과다한 크기의 파일 업로드를 막기 위해 maxUploadSize 프로퍼티를 설정하도록 권장한다.

	- RequestToViewNameTranslator
		- 뷰 이름이나 뷰 오브젝트를 돌려주지 않았을 경우 HTTP 요청정보를 참고하여 뷰 이름을 생성해주는 로직을 담고 있다.
		- DefaultRequestToViewNameTranslator 가 기본적으로 등록되어 있다.
			- URL 을 기준으로 해서 뷰 이름을 결정

### 스프링 3.1의 MVC
- 플래시 맵 매니저 전략
	- 플래시 맵
		- 플래시 애트리뷰트를 저장하는 맵
		- 플래시 애트리뷰트는 다음 요청에서 한 번 사용되고 바로 제거됨
		- Post/Redirect/Get 패턴
		- URL 조건과 제한시간을 지정 가능

	- 플래시 맵 매니저
		- 플래시 맵을 저장하고, 유지하고, 조회하고, 제거하는 등의 작업을 담당하는 오브젝트
		- FlashMapManager 인터페이스 구현

	- 플래시 맵 매니저 전략
		- 플래시 맵 정보는 HTTP 세션을 이용하여 저장된다. ( SessionFlashMapManager )

- WebApplicationInitializer 를 이용한 컨텍스트 등록
	- WAS 의 서블릿 컨테이너가 서블릿 3.0이나 그 이상을 지원하지 않는다면 사용 불가능
	- web.xml 탈피, 설정 방식을 모듈화 관리 가능
	- 스프링 3.1에서는 ServletContainerInitializer 이용

## @MVC

### @RequestMapping

- DefaultAnnotationHandlerMapping 등록
- @RequestMapping
	- value : URL 패턴
	- method : HTTP 요청 메소드
	- params : 요청 파라미터
	- headers : HTTP 헤더

- 타입 레벨 매핑과 메소드 레벨 매핑의 결합
	- 타입(클래스나 인터페이스) 레벨에 붙은 @RequestMapping 은 타입 내의 모든 매핑용 메소드의 공통 조건을 지정할 때 사용

- 메소드 레벨 단독 매핑

- 타입 레벨 단독 매핑

- 타입 상속과 매핑
	- @RequestMapping 정보는 상속된다.
	- 매핑정보 상속의 종류
		1. 상위 타입과 메소드의 상속
		2. 상위 타입과 하위 타입 메소드 결합
		3. 하위 타입과 메소드의 재정의
		4. 서브클래스 메소드의 URL 패턴 없는 재정의
		5. 제네릭스와 매핑정보 상속을 이용한 컨트롤러 작성

### @Controller

- @MVC 에 가장 강력하고 획기적인 방법
- AnnotationMethodHandlerAdapter 사용
- 파라미터 종류
	- HttpServletRequest, HttpServletResponse
	- HttpSession
	- WebRequest, NativeWebRequest
	- Locale
	- InputStream, Reader
	- OutputStream, Writer
	- @PathVariable
	- @RequestParam
	- @CookieValue
	- @RequestHeader
	- Map, Model, ModelMap
	- @ModelAttribute
	- Errors, BindingResult
	- SessionStatus
	- @RequestBody : messageConverter
	- @Value
	- @Valid : JSR-303

- 리턴 타입 종류
	- ModelAndView
	- String
	- void
	- 모델 오브젝트
	- Map, Model, ModelMap
	- View
	- @ResponseBody

- @SessionAttributes 와 SessionStatus
	- 데이터 유지
	- 필요성 이유
		1. 히든 필드
		2. DB 재조회
		3. 계층 사이의 강한 결합

	- @SessionAttributes
		- 기본적으로 HttpSession 이용
		- @ModelAttribute 로 해당 오브젝트를 가져올 수 있다.

	- SessionStatus
		- 세션 제거

### 모델 바인딩과 검증

- @Controller 에서 @ModelAttribute 가 지정된 파라미터를 가져올 때 3가지 과정을 거친다.
	1. 파타미터 타입의 오브젝트를 만든다.
	2. 준비된 오브젝트의 파라미터에 웹 파라미터를 바인딩 해준다.
	3. 모델의 값을 검증하는 것이다.

- 바인딩
	1. XML 설정 파일을 사용하여 빈을 정의할 경우
	2. HTTP 을 통해 전달되는 클라이언트의 요청을 모델 오브젝트로 변환할 경우

- PropertyEditor
	- 기본적으로 제공하는 바인딩 타입 변환 API
	- 자바빈 표준에 정의된 인터페이스
	- `org.springframework.beans.propertyeditors` 패키지에 구현되어 있음
	- PropertyEditorSupport 으로 간단하게 구현

- @InitBinder
	- HTTP 요청 파라미터가 적절히 변환돼서 들어가도록 만드는 것
	- AnnotationMethodHandlerAdapter 는 애노테이션에 바인딩 해줄 때, WebDataBinder 를 만든다.

- WebBindingInitializer
	- 바인딩이 애플리케이션 전반에 걸쳐 폭넓게 필요한 경우

- 프로토타입 빈 프로퍼티 에디터
	- 프로퍼티 에디터는 thread safe 하지 못 하다.
	- 방법
		1. 별도의 codeId 필드로 바인딩
		2. 모조 오브젝트 프로퍼티 에디터
		3. 프로토타입 도메인 오브젝트 프로퍼티 에디터

- Converter 와 Formatter
	- Converter
		- 단방형 변환만 지원

	- ConversionService
		- 컨트롤러 바인딩
		- 보통은 GenericConversionService 클래스 빈을 사용하면 된다.
			- 다양한 타입 변환 기능을 가진 오브젝트를 등록할 수 있도록 ConverterRegistry 인터페이스 제공
			- WebDataBinder 에 GenericConversionService 를 설정하는 방법
				1. @InitBinder 수동 등록
				2. ConfigurableWebBindingInitializer 를 이용한 일괄 등록

	- Formatter 와 FormattingConversionService
		- 범용적인 타입 변환 API
		- FormattingConversionServiceFactoryBean
			- 기본적으로 등록되는 Formatter
				1. @NumberFormat
				2. @DateTimeFormat

	- 바람직한 활용 전략
		- 사용자 정의 타입의 바인딩을 위한 일괄 적용 : Converter
		- 필드와 메소드 파라미터, 애노테이션 등의 메타정보를 활용하는 조건부 변환 기능 : ConditionGenericConverter
		- 애노테이션 정보를 활용한 HTTP 요청과 모델 필드 바인딩 : AnnotationFormatterFactory 와 Formatter
		- 특정 필드에만 적용되는 변환 기능 : PropertyEditor

	- WebDataBinder 설정 항목
		- allowedFields, disallowFields : @ModelAttribute 바인딩에서 시용하고자 하는 필드 지정
		- requiredFields : 바인딩 시, 필수로 요구되는 필드 지정
		- fieldMarkerPrefix : 체크박스 같은 값을 히든 필드에 넣을 때, 앞에 붙이는 접두어 지정
		- fieldDefaultPrefix : 체크박스 기본 값을 히든 필드에 넣을 때, 앞에 붙이는 접두어 지정

- Validator 와 BindingResult, Errors
	- Validator 표준 인터페이스
	- BindingResult 는 Errors 의 서브 인터페이스
	- Validator
		- 오브젝트 검증 API
		- 동일한 타입 체크, 데이터 검증 가능
		- ValidationUtils 유틸 클래스 존재
		- 검증 방법
			1. 컨트롤러 메소드 내의 코드
			2. @Valid 이용한 자동 검증
			3. 서비스 계층 오브젝트에서의 검증
			4. 서비스 계층을 활용하는 Validator

	- JSR-303 빈 검증 기능
		- LocalValidatorFactoryBean 이용

	- BindingResult 와 MessageCodeResolver
		- BindingResult 에는 타입 변환 오류정보와 검증 작업에 발견된 검증 오류정보가 모두 저장
		- 기본적으로 에러 메시지는 messages.properties  와 같은 프로퍼티에서 가져온다.
		- MessageCodeResolver 를 통해 메시지 키 값으로 확장된다.
		- 기본적으로 DefaultMessageCodeResolver 활용한다.

	- MessageSource
		- MessageSourceResolver 를 한 번 더 거쳐 최종적인 메시지를 만든다.
		- 구현 방법
			1. 코드로 등록 : StaticMessageSource
			2. 리소스 번들 방식( messages.properties ) : ResourceBundleMessageSource
				- 일정 주기로 재갱신(재시작 안해도 됨) : ReloadableResourceBundleMessageSource

		- 4가지 정보 활용
			1. 코드
			2. 메시지 파라미터 배열
			3. 디폴트 메시지
			4. 지역정보
- 모델의 일생
	- 모델은 MVC 아키텍처에서 정보를 담당하는 컴포넌트이다.

	- HTTP 요청으로부터 컨트롤러 메소드까지
		- @ModelAttribute 메소드 파라미터
		- @SessionAttribute 세션 저장 대상 모델 이름
		- WebDataBinder 에 등록된 프로퍼티 에디터, 컨버전 서비스
		- WebDataBinder 에 등록된 검증기
		- ModelAndView 의 모델 맵
		- 컨트롤러 메소드와 BindingResult 파라미터

	- 컨트롤러 메소드부터 뷰까지
		- ModelAndView 의 모델 맵
		- WebDataBinder 에 기본적으로 등록된 MessageCodeResolver
		- 빈으로 등록된 MessageSource 와 LocaleResolver
		- @SessionAttribute 세션 저장 대상 모델 이름
		- 뷰의 EL 과 스프링 태그 또는 매크로

### JSP 뷰와 form 태그

- EL과 spring 태그 라이브러리를 이용한 모델 출력
	- JSP/JSTL

	- JSP EL
		- ${key} 접근

	- 스프링 SpEL
		- JSP EL 보다 유연하고 강력한 표현식 지원
		- spring 태그 라이브러리 추가
			- `<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>`
		- 표현식 작성
			- `<spring:eval expression="{필드}.toString()" />`
			- @NumberFormat, @DataTimeFormat 같은 포맷터 적용 가능

	- 지역화 메시지 출력
		- 언어별 messages.properties 적용 가능, message 태그
			- `<spring:messasge code="{key}" />`

- spring 태그 라이브러리 이용한 폼 작성
	- 단일 폼 모델
	- `<spring:bind>` 와 BindingStatus
		- 오류 출력 용이

- form 태그 라이브러리
	- `<spring:bind>` 보다 간결한 코드로 동일한 기능 구현
	- 일반적인 HTML 사용 안됨
	- `<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>`
	- `<form:form>`
		- form 태그
		- commandName, modelAttribute
			- 폼에 적용할 모델의 이름 지정
		- method
			- http method 지정
			- 서블릿 필터로 HiddenHttpMethodFilter 추가
			- `<input type="hidden" name="_method" value="{method} />"`
		- action

	- `<form:input>`
		- input 태그
		- path
		- cssClass, cssErrorClass

	- `<form:label>`
		- label 태그
		- `<form:input>` 과 동일

	- `<form:errors>`
		- 기본적으로 span
		- path
		- delimiter
			- 복수 에러 메시지 구분자
			- 기본적으로 br 태그 사용
		- cssClass

	- `<form:hidden>`
		- input hidden 태그

	- `<form:password>`, `<form:textarea>`
		- input text 태그와 textarea 태그
		- `<form:input>` 과 동일

	- `<form:checkbox>`, `<form:checkboxs>`
		- input checkbox 태그

	- `<form:radiobutton>`, `<form:radiobuttons>`
		- input radiobutton 태그

	- `<form:select>`, `<form:option>`, `<form:options>`
		- select 태그와 option 태그

### 메시지 컨버터와 AJAX

- XML이나 JSON을 이용한 AJAX 기능이나 웹 서비스를 개발할 때 사용
- 응답 본문을 메시지로 다루는 방식
- @RequestBody 와 @ResponseBody 메시지 컨버터 적용
- 종류
	- AnnotationMethodHandlerAdapter 를 통해 등록
	- 기본적으로 4가지 등록 되어진다.
	- ByteArrayHttpMessageConverter
		- byte[]
		- 모든 미디어 타입 지원
		- application/octet-stream 보내짐
		- 바이너리 정보에 용이

	- StringHttpMessageConverter
		- String
		- 모든 미디어 타입 지원
		- text/plain 보내짐

	- FormHttpMessageConverter
		- application/x-www-form-urlencoded 지원
		- MultiValueMap<String, String> 지원
		- @ModelAttribute 가 더 편리(?)

	- SourceHttpMessageConverter
		- application/xml, application/++xml, text/xml 지원
		- DOMSource, SAXSource, StreamSource ( javax.xml.transform.Source 인터페이스 )


	- Jaxb2RootElementHttpMessageConverter
		- @XmlRootElment 와 @XmlType 이 붙은 클래스 이용
		- SourceHttpMessageConverter 와 동일한 XML 미디어 타입 지워

	- MarshallingHttpMessageConverter
		- 스프링 OXM 추상화의 Mashaller 와 Unmarshaller 이용
		- 미디어 타입은 다른 XML 기반 메시지 컨버터와 동일
		- 지원할 오브젝트 만큼 등록해줘야 한다.

	- MappingJacksonHttpMessageConverter
		- Jackson ObjectMapper 이용해서 자바 오브젝트와 JSON 변환
		- application/json 지원
		- 자바빈 스타일 오브젝트와 HashMap 지원
		- 날짜나 숫자는 부가적인 변환 기능 필요

- JSON 을 이용한 AJAX 컨트롤러 : GET + JSON
	- 활용 방법
		1. MappingJacksonJsonView
		2. @ResponseBody

- JSON 을 이용한 AJAX 컨트롤러 : POST + JSON

### MVC 네임스페이스

- 기본적인 AnnotationMethodHandlerAdapter 활용은 부족하다.
- mvc 스키마의 태그 활용
	- `<mvc:annotation-driven>`
		- 애노테이션 방식의 컨트롤러 사용 시, 필요한 DispatcherServlet 전략 빈 자동 등록
		- DefaultAnnotationHandlerMapping
			- @RequestMapping 를 이용한 핸들러 매핑 전략
		- AnnotationMethodHandlerAdapter
			- 디폴트 핸들러 어댑터
			- `<mvn:annotation-driven` 사용 시, 직접적인 빈 등록하면 안됨
		- ConfigurableWebBindingInitializer
			- 모든 컨트롤러 메소드에 자동으로 적용되는 WebDataBinder 적용
		- 메시지 컨버터
			- 기본 메시지 컨버터 등록
			- 해당 라이브러리가 존재한다면 자동적으로 등록 된다.
		- `<spring:eval>`
		- validator
		- conversion-service

	- `<mvc:intercepters>`
		- HandlerInterceptor 적용

	- `<mvc:view-controller>`
		- 단순 뷰 지정에 용이

### @MVC 확장 포인트

- AnnotationMethodHandlerAdapter 확장 기능
	- SessionAttributeStore
	- WebArgumentResolver
		- HTTP 요청 정보를 원하는 형태로 전달
	- ModelAndViewResolver

### URL 과 리소스 관리

- 스프링 3.0.4 이후 버전 지원 기능
- `<mvc:default-servlet-handler/>` 를 이용한 URL 관리
	- 동적 리소스, 정적 리소스
	- 정적 리소스는 낮은 순위 매핑 저략으로 포워딩

- <url:resources /> 를 이용한 리소스 관리
	- 정적 리소스 관리 용이
	- `<mvc:resources mapping="{URL} location={PATH}">`

### 스프링 3.1의 @MVC

- 새로운 RequestMapping 전략
	- DispatcherServlet 전략 클래스 변경
		- 유연한 확정성을 가질 수 있도록 개선

	- @RequestMapping 메소드와 핸드러 매핑 전략의 불일치
	- HandleMethod : @RequestMapping이 붙은 메소드의 정보를 추상화한 오브젝트 타입
		- 빈 오브젝트
		- 메소드 메타정보
		- 메소드 파라미터 메타정보
		- 메소드 애노테이션 메타정보
		- 메소드 리턴 값 메타정보
	- @RequestMapping 전략 선택
		- 3.1 이후에는 자바 코드를 이용한 @MVC 방식 권장

- @RequestMapping 핸들러 매핑 : RequestMappingHandlerMapping
	- 요청 조건
		- 3.0 : URL, 패턴, 메소드, 파라미터, HTTP 헤더
		- 3.1 : (3.0) + Content-Type 헤더, Accept 헤더
	- 요청 조건의 결합방식
		- URL 패턴 : PatternsRequestCondition
		- HTTP 요청 방법 : RequestMethodsRequestCondition
		- 파라미터 : ParamsRequestCondition
		- 헤더 : HeaderRequestCondition
		- Content-Type 헤더 : ConsumesRequestCondition
		- Accept-Type 헤더 : ProducesRequestCondition

- @RequestMapping 핸들러 어댑터
	- 파라미터 타입
		- @Validated/@Valid
			- JSR-303 검증 : @Valid
			- 제약 조건에 대한 그룹 지정 : @Validated
		- @Valid와 @RequestBody
			- 예외를 따로 처리하지 않으면 MethodArgumentNotValidException 발생. 400 Bad Request
		- UriComponentsBuilder
			- URI/URL 정보를 추상화한 UriComponents 빌더 클래스
		- RedirectAttributes와 리다이렉트 뷰
			- 기존 데이터 제거에 대한 번거로움
			- Model의 서브타입
		- RedirectAttributes와 플래시 애트리뷰트
	- 확장 포인트
		- 파라미터
		- 리턴 값

- @EnableWebMvc와 WebMvcConfigurationSupport를 이용한 @MVC 설정
	- `<mvc:annotation-config/>` 와 @EnableWebMvc 동일
	- 빈 설정자 : @Enable 전용 애노테이션의 설정을 위해 사용되는 빈 컨피규어러
	- @EnableWebMvc의 빈 설정자 : WebMvcConfigurationSupport
		- addFormatters : FormmatterRegistry를 이용한 포매터 등록
		- configureMessageConverters : 기본 메시지컨버터를 무시하고 재구성
		- getValidated : LocalValidatorFactoryBean을 대체할 검증 프레임워크 재구성
		- addArgumentResolvers : 파라미터 처리용 확장
		- addReturnValueHandlers : 리턴 값 처리용
		- configureHandlerExceptionResolvers : 핸들러 예외 전략 재구성
		- addInterceptors : 인터셉터 등록 (HandleInterceptor, WebRequestInterceptor)
		- addViewControllers : 간단한 컨트롤러 등록 메소드
		- addResourceHandlers : `<mvc:resources/>` 담당 메소드
		- configureDefaultServletHandling : `<mvc:default-servlet-handler/>` 담당 메소드
	- 빈 등록 방법
		1. @EnableWebMvc 와 WebMvcConfigurer 구현 클래스 Bean 등록
		2. @EnableWebMvc에 WebMvcConfigurer 구현
		3. @EnableWebMvc에 WebMvcConfigurerAdapter 구현
	- 전략용 설정 빈 등록
		- InternalResourceViewResolver 빈 등록

## AOP와 LTW

### 애스펙트 AOP

- 모듈화된 부가기능과 적용 대상의 조합을 통해 여러 오브젝트에 산재해서 나타나는 공통적인 기능을 손쉽게 개발하고 관리할 수 있는 기술
- 자바 JDK 다이나믹 프록시나 바이트 코드 조작 기술 없이도 프록시 기반 AOP 개발 제공
- 방법
	1. AOP 인터페이스 구현과 `<bean>` 등록
	2. AOP 인터페이스 구현과 `<aop:advisor>` 등록
	3. 임의의 자바 클래스와 `<aop:aspect>` 등록
	4. @AspectJ 등록
- 자동 프록시 생성기와 프록시 빈
	- @Autowired의 타입에 의한 의존관게 설정에 문제를 일으키지 않는다.
	- 다른 빈들이 해당 오브젝트에 직접 의존하지 못하게 한다.
- 프록시의 종류
	- JDK 다이나믹 프록시
	- CGLib (바이트코드 생성 라이브러리)

		> 기존에 개발한 레거시 코드나 외부에서 개발한 인터페이스 없는 라이브러리의 클래스 등에도 적용할 수 있게 해주기 위해

### @AspectJ AOP

- 에스펙트 : 필요한 부가기능을 추상화해놓은 것
- 준비 사항
	- `<aop:aspectj-autoproxy />`
	- 핵심 애노테이션 : @Aspect
- 구성요소
	- 포인트 컷 : @Pointcut
	- 어드바이스 : @Before, @After, @AfterReturning, @AfterThrowing, @Around
- 포인트컷 메소드와 애노테이션
	- 작성법
	```text
	애노테이션      표현식                      반드시 void    ()포함하여 포인컷 이름으로 사용된다.
	@Pointcut("execution(* method(...))") private void proxy_method();
	```
	- execution()
		- 리턴 타입, 타입, 메소드, 파라미터 타입, 예외 타입 조건 조합의 메소드 단위
	- within()
		- 타입 패턴만 적용

	> `.*`은 해당 패키지 바로 밑의 클래스와 인터페이스만, `..*`은 해당 패키지 밑의 모두 포함

	- this, target
		- 하나의 타입을 지정하는 방식
		- this는 빈 오브젝트 타입
		- target은 타깃 오브젝트 타입
	- args
		- 메소드의 파라미터 타입만을 이용
		- within이나 target 같은 타입 레벨과 조합
	- @target, @within
		- @target은 타깃 오브젝트에 특정 애노테이션이 부여된 것은 선정
		- @within은 타깃 오브젝트의 클래스에 특정 애노테이션이 부여된 것을 찾는다. (슈퍼 클래스의 메소드는 해당되지 않는다.)
	- @args
		- args와 유사하게 파라미터를 이용해 선정
	- @annotation
		- 특정 애노테이션이 있는 것만 선정
	- bean
		- 빈 이름 또는 아이디를 이용해서 선정
	- &&
		- 두개의 포인트컷 또는 지시자는 AND 조건으로 결합
	- ||, !
		- ||은 OR 조건
		- !은 NOT 조건
- 어드바이스 메소드와 애노테이션
	- @Around : 타깃 오브젝트의 메소드가 호출되는 전 과정을 모두 담을 수 있는 어드바이스
		- 파라미터 : ProceedingJoinPoint 타입 오브젝트(타깃 오브젝트의 메소드 결과 받을 수 있다.)
		- 메소드 n번 호출, 파라미터 변경, 메소드 호출 방지 가능
	- @Before : 타깃 오브젝트의 메소드가 실행되기 전에 사용되는 어드바이스
		- 호출하는 방식 제어 불가능, 파라미터 변경 불가능
		- 파라미터 : JoinPoint(ProceedingJoinPoint의 슈퍼 인터페이스)
	- @AfterReturning : 타깃 오브젝트의 메소드가 정상적으로 실행을 마친 뒤에 실행되는 어드바이스
		- returning 엘리먼트를 이용하여 리턴 값 파라미터 이름 지정
		- 리턴 값을 조작할 수 없지만, 참조하는 오브젝트는 조작할 수 있다.
		- JoinPoint
	- @AfterThrowing : 타깃 오브젝트의 메소드에서 예외가 발생하면 실행되는 어드바이스
		- throwing 엘리먼트를 이용하여 발생한 예외를 받을 수 있다.
	- @After : 메소드 실행이 마쳤을 때 실행되는 어드바이스
		- finally와 비슷한 용도
		- 반드시 반환돼야 하는 리소스가 있거나 로그를 남겨야 하는 경우에 사용
		- 리턴 값이나 예외를 직접 전달 받을 수는 없다.
- 파라미터 선언과 바인딩
	- 포인트컷 표현식과 타입 정보를 파라미터와 연결하는 방법
	- 패키지 이름을 포함한 클래스 이름 전체를 적어야 한다.
- @AspectJ를 이용한 AOP의 학습 방법과 적용 전략
	- 객체지향적인 방법으로 해결할 수 있는 방법을 불필요하게 AOP를 이용하려고 하지말아야 한다.
	- 충분한 테스트를 작성해야한다.
	- 포인트컷 적용 대상을 제대로 확인한다.

#### AspectJ와 @Configuration

- AspectJ AOP
	- 가장 강력한 AOP 프레임워크
- 빈이 아닌 오브젝트에 DI 적용하기
	- 스프링 빈이 아니라더라도 수정자 메소드를 가진 임의의 오브젝트에 DI를 할 수 있다.
- DI 애스펙트
	- @Configurable
	- `<bean>` 등록
	- autowire 옵션 추가
	- @Autowired 설정

#### 로드타임 위버(LTW)

- 활용
	1. @Configurable 지원
	2. 트랜잭션 AOP를 AspectJ로 지원
	3. JPA 로드타임 위버 사용

#### 스프링 3.1의 AOP와 LTW

- AOP와 LTW를 위한 애노테이션
	- @EnableAspectJAutoProxy
	- @EnableLoadTimeWeaving
