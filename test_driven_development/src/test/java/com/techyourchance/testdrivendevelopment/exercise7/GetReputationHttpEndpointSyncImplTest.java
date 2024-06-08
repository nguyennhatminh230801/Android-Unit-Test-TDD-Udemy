package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSyncImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

/*
*  Spec:
*  1) If the server request completes successfully, then use case should indicate successful completion of the flow.
   2) If the server request completes successfully, then the fetched reputation should be returned
   3) If the server request fails for any reason, the use case should indicate that the flow failed.
   4) If the server request fails for any reason, the returned reputation should be 0.
* */

@RunWith(MockitoJUnitRunner.class)
public class GetReputationHttpEndpointSyncImplTest {

    @Mock
    GetReputationHttpEndpointSyncImpl mockGetReputationHttpEndpointSync;

    @Before
    public void setUp() throws Exception {
    }

    //Test case 1: EndpointStatus is success -> returned success with fetch reputation

    @Test
    public void testGetReputationSync_success() throws Exception {
        //Arrange
        Mockito.when(mockGetReputationHttpEndpointSync.getReputationSync()).thenReturn(
                new GetReputationHttpEndpointSync.EndpointResult(
                        GetReputationHttpEndpointSync.EndpointStatus.SUCCESS,
                        10
                )
        );

        //Act
        mockGetReputationHttpEndpointSync.getReputationSync();

        //Assert
        Mockito.verify(mockGetReputationHttpEndpointSync, Mockito.atLeastOnce())
                .getReputationSync();
    }

    //Test case 2: EndpointStatus is general error -> returned general error with reputation = 0
    @Test
    public void testGetReputationSync_generalError() throws Exception {
        //Arrange
        Mockito.when(mockGetReputationHttpEndpointSync.getReputationSync()).thenReturn(
                new GetReputationHttpEndpointSync.EndpointResult(
                        GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR,
                        0
                )
        );

        //Act
        mockGetReputationHttpEndpointSync.getReputationSync();

        //Assert
        Mockito.verify(mockGetReputationHttpEndpointSync, Mockito.atLeastOnce())
                .getReputationSync();
    }

    //Test case 3: EndpointStatus is network error -> returned network error with reputation = 0
    @Test
    public void testGetReputationSync_networkError() throws Exception {
        //Arrange
        Mockito.when(mockGetReputationHttpEndpointSync.getReputationSync()).thenReturn(
                new GetReputationHttpEndpointSync.EndpointResult(
                        GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR,
                        0
                )
        );

        //Act
        mockGetReputationHttpEndpointSync.getReputationSync();

        //Assert
        Mockito.verify(mockGetReputationHttpEndpointSync, Mockito.atLeastOnce())
                .getReputationSync();
    }

}