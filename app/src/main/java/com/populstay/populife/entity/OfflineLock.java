package com.populstay.populife.entity;

public class OfflineLock {
    private String userId;
    private String lockMac;
    private String lockName;
    private String lockDataJson;
    private int lockType;

    public OfflineLock() {
    }

    public OfflineLock(String userId, String lockMac, String lockName, String lockDataJson, int lockType) {
        this.userId = userId;
        this.lockMac = lockMac;
        this.lockName = lockName;
        this.lockDataJson = lockDataJson;
        this.lockType = lockType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLockMac() {
        return lockMac;
    }

    public void setLockMac(String lockMac) {
        this.lockMac = lockMac;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getLockDataJson() {
        return lockDataJson;
    }

    public void setLockDataJson(String lockDataJson) {
        this.lockDataJson = lockDataJson;
    }

    public int getLockType() {
        return lockType;
    }

    public void setLockType(int lockType) {
        this.lockType = lockType;
    }
}

