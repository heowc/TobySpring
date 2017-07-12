package com.tistory.tobyspring.service;

import com.tistory.tobyspring.config.ApplicationContext;
import com.tistory.tobyspring.dao.UserDao;
import com.tistory.tobyspring.domain.Level;
import com.tistory.tobyspring.domain.User;
import com.tistory.tobyspring.exception.TestUserLevelUpgradePolicyException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

import static com.tistory.tobyspring.service.impl.SimpleUserLevelUpgradePolicy.MIN_LOGINCOUNT_FOR_SLIVER;
import static com.tistory.tobyspring.service.impl.SimpleUserLevelUpgradePolicy.MIN_RECOMMENDCOUNT_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContext.class)
@ActiveProfiles("test")
//@Transactional
//@TransactionConfiguration(defaultRollback = false) // 클래스레벨에서의 롤백
public class UserServiceTest {

    @Autowired
    private UserService testUserService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private UserDao userDao;

    private List<User> userList;

    @Before
    public void before_init() {
        userDao.createTable();

        userList = Arrays.asList(
            new User("bunjin", "박범진", "p1", Level.BASIC, MIN_LOGINCOUNT_FOR_SLIVER-1, 8, "heowc1992@gmail.com"),
            new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGINCOUNT_FOR_SLIVER, 8, "heowc1992@gmail.com"),
            new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMENDCOUNT_FOR_GOLD-1, "heowc1992@gmail.com"),
            new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMENDCOUNT_FOR_GOLD, "heowc1992@gmail.com"),
            new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "heowc1992@gmail.com")
        );
    }

    private void checkLevelUpgraded(User user, boolean isUpgraded) {
        User userUpdate = userDao.get(user.getId());
        if (isUpgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    @Test
    public void test_add() {
        userDao.deleteAll();

        User userWithLevel = userList.get(4);       // GOLD LEVEL
        User userWithoutLevel = userList.get(0);
        userWithoutLevel.setLevel(null);            // NULL LEVEL

        testUserService.add(userWithLevel);
        testUserService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }

    /**
     * 트랜잭션 문제 발생 </BR>
     * <pre>
     *     Connection c = dataSource.getConnection();
     *
     *     c.setAutoCommit(false); // 트랜잭션 시작
     *     try {
     *         PrepareStatement st1 =
     *              c.prepareStatement("update users ...");
     *         st1.executeUpdate();
     *
     *         PrepareStatement st2 =
     *              c.prepareStatement("delete users ...");
     *         st2.executeUpdate();
     *
     *         c.commit(); // 트랜잭션 커밋
     *     } catch(Exception e) {
     *         c.rollback(); // 트랜잭션 롤백
     *     }
     *
     *     c.close();
     * </pre>
     *
     * => 트랜잭션 동기화 방식
     * (트랜잭션을 시작하기 위해 만든 Connection을 특별한 저장소에 보관해두고, 이후에 저장된 Connection을 가져다가 사용하는 것)
     */
    @Test
    public void test_upgradeAllOrNothing() throws Exception {
        userDao.deleteAll();

        for (User user: userList) {
            userDao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("TestUserLevelUpgradePolicyException expected");
        } catch (TestUserLevelUpgradePolicyException e) {
        }

        checkLevelUpgraded(userList.get(1), false);
    }

    // JDBC 드라이버에 따라 다르다.
    @Test
    public void test_readOnlyTransactionGetAll() {
        testUserService.getAll();
    }

    @Test
//    @Transactional
//    @Rollback(false)  // 테스트 환경에서는 자동 롤백이 되기 때문에,
                        // 메소드 레벨에서 데이터를 반영하고 싶다면 value값을 false로 수정
    public void test_transactionSync() {
//        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
//        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);
//        txDefinition.setReadOnly(true);

        testUserService.deleteAll();

        testUserService.add(userList.get(0));
        testUserService.add(userList.get(1));

//        transactionManager.commit(txStatus);
    }
}