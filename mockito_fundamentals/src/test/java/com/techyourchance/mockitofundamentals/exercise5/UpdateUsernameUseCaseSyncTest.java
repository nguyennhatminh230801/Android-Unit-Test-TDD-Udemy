package com.techyourchance.mockitofundamentals.exercise5;


import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.testclass.Ex5Constants;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;


/*
 *  Spec:
 *
 *  UseCaseResult:
 *  SUCCESS - success update username
 *  FAILURE - fail update username
 *  NETWORK_ERROR - network error case
 *
 *  EndpointResultStatus - UpdateUsernameHttpEndpointSync:
 *  SUCCESS - success update username
 *  AUTH_ERROR - fail by auth error
 *  SERVER_ERROR - fail by server error
 *  GENERAL_ERROR - fail by general error
 *
 *  Test Case:
 *  1 - EndpointResultStatus = SUCCESS -> UseCaseResult = SUCCESS, and cached user
 *  2 - EndpointResultStatus = GENERAL_ERROR -> UseCaseResult = FAILURE, and not cached user
 *  3 - EndpointResultStatus = AUTH_ERROR -> UseCaseResult = FAILURE, and not cached user
 *  4 - EndpointResultStatus = SERVER_ERROR -> UseCaseResult = FAILURE, and not cached user
 *  5 - UseCaseResult = NETWORK_ERROR, throw Exception and not cached user
 * */

public class UpdateUsernameUseCaseSyncTest {

    UpdateUsernameUseCaseSync updateUsernameUseCaseSyncMock;
    UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSyncMock;
    UsersCache usersCacheMock;
    EventBusPoster eventBusPosterMock;

    @Before
    public void setUp() throws Exception {
        updateUsernameHttpEndpointSyncMock = Mockito.mock(UpdateUsernameHttpEndpointSync.class);
        usersCacheMock = Mockito.mock(UsersCache.class);
        eventBusPosterMock = Mockito.mock(EventBusPoster.class);
        updateUsernameUseCaseSyncMock = new UpdateUsernameUseCaseSync(
                updateUsernameHttpEndpointSyncMock,
                usersCacheMock,
                eventBusPosterMock
        );
    }

    //  1 - EndpointResultStatus = SUCCESS -> UseCaseResult = SUCCESS, and cached user
    @Test
    public void testUpdateUsernameSync_ReturnSuccess_CachedUser() throws Exception {
        //GIVEN
        ArgumentCaptor<String> stringArgsCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Object> objectArgumentCaptor = ArgumentCaptor.forClass(Object.class);

        UpdateUsernameHttpEndpointSync.EndpointResult resultSucceed = new UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS,
                Ex5Constants.INPUT_USER_NAME,
                Ex5Constants.INPUT_PASS_WORD
        );

