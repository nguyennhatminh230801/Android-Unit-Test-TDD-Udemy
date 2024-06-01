package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.exercise4.testclass.FakeUserCache;
import com.techyourchance.testdoublesfundamentals.exercise4.testclass.FakeUserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FetchUserProfileUseCaseSyncTest {

    FetchUserProfileUseCaseSync fetchUserProfileUseCaseSync;

    FakeUserProfileHttpEndpointSync userProfileHttpEndpointSync;

    FakeUserCache mockUsersCache;

    @Before
    public void setUp() throws Exception {
        userProfileHttpEndpointSync = new FakeUserProfileHttpEndpointSync();
        mockUsersCache = new FakeUserCache();
        fetchUserProfileUseCaseSync = new FetchUserProfileUseCaseSync(userProfileHttpEndpointSync, mockUsersCache);
    }

    //Test case:
    //1. General Error -> Fail by General Error
    @Test
    public void testFetchUserProfileSync_WhenGeneralError_ReturnFails_UserNotCached() {
        //Given
        userProfileHttpEndpointSync.setUpGeneralErrorCase();

        //When
        FetchUserProfileUseCaseSync.UseCaseResult result =
                fetchUserProfileUseCaseSync.fetchUserProfileSync(FetchUserProfileUseCaseConstant.USER_ID);

        //Then
        Assert.assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE.name(), result.name());

        User user = fetchUserProfileUseCaseSync.getUsersCache().getUser(FetchUserProfileUseCaseConstant.USER_ID);

        Assert.assertNull(user);
    }

    //2. Network Error -> Throw Network Exception
    @Test
    public void testFetchUserProfileSync_WhenNetworkError_ReturnNetworkError_UserNotCached() {
        //Given
        userProfileHttpEndpointSync.setUpNetworkErrorCase();

        //When
        FetchUserProfileUseCaseSync.UseCaseResult result =
                fetchUserProfileUseCaseSync.fetchUserProfileSync(FetchUserProfileUseCaseConstant.USER_ID);

        //Then
        Assert.assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR.name(), result.name());

        User user = fetchUserProfileUseCaseSync.getUsersCache().getUser(FetchUserProfileUseCaseConstant.USER_ID);

        Assert.assertNull(user);
    }

    //3. Auth Error -> Fail by Auth Error
    @Test
    public void testFetchUserProfileSync_WhenAuthError_ReturnFails_UserNotCached() {
        //Given
        userProfileHttpEndpointSync.setUpAuthErrorCase();

        //When
        FetchUserProfileUseCaseSync.UseCaseResult result =
                fetchUserProfileUseCaseSync.fetchUserProfileSync(FetchUserProfileUseCaseConstant.USER_ID);

        //Then
        Assert.assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE.name(), result.name());

        User user = fetchUserProfileUseCaseSync.getUsersCache().getUser(FetchUserProfileUseCaseConstant.USER_ID);

        Assert.assertNull(user);
    }

    //4. Server Error -> Fail by Server Error
    @Test
    public void testFetchUserProfileSync_WhenServerError_ReturnFails_UserNotCached() {
        //Given
        userProfileHttpEndpointSync.setUpServerErrorCase();

        //When
        FetchUserProfileUseCaseSync.UseCaseResult result =
                fetchUserProfileUseCaseSync.fetchUserProfileSync(FetchUserProfileUseCaseConstant.USER_ID);

        //Then
        Assert.assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE.name(), result.name());

        User user = fetchUserProfileUseCaseSync.getUsersCache().getUser(FetchUserProfileUseCaseConstant.USER_ID);

        Assert.assertNull(user);
    }


    //6. Success, match user Id()
    @Test
    public void testFetchUserProfileSync_SuccessFetch_MatchUserId_ReturnTrue_UserCached() {
        //When
        FetchUserProfileUseCaseSync.UseCaseResult result =
                fetchUserProfileUseCaseSync.fetchUserProfileSync(FetchUserProfileUseCaseConstant.USER_ID);

        //Then
        Assert.assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS.name(), result.name());

        User user = fetchUserProfileUseCaseSync.getUsersCache().getUser(FetchUserProfileUseCaseConstant.USER_ID);

        Assert.assertNotNull(user);

        Assert.assertEquals(FetchUserProfileUseCaseConstant.USER_ID, user.getUserId());
        Assert.assertEquals(FetchUserProfileUseCaseConstant.FULL_NAME, user.getFullName());
        Assert.assertEquals(FetchUserProfileUseCaseConstant.IMAGE_URL, user.getImageUrl());
    }
}