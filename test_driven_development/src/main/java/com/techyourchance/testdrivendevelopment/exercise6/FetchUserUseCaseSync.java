package com.techyourchance.testdrivendevelopment.exercise6;

import org.jetbrains.annotations.Nullable;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;

import java.util.Objects;

interface FetchUserUseCaseSync {

    enum Status {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    class UseCaseResult {
        private final Status mStatus;

        @Nullable
        private final User mUser;

        public UseCaseResult(Status status, @Nullable User user) {
            mStatus = status;
            mUser = user;
        }

        public Status getStatus() {
            return mStatus;
        }

        @Nullable
        public User getUser() {
            return mUser;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UseCaseResult)) return false;
            UseCaseResult result = (UseCaseResult) o;
            return mStatus == result.mStatus && Objects.equals(mUser, result.mUser);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mStatus, mUser);
        }
    }

    UseCaseResult fetchUserSync(String userId);

}