        //Setup inner function returns success result
        Mockito.when(updateUsernameHttpEndpointSyncMock.updateUsername(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(resultSucceed);

        //WHEN
        UpdateUsernameUseCaseSync.UseCaseResult actualUseCaseResult = updateUsernameUseCaseSyncMock.updateUsernameSync(
                Ex5Constants.INPUT_USER_NAME,
                Ex5Constants.INPUT_PASS_WORD
        );

        //THEN
        //Verify use case result is succeed
        Assert.assertEquals(UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS, actualUseCaseResult);

        //Verify run updateUsername() at least Once
        Mockito.verify(updateUsernameHttpEndpointSyncMock, Mockito.times(1))
                .updateUsername(stringArgsCapture.capture(), stringArgsCapture.capture());

        //Verify usersCacheMock.cacheUser() run at least once
        Mockito.verify(usersCacheMock, Mockito.times(1))
                .cacheUser(userArgumentCaptor.capture());

        //Verify usersCacheMock.postEvent() run at least once
        Mockito.verify(eventBusPosterMock, Mockito.times(1))
                .postEvent(objectArgumentCaptor.capture());

        //Verify arguments pass is correct
        List<String> captures = stringArgsCapture.getAllValues();

        Assert.assertEquals(Ex5Constants.INPUT_USER_NAME, captures.get(0));
        Assert.assertEquals(Ex5Constants.INPUT_PASS_WORD, captures.get(1));

        //Verify user is inserted correct
        List<User> userCaptures = userArgumentCaptor.getAllValues();
        User expectedUser = new User(Ex5Constants.INPUT_USER_NAME, Ex5Constants.INPUT_PASS_WORD);
        Assert.assertEquals(expectedUser, userCaptures.get(0));

        //Verify postEvent is UserDetailsChangedEvent
        List<Object> eventBusPosterCaptured = objectArgumentCaptor.getAllValues();
        boolean isUserDetailsChangedEvent = eventBusPosterCaptured.get(0) instanceof UserDetailsChangedEvent;
        Assert.assertTrue(isUserDetailsChangedEvent);
    }

    //  2 - EndpointResultStatus = GENERAL_ERROR -> UseCaseResult = FAILURE, and not cached user
    @Test
    public void testUpdateUsernameSync_GeneralError_ReturnFailed_NotCachedUser() throws Exception {
        //GIVEN
        ArgumentCaptor<String> stringArgsCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Object> objectArgumentCaptor = ArgumentCaptor.forClass(Object.class);

        UpdateUsernameHttpEndpointSync.EndpointResult resultFailed = new UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                Ex5Constants.WRONG_USER_NAME,
                Ex5Constants.WRONG_PASS_WORD
        );

        //Setup inner function returns failed by general error result
        Mockito.when(updateUsernameHttpEndpointSyncMock.updateUsername(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(resultFailed);

        //WHEN
        UpdateUsernameUseCaseSync.UseCaseResult actualUseCaseResult = updateUsernameUseCaseSyncMock.updateUsernameSync(
                Ex5Constants.INPUT_USER_NAME,
                Ex5Constants.INPUT_PASS_WORD
        );

        //THEN
        //Verify use case result is succeed
        Assert.assertEquals(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE, actualUseCaseResult);

        //Verify run updateUsername() at least once
        Mockito.verify(updateUsernameHttpEndpointSyncMock, Mockito.times(1))
                .updateUsername(stringArgsCapture.capture(), stringArgsCapture.capture());

        //Verify never usersCacheMock.cacheUser()
        Mockito.verify(usersCacheMock, Mockito.never())
                .cacheUser(userArgumentCaptor.capture());

        //Verify usersCacheMock.postEvent() run at least once
        Mockito.verify(eventBusPosterMock, Mockito.never())
                .postEvent(objectArgumentCaptor.capture());

        //Verify arguments pass is correct
        List<String> captures = stringArgsCapture.getAllValues();

        Assert.assertEquals(Ex5Constants.INPUT_USER_NAME, captures.get(0));
        Assert.assertEquals(Ex5Constants.INPUT_PASS_WORD, captures.get(1));
    }

    //  3 - EndpointResultStatus = AUTH_ERROR -> UseCaseResult = FAILURE, and not cached user
    @Test
    public void testUpdateUsernameSync_AuthError_ReturnFailed_NotCachedUser() throws Exception {
        //GIVEN
        ArgumentCaptor<String> stringArgsCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Object> objectArgumentCaptor = ArgumentCaptor.forClass(Object.class);

        UpdateUsernameHttpEndpointSync.EndpointResult resultFailed = new UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                Ex5Constants.WRONG_USER_NAME,
                Ex5Constants.WRONG_PASS_WORD
        );

        //Setup inner function returns failed by general error result
        Mockito.when(updateUsernameHttpEndpointSyncMock.updateUsername(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(resultFailed);

        //WHEN
        UpdateUsernameUseCaseSync.UseCaseResult actualUseCaseResult = updateUsernameUseCaseSyncMock.updateUsernameSync(
                Ex5Constants.INPUT_USER_NAME,
                Ex5Constants.INPUT_PASS_WORD
        );

        //THEN
        //Verify use case result is succeed
        Assert.assertEquals(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE, actualUseCaseResult);

        //Verify run updateUsername() at least once
        Mockito.verify(updateUsernameHttpEndpointSyncMock, Mockito.times(1))
                .updateUsername(stringArgsCapture.capture(), stringArgsCapture.capture());

        //Verify never usersCacheMock.cacheUser()
        Mockito.verify(usersCacheMock, Mockito.never())
                .cacheUser(userArgumentCaptor.capture());

        //Verify never usersCacheMock.postEvent()
        Mockito.verify(eventBusPosterMock, Mockito.never())
                .postEvent(objectArgumentCaptor.capture());

        //Verify arguments pass is correct
        List<String> captures = stringArgsCapture.getAllValues();

        Assert.assertEquals(Ex5Constants.INPUT_USER_NAME, captures.get(0));
        Assert.assertEquals(Ex5Constants.INPUT_PASS_WORD, captures.get(1));
    }

    //  4 - EndpointResultStatus = SERVER_ERROR -> UseCaseResult = FAILURE, and not cached user
    @Test
    public void testUpdateUsernameSync_ServerError_ReturnFailed_NotCachedUser() throws Exception {
        //GIVEN
        ArgumentCaptor<String> stringArgsCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Object> objectArgumentCaptor = ArgumentCaptor.forClass(Object.class);

        UpdateUsernameHttpEndpointSync.EndpointResult resultFailed = new UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                Ex5Constants.WRONG_USER_NAME,
                Ex5Constants.WRONG_PASS_WORD
        );

        //Setup inner function returns failed by general error result
        Mockito.when(updateUsernameHttpEndpointSyncMock.updateUsername(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(resultFailed);

        //WHEN
        UpdateUsernameUseCaseSync.UseCaseResult actualUseCaseResult = updateUsernameUseCaseSyncMock.updateUsernameSync(
                Ex5Constants.INPUT_USER_NAME,
                Ex5Constants.INPUT_PASS_WORD
        );

        //THEN
        //Verify use case result is succeed
        Assert.assertEquals(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE, actualUseCaseResult);

        //Verify run updateUsername() at least once
        Mockito.verify(updateUsernameHttpEndpointSyncMock, Mockito.times(1))
                .updateUsername(stringArgsCapture.capture(), stringArgsCapture.capture());

        //Verify never usersCacheMock.cacheUser()
        Mockito.verify(usersCacheMock, Mockito.never())
                .cacheUser(userArgumentCaptor.capture());

        //Verify never run usersCacheMock.postEvent()
        Mockito.verify(eventBusPosterMock, Mockito.never())
                .postEvent(objectArgumentCaptor.capture());

        //Verify arguments pass is correct
        List<String> captures = stringArgsCapture.getAllValues();

        Assert.assertEquals(Ex5Constants.INPUT_USER_NAME, captures.get(0));
        Assert.assertEquals(Ex5Constants.INPUT_PASS_WORD, captures.get(1));
    }

    //  5 - UseCaseResult = NETWORK_ERROR, throw Exception and not cached user
    @Test
    public void testUpdateUsernameSync_NetworkError_ReturnNetworkError_NotCachedUser() throws Exception {
        //GIVEN
        ArgumentCaptor<String> stringArgsCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Object> objectArgumentCaptor = ArgumentCaptor.forClass(Object.class);

        //Setup inner function returns failed by general error result
        Mockito.when(updateUsernameHttpEndpointSyncMock.updateUsername(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new NetworkErrorException());

        //WHEN
        UpdateUsernameUseCaseSync.UseCaseResult actualUseCaseResult = updateUsernameUseCaseSyncMock.updateUsernameSync(
                Ex5Constants.INPUT_USER_NAME,
                Ex5Constants.INPUT_PASS_WORD
        );

        //THEN
        //Verify use case result is succeed
        Assert.assertEquals(UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR, actualUseCaseResult);

        //Verify run updateUsername() at least once
        Mockito.verify(updateUsernameHttpEndpointSyncMock, Mockito.times(1))
                .updateUsername(stringArgsCapture.capture(), stringArgsCapture.capture());

        //Verify never usersCacheMock.cacheUser()
        Mockito.verify(usersCacheMock, Mockito.never())
                .cacheUser(userArgumentCaptor.capture());

        //Verify never run usersCacheMock.postEvent()
        Mockito.verify(eventBusPosterMock, Mockito.never())
                .postEvent(objectArgumentCaptor.capture());

        //Verify arguments pass is correct
        List<String> captures = stringArgsCapture.getAllValues();

        Assert.assertEquals(Ex5Constants.INPUT_USER_NAME, captures.get(0));
        Assert.assertEquals(Ex5Constants.INPUT_PASS_WORD, captures.get(1));
    }
}