package com.tistory.tobyspring.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserTest {

    private User user;

    @Before
    public void before_init() {
        user = new User();
    }

    @Test
    public void upgradeLevel() {
        Level [] levels = Level.values();

        for(Level level : levels) {
            if(level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel(), is(level.nextLevel()));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void notUpgradeLevel() {
        Level [] levels = Level.values();

        for(Level level : levels) {
            if(level.nextLevel() != null) continue;
            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}
