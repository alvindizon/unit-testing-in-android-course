package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

//  1) If the user with given user ID is not in the cache then it should be fetched from the server.
//  2 If the user fetched from the server then it should be stored in the cache before returning to the caller.
//  3) If the user is in the cache then cached record should be returned without polling the server.
public class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {

    private final FetchUserHttpEndpointSync fetchUserHttpEndpointSync;
    private final UsersCache usersCacheMock;

    public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache usersCacheMock) {
        this.fetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        this.usersCacheMock = usersCacheMock;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {
        FetchUserHttpEndpointSync.EndpointResult result;
        try {
            if(usersCacheMock.getUser(userId) != null) {
                return new UseCaseResult(Status.SUCCESS, usersCacheMock.getUser(userId));
            }
            result = fetchUserHttpEndpointSync.fetchUserSync(userId);
        } catch (NetworkErrorException e) {
            return new UseCaseResult(Status.NETWORK_ERROR, null);
        }

        switch (result.getStatus()) {
            case SUCCESS:
                // cache user first before returning to user
                User user = new User(result.getUserId(), result.getUsername());
                usersCacheMock.cacheUser(user);
                return new UseCaseResult(Status.SUCCESS, user);
            case GENERAL_ERROR:
            case AUTH_ERROR:
            default:
                return new UseCaseResult(Status.FAILURE, null);
        }
    }
}
