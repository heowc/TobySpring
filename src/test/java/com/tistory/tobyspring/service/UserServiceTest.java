package com.tistory.tobyspring.service;

import com.tistory.tobyspring.dao.UserDao;
import com.tistory.tobyspring.domain.Level;
import com.tistory.tobyspring.domain.User;
import com.tistory.tobyspring.exception.TestUserLevelUpgradePolicyException;
import com.tistory.tobyspring.service.impl.SimpleUserLevelUpgradePolicy;
import com.tistory.tobyspring.service.impl.SimpleUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
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
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-datasource-context.xml",
                                    "/aop-context.xml",
                                    "/transaction-context.xml"})
public class UserServiceTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserService userService;

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

    /**
     * * 인터페이스를 이용하여 생성 <BR>
     * * 리턴할 값이 있으면 이를 지정해준다. 또한 예외를 강제로 던질 수도 있다. <BR>
     * * 테스트 대상 오브젝트에 DI를 이용하여 Mock 오브젝트를 사용하도록 한다. <BR>
     * * 특정 메소드가 호출 됐는지, 어떤 값을 가지고 몇번 호출 됐는지 검증한다. <BR>
     */
    @Test
    public void test_upgradeLevels() throws Exception {
        SimpleUserService userService = new SimpleUserService();
        SimpleUserLevelUpgradePolicy userLevelUpgradePolicy = new SimpleUserLevelUpgradePolicy();

        // Mock UserDao 생성
        UserDao mockUserDao = mock(UserDao.class);
        // getAll()에 스텁 기능 추가
        when(mockUserDao.getAll()).thenReturn(userList);

        // Mock MailSender 생성
        MailSender mockMailSender = mock(MailSender.class);
        userLevelUpgradePolicy.setMailSender(mockMailSender);
        userLevelUpgradePolicy.setUserDao(mockUserDao);

        userService.setUserDao(mockUserDao);
        userService.setUserLevelUpgradePolicy(userLevelUpgradePolicy);

        userService.upgradeLevels();

        // update(User user) 2번 호출하는지 확인
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(userList.get(1));
        assertThat(userList.get(1).getLevel(), is(Level.SILVER));
        verify(mockUserDao).update(userList.get(3));
        assertThat(userList.get(3).getLevel(), is(Level.GOLD));

        // ArgumentCaptor를 이용하여 실제 MailSender Mock 오브젝트에 전달된 파라미터를 가져와 검증
        // 파라미터를 직접 비교하기보다는 파라미터의 내부 정보를 확인해야 하는 경우
        ArgumentCaptor<SimpleMailMessage> mailMessageArg
                = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessageList = mailMessageArg.getAllValues();
        assertThat(mailMessageList.get(0).getTo()[0], is(userList.get(1).getEmail()));
        assertThat(mailMessageList.get(1).getTo()[0], is(userList.get(3).getEmail()));
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

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

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
            userService.upgradeLevels();
            fail("TestUserLevelUpgradePolicyException expected");
        } catch (TestUserLevelUpgradePolicyException e) {
        }

        checkLevelUpgraded(userList.get(1), false);
    }

    // JDBC 드라이버에 따라 다르다.
    @Test
    public void test_readOnlyTransactionGetAll() {
        userService.getAll();
    }
}