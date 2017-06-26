package com.tistory.tobyspring.aspectj;

import com.tistory.tobyspring.aop.pointcut.test.Bean;
import com.tistory.tobyspring.aop.pointcut.test.Target;
import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * 포인트컷 표현식 </br>
 * AspectJExpressionPointcut 이용 </br>
 *
 * excution([접근제한자 패턴] 타입 패턴 [패키지와 클래스 이름 패턴.]이름 패턴 (파라미터 패턴 | "..", ...) [throws 예외 패턴])
 */
public class AspectjTest {

    @Test
    public void test_methodSignaturePointcut() throws Exception {
        String expression =
                "execution(" +
                        "public int com.tistory.tobyspring.aop.pointcut.test.Target.minus(int, int) " +
                        "throws java.lang.RuntimeException"
                        + ")";

        pointcutMatches(expression, true, Target.class, "minus", int.class, int.class);
        pointcutMatches(expression, false, Target.class, "plus", int.class, int.class);
        pointcutMatches(expression, false, Bean.class, "method");
    }

    @Test
    public void test_targetClassPointcutMatches() throws Exception {
        String expression =
                "execution(" +
                        "* *(..)" +
                         ")";

        pointcutMatches(expression, true, Target.class, "hello");
        pointcutMatches(expression, true, Target.class, "hello", String.class);
        pointcutMatches(expression, true, Target.class, "plus", int.class, int.class);
        pointcutMatches(expression, true, Target.class, "minus", int.class, int.class);
        pointcutMatches(expression, true, Target.class, "method");
        pointcutMatches(expression, true, Bean.class, "method");
    }

    private void pointcutMatches(String expression, Boolean isExpected, Class<?> clazz,
                                 String methodName, Class<?>... args) throws Exception {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);


        assertThat(pointcut.getClassFilter().matches(clazz) &&
                        pointcut.getMethodMatcher().matches(
                                clazz.getMethod(methodName, args), null
                        ),
                    is(isExpected));
    }
}
