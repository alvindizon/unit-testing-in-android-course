package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;;

//1) If the server request completes successfully, then use case should indicate successful completion of the flow.
//2) If the server request completes successfully, then the fetched reputation should be returned
//3) If the server request fails for any reason, the use case should indicate that the flow failed.
//4) If the server request fails for any reason, the returned reputation should be 0.
public class FetchReputationUseCaseSync {

    private final GetReputationHttpEndpointSync getReputationHttpEndpointSync;

    public FetchReputationUseCaseSync(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        this.getReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    public UseCaseResult getReputationSync() {
        GetReputationHttpEndpointSync.EndpointResult endpointResult = getReputationHttpEndpointSync.getReputationSync();
        switch (endpointResult.getStatus()) {
            case NETWORK_ERROR:
            case GENERAL_ERROR:
                return UseCaseResult.FAILURE;
            case SUCCESS:
                return UseCaseResult.SUCCESS;
            default:
                throw new RuntimeException("Invalid status: " + endpointResult.getStatus().toString());
        }
    }

    public enum UseCaseResult {FAILURE, SUCCESS}
}
