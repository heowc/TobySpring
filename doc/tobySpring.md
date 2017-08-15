 
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
          서블릿에서 DispatcherServlet 이라는 것을 제공하여 가능하다. (web.xml에 등록)
          

### 특징

- ApplicationContext는 계층 구조를 갖을 수 있다.
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
   - XML을 이용한 빈 스캐너 등록 ( 빈 스캐너를 내장한 애플리케이션 컨텍스트 사용 )
   
      ```xml
      <context:component-scan base-package="com.tistory.tobyspring" />
      ```

3. @Configuration 클래스의 @Bean 메소드
   - 컴파일러나 IDE를 통한 타입 검증이 가능하다.
   - 이해하기 쉽다.
   
#### Scope

- @Scope나 xml 속성값 scope 변경 가능하다.
- 기본적으로 singleton이다. ( singleton, prototyep, request, session, globalsession )
- prototype은 오브젝트 중심 아키텍처에 적합(?)할 수 있다.
- session은 DL 방식으로는 Provider나 ObjectFactory에 넣어두고 사용할 수 있다. DI 방식으로는 프록시 설정을 변경하여 사용할 수 있다.

#### Meta 정보

- 식별자 ( id, name을 이용 )
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
     why ?) JdbcInsert, JdbcCall은 DAO마다 다른 오브젝트를 갖는 경향이 있다.
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
                         ↑↓               ↓
      HTTP 응답 ←        View            Model
   ```
      
