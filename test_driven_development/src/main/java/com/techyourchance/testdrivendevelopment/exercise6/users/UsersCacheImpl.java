package com.techyourchance.testdrivendevelopment.exercise6.users;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UsersCacheImpl implements UsersCache {

    List<User> cachedUsers;

    public UsersCacheImpl() {
        this.cachedUsers = new ArrayList<>();
    }

    public void setInitialList(List<User> initialList) {
        this.cachedUsers = new ArrayList<>();
        this.cachedUsers.addAll(initialList);
    }

    @Override
    public void cacheUser(User user) {
        boolean isExists = false;

        for (User user1: cachedUsers) {
            if (user1.getUserId().equals(user.getUserId())) {
                isExists = true;
                break;
            }
        }

        if (!isExists) {
            cachedUsers.add(user);
        }
    }

    @Nullable
    @Override
    public User getUser(String userId) {
        try {
            for (User user1: cachedUsers) {
                if (user1.getUserId().equals(userId)) {
                    return user1;
                }
            }

            return null;
        } catch (Exception ex) {
            return null;
        }
    }
}
