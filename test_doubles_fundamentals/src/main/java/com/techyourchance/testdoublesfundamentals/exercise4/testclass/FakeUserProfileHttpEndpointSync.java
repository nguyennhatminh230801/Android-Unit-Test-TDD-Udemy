package com.techyourchance.testdoublesfundamentals.exercise4.testclass;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.FetchUserProfileUseCaseConstant;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;

public class FakeUserProfileHttpEndpointSync implements UserProfileHttpEndpointSync {

    private boolean isNetworkError = false;
    private boolean isGeneralError = false;
    private boolean isAuthError = false;
    private boolean isServerError = false;

    public boolean isNetworkError() {
        return isNetworkError;
    }

    public void setNetworkError(boolean networkError) {
        isNetworkError = networkError;
    }

    public boolean isGeneralError() {
        return isGeneralError;
    }

    public void setGeneralError(boolean generalError) {
        isGeneralError = generalError;
    }

    public boolean isAuthError() {
        return isAuthError;
    }

    public void setAuthError(boolean authError) {
        isAuthError = authError;
    }

    public boolean isServerError() {
        return isServerError;
    }

    public void setServerError(boolean serverError) {
        isServerError = serverError;
    }

    public FakeUserProfileHttpEndpointSync() {}

    @Override
    public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
        if (isGeneralError) {
            return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
        } else if (isAuthError) {
            return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
        }  else if (isServerError) {
            return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
        } else if (isNetworkError) {
            throw new NetworkErrorException();
        } else {
            return new EndpointResult(
                    EndpointResultStatus.SUCCESS,
                    userId,
                    FetchUserProfileUseCaseConstant.FULL_NAME,
                    FetchUserProfileUseCaseConstant.IMAGE_URL
            );
        }
    }

    public void setUpNetworkErrorCase() {
        isNetworkError = true;
    }

    public void setUpGeneralErrorCase() {
        isGeneralError = true;
    }

    public void setUpAuthErrorCase() {
        isAuthError = true;
    }

    public void setUpServerErrorCase() {
        isServerError = true;
    }
}
