package com.tistory.tobyspring.context;

public class Hello {

    private String name;

    private Printer printer;

    public String sayHello() {
        return "Hello " + name;
    }

    public void print() {
        printer.print(sayHello());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
}
/*
    애노테이션을 이용해 빈의 의존관계를 정의하는 방법
    1. @Resource (수정자메소드, 필드)
     : 주입할 빈의 아이디를 찾아 지정하는 방법
        - <context:annotation-config /> : 빈 후처리기 등록
        - <context:component-scan /> : 빈 스캐닝 등록 ( 만들어지는 빈에 관계되는 빈과 함께 등록 )
        - AnnotationConfig(Web)ApplicationContext : 빈 스캐너와 애노테이션 의존 관계 정보를 읽는
                                                    후 처리기를 내장한 어플리케이션 컨텍스트 사용

    2. ★ @Autowired / @Inject
     : 타입에 의한 자동와이어링

        - @Autowired (생성자, 필드, 수정자메소드, 일반메소드)

    +) @Qualifier
     : 타입 외의 정보를 추가해서 자동와이어링을 세밀하게 제어할 수 있는 보조적인 방법

 */

/*
    메타정보 종류에 따른 값 설정 방법
    1. XML <property>
    2. @Value
        - 시스템 프로퍼티 ( #{systemProperties['os.name']} )
        - 어플리케이션 환경정보 프로퍼티 ( <context:property-placeholder location="classpath:~~"/>
                                        #{{key}}
                                    )
        - PropertyEditor, ConversionService라는 것으로 문자열이 자동 형변환이 된다.
        - <util:list>, <util:set>, <util:map>, <utils:properties>
    3. SpEL( Spring Expression Language )
        - 빈에 직접 접근할 수 있는 표현식

*/