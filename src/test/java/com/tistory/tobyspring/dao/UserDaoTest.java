package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {

    public static void main(String [] args) throws SQLException, ClassNotFoundException {
        /*
            IoC 컨테이너(어플리케이션 컨텍스트)
            1. 클라이언트는 구체적인 팩토리 클래스를 알 필요가 없다.
            2. 종합 IoC 서비스 제공(자동 생성, 후처리, 정보의 조합, 설정 방식의 다변화, 인터셉팅)
            3. 빈 검색
         */
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);
        dao.createTable();

        /* ============================================================================ */

        User user = new User();
        user.setId("wonchul");
        user.setName("허원철");
        user.setPassword("1234");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
