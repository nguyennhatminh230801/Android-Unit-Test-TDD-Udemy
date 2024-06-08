package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSyncImpl;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCacheImpl;

public class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {

    private FetchUserHttpEndpointSyncImpl fetchUserHttpEndpointSync;
    private UsersCacheImpl usersCache;

    public FetchUserUseCaseSyncImpl() {
    }

    public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSyncImpl fetchUserHttpEndpointSync, UsersCacheImpl usersCache) {
        this.fetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        this.usersCache = usersCache;
    }

    /*
    *  The requirements:
        1) If the user with given user ID is not in the cache then it should be fetched from the server.
        2) If the user fetched from the server then it should be stored in the cache before returning to the caller.
        3) If the user is in the cache then cached record should be returned without polling the server.
    * */
    @Override
    public UseCaseResult fetchUserSync(String userId) {
        boolean isUsersExistInCached = usersCache.getUser(userId) != null;

        try {
            if (isUsersExistInCached) {
                return new FetchUserUseCaseSync.UseCaseResult(Status.SUCCESS, usersCache.getUser(userId));
            } else {
                FetchUserHttpEndpointSync.EndpointResult result = fetchUserHttpEndpointSync.fetchUserSync(userId);
                if (result.getStatus() == FetchUserHttpEndpointSync.EndpointStatus.SUCCESS) {
                    User user = new User(result.getUserId(), result.getUsername());
                    usersCache.cacheUser(user);
                    return new FetchUserUseCaseSync.UseCaseResult(Status.SUCCESS, user);
                } else {
                    return new FetchUserUseCaseSync.UseCaseResult(Status.FAILURE, null);
                }
            }
        } catch (NetworkErrorException ex) {
            return new FetchUserUseCaseSync.UseCaseResult(Status.NETWORK_ERROR, null);
        }
    }

}
