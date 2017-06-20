package com.tistory.tobyspring.service.test;

import com.tistory.tobyspring.dao.UserDao;
import com.tistory.tobyspring.domain.Level;
import com.tistory.tobyspring.domain.User;
import com.tistory.tobyspring.service.UserLevelUpgradePolicy;
import com.tistory.tobyspring.service.UserService;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

/**
 * Service <BR>
 * Test 유저 비즈니스 로직 처리 클래스 <BR>
 */
public class TestUserService implements UserService {

    private DataSource dataSource;
    private UserDao userDao;
    private UserLevelUpgradePolicy userLevelUpgradePolicy;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    @Override
    public void upgradeLevels() throws Exception {
        TransactionSynchronizationManager.initSynchronization();    // 트랜잭션 동기화 초기화
        Connection c = DataSourceUtils.getConnection(dataSource);   // DB Connection 생성
        c.setAutoCommit(false);                                     // 트랜잭션 시작

        try {
            List<User> userList = userDao.getAll();

            for (User user : userList) {
                if (userLevelUpgradePolicy.isUpgradeLevel(user)) {
                    userLevelUpgradePolicy.upgradeLevel(user);
                }
            }

            c.commit(); // 트랜잭션 커밋
        } catch (Exception e) {
            c.rollback(); // 트랜잭션 롤백
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(c, dataSource);               // DB Connection 닫음
            TransactionSynchronizationManager.unbindResource(dataSource);   // 트랜잭션 동기화 닫기
            TransactionSynchronizationManager.clearSynchronization();       // 트랜잭션 동기화 종료
        }
    }

    @Override
    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
