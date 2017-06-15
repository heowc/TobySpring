package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

// JUnit이 테스트를 진행하는 중에 테스트에 사용할 어플리케이션 컨텍스트를 만들고 관리하는 작업을 진행
@RunWith(SpringJUnit4ClassRunner.class)
// 자동으로 만들어줄 어플리케이션 컨텍스트의 설정 파일 위치 지정
@ContextConfiguration(locations = "/test-context-datasource.xml")
// @DirtiesContext // 해당 레벨(클래스, 메소드)에 해당하는 컨텍스트 구성이나 상태를 변경
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Before
    public void before_init() throws SQLException {
        dao.createTable();
    }
    @Test
    public void test_addAndGet() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        User user = new User();
        user.setId("wonchul");
        user.setName("허원철");
        user.setPassword("1234");

        dao.add(user);
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user.getId());

        assertThat(user.getName(), is(user2.getName()));
        assertThat(user.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void test_count() throws SQLException {
        User user1 = new User("wonchul", "허원철", "1234");
        User user2 = new User("naeun", "전나은", "1234");
        User user3 = new User("toby", "이일민", "1234");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void test_getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id"); // Incorrect result size: expected 1, actual 0
    }
}