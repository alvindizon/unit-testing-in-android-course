package com.techyourchance.testdrivendevelopment.exercise6;

import com.sun.org.apache.xpath.internal.Arg;
import com.techyourchance.testdrivendevelopment.example9.networking.CartItemScheme;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR;
import static com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR;
import static com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus.SUCCESS;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentCaptor.*;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncImplTest {

    // region constants
    private static String USER_ID = "user_id";
    private static String USERNAME = "username";
    // endregion constants

    // region helper fields
    @Mock
    FetchUserHttpEndpointSync fetchUserHttpEndpointSyncMock;

    @Mock
    UsersCache usersCacheMock;
    // endregion helper fields

    FetchUserUseCaseSyncImpl SUT;

    @Before
    public void setup() throws Exception {
        SUT = new FetchUserUseCaseSyncImpl(fetchUserHttpEndpointSyncMock, usersCacheMock);
        success();
    }

    @Test
    public void fetchUser_correctParametersPassedToEndpoint() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(fetchUserHttpEndpointSyncMock).fetchUserSync(ac.capture());
        assertThat(ac.getValue(), is(USER_ID));
    }

    @Test
    public void fetchUser_success_success() throws Exception {
        // Arrange
        // Act
        FetchUserUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(useCaseResult.getStatus(), is(FetchUserUseCaseSync.Status.SUCCESS));
        assertThat(useCaseResult.getUser().getUserId(), is(USER_ID));
        assertThat(useCaseResult.getUser().getUsername(), is(USERNAME));
    }

    @Test
    public void fetchUser_authError_failureReturned() throws Exception {
        // Arrange
        authError();
        // Act
        FetchUserUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(useCaseResult.getStatus(), is(FetchUserUseCaseSync.Status.FAILURE));
        assertNull(useCaseResult.getUser());
    }

    @Test
    public void fetchUser_generalError_failureReturned() throws Exception {
        // Arrange
        generalError();
        // Act
        FetchUserUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(useCaseResult.getStatus(), is(FetchUserUseCaseSync.Status.FAILURE));
        assertNull(useCaseResult.getUser());
    }

    @Test
    public void fetchUser_networkError_networkErrorReturned() throws Exception {
        // Arrange
        networkError();
        // Act
        FetchUserUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(useCaseResult.getStatus(), is(FetchUserUseCaseSync.Status.NETWORK_ERROR));
        assertNull(useCaseResult.getUser());
    }

    // region helper methods
    private void success() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(ArgumentMatchers.any(String.class)))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(SUCCESS, USER_ID, USERNAME));
    }

    private void authError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(ArgumentMatchers.any(String.class)))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(AUTH_ERROR, null, null));
    }

    private void generalError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(ArgumentMatchers.any(String.class)))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(GENERAL_ERROR, null, null));
    }

    private void networkError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(ArgumentMatchers.any(String.class)))
                .thenThrow(new NetworkErrorException());
    }

    // endregion helper methods

    // region helper classes

    // endregion helper classes

}