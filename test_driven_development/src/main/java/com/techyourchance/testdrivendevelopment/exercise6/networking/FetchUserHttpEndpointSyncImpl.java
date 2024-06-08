package com.techyourchance.testdrivendevelopment.exercise6.networking;

import com.techyourchance.testdrivendevelopment.exercise6.Ex6Constant;

public class FetchUserHttpEndpointSyncImpl implements FetchUserHttpEndpointSync {

    private boolean mIsAuthError = false;
    private boolean mIsGeneralError = false;

    public FetchUserHttpEndpointSyncImpl() {
        mIsAuthError = false;
        mIsGeneralError = false;
    }

    @Override
    public EndpointResult fetchUserSync(String userId) throws NetworkErrorException {
         if (mIsAuthError) {
             return new EndpointResult(EndpointStatus.AUTH_ERROR, "", "");
         } else if (mIsGeneralError) {
             return new EndpointResult(EndpointStatus.GENERAL_ERROR, "", "");
         } else {
             return new EndpointResult(EndpointStatus.SUCCESS, Ex6Constant.INPUT_MOCK_USER_ID, Ex6Constant.INPUT_MOCK_USER_NAME);
         }
    }
}
