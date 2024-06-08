package com.techyourchance.testdrivendevelopment.exercise6;


import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSyncImpl;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCacheImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncImplTest {

    @Mock
    FetchUserHttpEndpointSyncImpl mockFetchUserHttpEndpointSync;

    @Mock
    UsersCacheImpl mockUsersCache;

    FetchUserUseCaseSyncImpl fetchUserUseCaseSync;

    @Before
    public void setUp() throws Exception {
        fetchUserUseCaseSync = Mockito.spy(new FetchUserUseCaseSyncImpl(mockFetchUserHttpEndpointSync, mockUsersCache));
    }

    /*
    *  The requirements:
        1) If the user with given user ID is not in the cache then it should be fetched from the server.
        2) If the user fetched from the server then it should be stored in the cache before returning to the caller.
        3) If the user is in the cache then cached record should be returned without polling the server.
    * */

    //Test case : user cached -> fetch user from cache -> succeed
    @Test
    public void testFetchUserSync_userCached_fetchUserFromCached_success() throws Exception {
        //Arrange
        Mockito.when(mockUsersCache.getUser(Mockito.anyString())).thenCallRealMethod();
        Mockito.doCallRealMethod().when(mockUsersCache).cacheUser(Mockito.any(User.class));
        Mockito.doCallRealMethod().when(mockUsersCache).setInitialList(Mockito.<User>anyList());

        FetchUserHttpEndpointSync.EndpointResult successEndpointResult = new FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.SUCCESS,
                Ex6Constant.INPUT_MOCK_USER_ID,
                Ex6Constant.INPUT_MOCK_USER_NAME
        );

        Mockito.when(mockFetchUserHttpEndpointSync.fetchUserSync(Mockito.anyString())).thenReturn(successEndpointResult);

        //Init cached
        User defaultCachedUser = new User(Ex6Constant.INPUT_MOCK_USER_ID, Ex6Constant.INPUT_MOCK_USER_NAME);
        mockUsersCache.setInitialList(Collections.singletonList(defaultCachedUser));

        Mockito.when(fetchUserUseCaseSync.fetchUserSync(Mockito.anyString())).thenCallRealMethod();


        //Act
        FetchUserUseCaseSync.UseCaseResult result = fetchUserUseCaseSync.fetchUserSync(Ex6Constant.INPUT_MOCK_USER_ID);

        //Assert
        FetchUserUseCaseSync.UseCaseResult successResult = new FetchUserUseCaseSync.UseCaseResult(
                FetchUserUseCaseSync.Status.SUCCESS,
                defaultCachedUser
        );

        Assert.assertEquals(successResult, result);

        Mockito.verify(mockUsersCache, Mockito.atLeastOnce()).getUser(Mockito.anyString());
        Assert.assertNotNull(mockUsersCache.getUser(Ex6Constant.INPUT_MOCK_USER_ID));
    }

    //Test case : user not cached -> fetch user from network -> store to cache -> succeed
    @Test
    public void testFetchUserSync_userNotCached_fetchUserFromCached_success() throws Exception {
        //Arrange
        Mockito.doCallRealMethod().when(mockUsersCache).setInitialList(Mockito.<User>anyList());
        Mockito.when(mockUsersCache.getUser(Mockito.anyString())).thenCallRealMethod();
        Mockito.doCallRealMethod().when(mockUsersCache).cacheUser(Mockito.any(User.class));

        FetchUserHttpEndpointSync.EndpointResult successEndpointResult = new FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.SUCCESS,
                Ex6Constant.INPUT_MOCK_USER_ID,
                Ex6Constant.INPUT_MOCK_USER_NAME
        );

        mockUsersCache.setInitialList(Collections.<User>emptyList());

        Mockito.when(mockFetchUserHttpEndpointSync.fetchUserSync(Mockito.anyString())).thenReturn(successEndpointResult);
        Mockito.when(fetchUserUseCaseSync.fetchUserSync(Mockito.anyString())).thenCallRealMethod();

        //Act

        FetchUserUseCaseSync.UseCaseResult result = fetchUserUseCaseSync.fetchUserSync(Ex6Constant.INPUT_MOCK_USER_ID);

        //Assert
        //Mockito.verify(mockUsersCache, Mockito.times(1)).cacheUser(Mockito.any(User.class));

        User defaultUser = new User(Ex6Constant.INPUT_MOCK_USER_ID, Ex6Constant.INPUT_MOCK_USER_NAME);
        FetchUserUseCaseSync.UseCaseResult successResult = new FetchUserUseCaseSync.UseCaseResult(
                FetchUserUseCaseSync.Status.SUCCESS,
                defaultUser
        );

        Assert.assertEquals(successResult, result);
        Assert.assertNotNull(mockUsersCache.getUser(Ex6Constant.INPUT_MOCK_USER_ID));
    }

    //Test case : user not cached -> fetch user from network -> auth error -> not store to cached -> failure
    @Test
    public void testFetchUserSync_userNotCached_fetchUserFromCached_authError() throws Exception {
        //Arrange
        Mockito.doCallRealMethod().when(mockUsersCache).cacheUser(Mockito.any(User.class));
        Mockito.when(mockUsersCache.getUser(Mockito.anyString())).thenCallRealMethod();
        FetchUserHttpEndpointSync.EndpointResult authErrorResult = new FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR,
                "",
                ""
        );

        Mockito.when(mockFetchUserHttpEndpointSync.fetchUserSync(Mockito.anyString())).thenReturn(authErrorResult);

        Mockito.when(fetchUserUseCaseSync.fetchUserSync(Mockito.anyString())).thenCallRealMethod();

        //Act
        FetchUserUseCaseSync.UseCaseResult result = fetchUserUseCaseSync.fetchUserSync(Ex6Constant.INPUT_MOCK_USER_ID);

        //Assert
        FetchUserUseCaseSync.UseCaseResult successResult = new FetchUserUseCaseSync.UseCaseResult(
                FetchUserUseCaseSync.Status.FAILURE,
                null
        );

        Assert.assertEquals(successResult, result);
        Mockito.verify(mockUsersCache, Mockito.never()).cacheUser(null);
    }
    //Test case : user not cached -> fetch user from network -> general error -> not store to cached -> failure
    @Test
    public void testFetchUserSync_userNotCached_fetchUserFromCached_generalError() throws Exception {
        //Arrange
        Mockito.when(mockUsersCache.getUser(Mockito.anyString())).thenCallRealMethod();
        Mockito.doCallRealMethod().when(mockUsersCache).cacheUser(Mockito.any(User.class));
        FetchUserHttpEndpointSync.EndpointResult generalErrorResult = new FetchUserHttpEndpointSync.EndpointResult(
                FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR,
                "",
                ""
        );

        Mockito.when(mockFetchUserHttpEndpointSync.fetchUserSync(Mockito.anyString())).thenReturn(generalErrorResult);

        Mockito.when(fetchUserUseCaseSync.fetchUserSync(Mockito.anyString())).thenCallRealMethod();

        //Act
        FetchUserUseCaseSync.UseCaseResult result = fetchUserUseCaseSync.fetchUserSync(Ex6Constant.INPUT_MOCK_USER_ID);

        //Assert

        FetchUserUseCaseSync.UseCaseResult successResult = new FetchUserUseCaseSync.UseCaseResult(
                FetchUserUseCaseSync.Status.FAILURE,
                null
        );

        Assert.assertEquals(successResult, result);
        Mockito.verify(mockUsersCache, Mockito.never()).cacheUser(null);
    }

    //Test case : user not cached -> fetch user from network -> not store to cached -> network exception
    @Test
    public void testFetchUserSync_userNotCached_fetchUserFromCached_networkException() throws Exception {
        //Arrange
        Mockito.when(mockUsersCache.getUser(Mockito.anyString())).thenCallRealMethod();
        Mockito.doCallRealMethod().when(mockUsersCache).cacheUser(Mockito.any(User.class));
        Mockito.when(mockFetchUserHttpEndpointSync.fetchUserSync(Mockito.anyString())).thenThrow(new NetworkErrorException());
        Mockito.when(fetchUserUseCaseSync.fetchUserSync(Mockito.anyString())).thenCallRealMethod();

        //Act
        FetchUserUseCaseSync.UseCaseResult result = fetchUserUseCaseSync.fetchUserSync(Ex6Constant.INPUT_MOCK_USER_ID);

        //Assert

        FetchUserUseCaseSync.UseCaseResult successResult = new FetchUserUseCaseSync.UseCaseResult(
                FetchUserUseCaseSync.Status.NETWORK_ERROR,
                null
        );

        Assert.assertEquals(successResult, result);
        Mockito.verify(mockUsersCache, Mockito.never()).cacheUser(null);
    }
}