package com.techyourchance.testdoublesfundamentals.exercise4.testclass;

import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class FakeUserCache implements UsersCache {

    private ArrayList<User> users = new ArrayList<>(1);

    @Override
    public void cacheUser(User user) {
        users.add(user);
    }

    @Nullable
    @Override
    public User getUser(String userId) {
        User matchUser = null;

        for (User user: users) {
            if (user.getUserId().equals(userId)) {
                matchUser = user;
                break;
            }
        }

        return matchUser;
    }
}
