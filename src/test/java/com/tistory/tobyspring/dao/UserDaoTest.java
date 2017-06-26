package com.tistory.tobyspring.dao;

import com.tistory.tobyspring.domain.Level;
import com.tistory.tobyspring.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;

// JUnit이 테스트를 진행하는 중에 테스트에 사용할 어플리케이션 컨텍스트를 만들고 관리하는 작업을 진행
@RunWith(SpringJUnit4ClassRunner.class)
// 자동으로 만들어줄 어플리케이션 컨텍스트의 설정 파일 위치 지정
@ContextConfiguration(locations = "/test-datasource-context.xml")
// @DirtiesContext // 해당 레벨(클래스, 메소드)에 해당하는 컨텍스트 구성이나 상태를 변경
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void before_init() throws SQLException {
        dao.createTable();

        user1 = new User("wonchul", "허원철", "1234", Level.BASIC, 1, 0, "heowc1992@gmail.com");
        user2 = new User("naeun", "전나은", "1234", Level.SILVER, 55, 10, "heowc1992@gmail.com");
        user3 = new User("toby", "이일민", "1234", Level.GOLD, 100, 40, "heowc1992@gmail.com");
    }

    @Test
    public void test_addAndGet() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        user2 = dao.get(user1.getId());

        checkSameUser(user1, user2);
    }

    @Test
    public void test_count() throws SQLException {
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

    @Test
    public void test_getAll() throws SQLException {
        dao.deleteAll();

        dao.add(user1);
        List<User> userList1 = dao.getAll();
        assertThat(userList1.size(), is(1));
        checkSameUser(user1, userList1.get(0));

        dao.add(user2);
        List<User> userList2 = dao.getAll();
        assertThat(userList2.size(), is(2));
        checkSameUser(user1, userList2.get(0));
        checkSameUser(user2, userList2.get(1));

        dao.add(user3);
        List<User> userList3 = dao.getAll();
        assertThat(userList3.size(), is(3));
        checkSameUser(user1, userList3.get(0));
        checkSameUser(user2, userList3.get(1));
        checkSameUser(user3, userList3.get(2));
    }

    private void checkSameUser(User user1, User user2) throws SQLException {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLoginCount(), is(user2.getLoginCount()));
        assertThat(user1.getRecommendCount(), is(user2.getRecommendCount()));
        assertThat(user1.getEmail(), is(user2.getEmail()));
    }

    @Test(expected = DataAccessException.class)
    public void test_duplicateKey() throws SQLException {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user1);
    }

    @Test
    public void test_sqlExceptionTranslate() {
        dao.deleteAll();

        try {
            dao.add(user1);
            dao.add(user1);
        } catch (DuplicateKeyException e) {
            SQLException sqlEx = (SQLException) e.getRootCause();
            SQLExceptionTranslator translator =
                    new SQLErrorCodeSQLExceptionTranslator(dataSource);

            assertThat(translator.translate(null, null, sqlEx),
                        isA(DataAccessException.class));
        }
    }

    @Test
    public void test_update() throws SQLException {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);

        user1.setName("HeoWonChul");
        user1.setPassword("1111");
        user1.setLevel(Level.GOLD);
        user1.setLoginCount(123);
        user1.setRecommendCount(50);
        dao.update(user1);

        User user1Update = dao.get(user1.getId());
        checkSameUser(user1, user1Update);
        User user2Same = dao.get(user2.getId());
        checkSameUser(user2, user2Same);
    }
}