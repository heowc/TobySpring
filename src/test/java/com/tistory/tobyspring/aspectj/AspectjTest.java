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
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(" +
                "public int com.tistory.tobyspring.aop.pointcut.test.Target.minus(int, int) " +
                "throws java.lang.RuntimeException"
               + ")");

        assertThat(pointcut.getClassFilter().matches(Target.class) &&
                    pointcut.getMethodMatcher().matches(
                            Target.class.getMethod("minus", int.class, int.class), null
                    ),
                    is(true));

        assertThat(pointcut.getClassFilter().matches(Target.class) &&
                        pointcut.getMethodMatcher().matches(
                                Target.class.getMethod("plus", int.class, int.class), null
                        ),
                    is(false));

        assertThat(pointcut.getClassFilter().matches(Bean.class) &&
                        pointcut.getMethodMatcher().matches(
                                Target.class.getMethod("method"), null
                        ),
                    is(false));
    }
}
