package com.tistory.tobyspring.service.impl;

import com.tistory.tobyspring.domain.User;
import com.tistory.tobyspring.service.UserService;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Service <BR>
 * 트랜잭션 분리 <BR>
 */
public class TransactionUserService implements UserService {

    private PlatformTransactionManager transactionManager;
    private UserService userService;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void upgradeLevels() {
        TransactionStatus status =
                transactionManager.getTransaction(new DefaultTransactionDefinition()); // 트랜잭션 시작

        try {
            userService.upgradeLevels();

            transactionManager.commit(status); // 트랜잭션 커밋
        } catch (RuntimeException e) {
            transactionManager.rollback(status); // 트랜잭션 롤백
            throw e;
        }
    }

    @Override
    public void add(User user) {
        userService.add(user);
    }
}
