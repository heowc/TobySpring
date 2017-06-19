package com.tistory.tobyspring.domain;

/**
 * 사용자 정보를 저장하는 자바빈 규약을 따르는 오브젝트
 */
public class User {

    private String id;
    private String name;
    private String password;

    private Level level;
    private int loginCount;
    private int recommendCount;

    public User() {}

    public User(String id, String name, String password, Level level, int loginCount, int recommendCount) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.loginCount = loginCount;
        this.recommendCount = recommendCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public int getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(int recommendCount) {
        this.recommendCount = recommendCount;
    }

    public void upgradeLevel() {
        Level nextLevel = level.nextLevel();
        if (nextLevel != null) {
            level = nextLevel;
        } else {
            throw new IllegalStateException(level + "은 업그레이드가 불가능 합니다.");
        }
    }
}