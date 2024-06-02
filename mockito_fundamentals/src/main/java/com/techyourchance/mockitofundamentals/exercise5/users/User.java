package com.techyourchance.mockitofundamentals.exercise5.users;

import java.util.Objects;

public class User {
    private final String mUserId;
    private final String mUsername;

    public User(String userId, String username) {
        mUserId = userId;
        mUsername = username;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getUsername() {
        return mUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(mUserId, user.mUserId) && Objects.equals(mUsername, user.mUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUserId, mUsername);
    }
}
