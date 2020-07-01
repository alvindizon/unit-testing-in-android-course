package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class FetchUserProfileUseCaseSyncTest {

    public static String USER_ID = "user_id";
    public static String FULLNAME = "Full N. Ame";
    public static String IMAGE_URL = "https://img.com/i/123456";

    UserProfileHttpEndPointSyncTd userProfileHttpEndPointSyncTd;
    UsersCacheTd usersCacheTd;

    FetchUserProfileUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        userProfileHttpEndPointSyncTd = new UserProfileHttpEndPointSyncTd();
        usersCacheTd = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(userProfileHttpEndPointSyncTd, usersCacheTd);
    }

    @Test
    public void fetchUserProfile_success_endpointResultIsSuccess() throws Exception{
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(userProfileHttpEndPointSyncTd.getUserProfile(USER_ID).getStatus(), is(UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS));
    }

    @Test
    public void fetchUserProfile_success_userIdIsPassedToEndpointResult() throws Exception{
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(userProfileHttpEndPointSyncTd.getUserProfile(USER_ID).getUserId(), is(USER_ID));
    }

    @Test
    public void fetchUserProfile_success_expectedFullNameIsInEndpointResult() throws Exception{
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(userProfileHttpEndPointSyncTd.getUserProfile(USER_ID).getFullName(), is(FULLNAME));
    }

    @Test
    public void fetchUserProfile_success_expectedImageUrlIsInEndpointResult() throws Exception{
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(userProfileHttpEndPointSyncTd.getUserProfile(FULLNAME).getImageUrl(), is(IMAGE_URL));
    }

    @Test
    public void fetchUserProfile_success_endpointResultWithSuccessStatusReturned() throws Exception{
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void fetchUserProfile_success_userCacheContainsUserWithExpectedUserId() throws Exception {
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(usersCacheTd.getUser(USER_ID).getUserId(), is(USER_ID));
    }

    @Test
    public void fetchUserProfile_success_userCacheContainsUserWithExpectedFullName() throws Exception {
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(usersCacheTd.getUser(USER_ID).getFullName(), is(FULLNAME));
    }

    @Test
    public void fetchUserProfile_success_userCacheContainsUserWithExpectedImageUrl() throws Exception {
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(usersCacheTd.getUser(USER_ID).getImageUrl(), is(IMAGE_URL));
    }

    @Test
    public void fetchUserProfile_success_userEndPointContainsUserId() throws Exception {
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(usersCacheTd.getUser(USER_ID).getImageUrl(), is(IMAGE_URL));
    }

    @Test
    public void fetchUserProfile_success_userIdPassedToEndpoint() throws Exception {
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(userProfileHttpEndPointSyncTd.userId, is(USER_ID));
    }

    @Test
    public void fetchUserProfile_authError_userCacheIsNullOnError() throws Exception{
        userProfileHttpEndPointSyncTd.isAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(usersCacheTd.getUser(USER_ID));
    }

    @Test
    public void fetchUserProfile_serverError_userCacheIsNullOnError() throws Exception{
        userProfileHttpEndPointSyncTd.isServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(usersCacheTd.getUser(USER_ID));
    }

    @Test
    public void fetchUserProfile_generalError_userCacheIsNullOnError() throws Exception{
        userProfileHttpEndPointSyncTd.isGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(usersCacheTd.getUser(USER_ID));
    }

    @Test
    public void fetchUserProfile_authError_endpointResultWithErrorStatusReturned() throws Exception{
        userProfileHttpEndPointSyncTd.isAuthError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfile_serverError_endpointResultWithErrorStatusReturned() throws Exception{
        userProfileHttpEndPointSyncTd.isServerError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfile_generalError_endpointResultWithErrorStatusReturned() throws Exception{
        userProfileHttpEndPointSyncTd.isGeneralError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfile_networkError_endpointResultWithErrorStatusReturned() throws Exception{
        userProfileHttpEndPointSyncTd.isNetworkError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }


    private static class UserProfileHttpEndPointSyncTd implements UserProfileHttpEndpointSync {

        private String userId = "";

        private boolean isAuthError;

        private boolean isServerError;

        private boolean isGeneralError;

        private boolean isNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            this.userId = userId;

            if(isAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            } else if(isServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            } else if(isGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            } else if(isNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, userId, FULLNAME, IMAGE_URL);
            }
        }
    }

    private static class UsersCacheTd implements UsersCache {

        private List<User> users = new ArrayList<>();

        @Override
        public void cacheUser(User user) {
            User existingUser = getUser(user.getUserId());
            if(existingUser != null) {
                // for purposes of testing, we continually make room for repeated requests using the same user
                users.remove(existingUser);
            }
            users.add(user);
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            for(User user : users) {
                if(user.getUserId().contentEquals(userId)) {
                    return user;
                }
            }
            return null;
        }
    }
}