package com.techyourchance.testdrivendevelopment.exercise7.networking;

public class GetReputationHttpEndpointSyncImpl implements GetReputationHttpEndpointSync {

    private boolean isGeneralError = false;
    private boolean isNetworkError = false;

    @Override
    public EndpointResult getReputationSync() {
        EndpointResult endpointResult;

        if (isGeneralError) {
            endpointResult = new EndpointResult(EndpointStatus.GENERAL_ERROR, 0);
        } else if (isNetworkError) {
            endpointResult = new EndpointResult(EndpointStatus.NETWORK_ERROR, 0);
        } else {
            endpointResult = new EndpointResult(EndpointStatus.SUCCESS, 10);
        }

        return endpointResult;
    }
}
