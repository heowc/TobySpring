 
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
		두 개의 컨테이너, 즉 WebApplicationContext가 만들어진다.
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

#### 빈 등록 방법

1. bean 태그
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

#### Scope

- @Scope 나 xml 속성값 scope 변경 가능하다.
- 기본적으로 singleton 이다. ( singleton, prototyep, request, session, globalsession )
- prototype 은 오브젝트 중심 아키텍처에 적합(?)할 수 있다.
- session 은 DL 방식으로는 Provider 나 ObjectFactory 에 넣어두고 사용할 수 있다. DI 방식으로는 프록시 설정을 변경하여 사용할 수 있다.

#### Meta 정보

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
   
#### Bean 종류

- 애플리케이션 로직 빈 
	- 비즈니스 로직을 담당하는 빈
- 애플리케이션 인프라 빈 
	- 스프링에서 제공하지 않는 부분을 빈으로 만드는 것
	- ex) DataSource, DataSourceTransactionManager
- 컨테이너 인프라 빈
	- 빈 등록&생성, 환경설정, 초기화
	- @PostConstruct

		> @Configuration, @Bean, @PostConstruct은 스프링 기본 스펙이 아니다.
			빈 후처리기에 의해 등록 되는 것이기 때문에, 빈 후처리기를 등록해주는 <context:annotation-config /> 를 추가해줘야 한다.
			만약 <context:component-config basepackages="..." /> 를 등록해줬다면, component scan과정에서 annotation-config에서 
			등록해주는 빈을 등록 해주기 때문에 <context:annotation-config /> 를 생략해도 된다.

	- @ComponentScan, @Import, @ImportResource, @Enable~~

#### 런타임 환경 추상화

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
		- 이름 치환자 가능 ( Map이나 MapSourceParameterSource 사용, BeanPropertySqlParameterSource 사용 => domain이나 dto 클래스 필드 명(setter/getter))
	- SimpleJdbcInsert, SimpleJdbcCall
- 특징
	- 단일 로우 조회 시, 결과가 없으면 EmptyResultDataAccessException 발생
	- 다중 컬럼 조회 시, RowMapper나 BeanPropertySqlParameterSource 사용
	- SimpleJdbcInsert는 테이블 별로 만들어서 사용
	- SimpleJdbcCall은 저장 프로시저나 저장 펑션에 사용 
- 스프링 JDBC 설계법
	- DataSource를 DI로 갖고, JdbcTemplate와 JdbcInsert, JdbcCall을 생성
		`why ?) JdbcInsert, JdbcCall은 DAO마다 다른 오브젝트를 갖는 경향이 있다.`
	- JdbcDaoSupport 제공

### ibatis ( mybatis )

- SQL Mapper
- SQL을 별도의 파일로 분리 가능하다.
- 오브젝트와 SQL간의 자동 매핑이 이루어진다.
- SqlMapClient 핵심 인터페이스
	- JDBC에서 필수로 사용되는 Connection, Statement, ResultSet과 동급 오브젝트
	- SqlMapClientBuilder로 생성 가능(팩토리빈)
- 공통 설정 XML과 매핑 정보 XML이 필요하다.
	- 공통 설정 XML
		- 데이터소스 => 스프링빈 이용 가능
		- 트랜잭션 => 스프링빈 이용 가능
		- 매핑리소스
		- 프로퍼티
		- 타입 별칭 핸들러
	- 매핑 정보 XML
		- SQL문
		- 자바 오브젝트
- 스프링에서는 SqlMapClient를 받아 사용할 수 있는 SqlMapClientTemplate를 제공
- SqlMapClientDaoSupport 제공

### JPA

- 영속성 관리와 O/R 매핑을 위한 표준 기술
- 오브젝트 중심인 언어에서의 불푠함 해소
- EntityManager 핵심 인터페이스
- EntityManagerFactory로 생성 가능(팩토리빈)
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
							↑↓              ↓
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
			- xslt 변환을 이용한 뷰 생성, Tiles 뷰 생성, 리포트 작성용 프레임워크인 JasperReports를 이용해 CSV, HTML, PDF, Excel 작성.
	
		- MappingJacksonJsonView
			- JSON 타입의 콘텐트를 작성해주는 뷰

	- 뷰 리졸버
		- 뷰 이름의 경우, 뷰 리졸버가 필요 ( 논리적 뷰 -> 실질적 뷰 오브젝트 )
		- ViewResolver 인터페이스를 구현해야 한다.
		- InternalResourceViewResolver
			- 뷰 리졸버를 지정하지 않았을 때, 자동등록되는 디폴트 뷰 리졸버