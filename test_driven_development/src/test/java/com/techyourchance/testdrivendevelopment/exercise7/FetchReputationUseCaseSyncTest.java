package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR;
import static com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR;
import static com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointStatus.SUCCESS;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

//1) You are not allowed to write any production code unless it is to make a failing unit test pass
//2) You are not allowed to write any more of a unit test than is sufficient to fail; and compilation failures are failures
//3) You are not allowed to write any more production code than is sufficient to pass the one failing unit test
@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseSyncTest {

    // region constants
    private final int REPUTATION_SUCCESS = 100;
    private final int REPUTATION_ERROR = 0;
    // endregion constants

    // region helper fields
    @Mock
    GetReputationHttpEndpointSync getReputationHttpEndpointSyncMock;
    // endregion helper fields

    FetchReputationUseCaseSync SUT;

    @Before
    public void setup() throws Exception {
        SUT = new FetchReputationUseCaseSync(getReputationHttpEndpointSyncMock);
        success();
    }

    @Test
    public void fetchReputation_success_successReturned() throws Exception {
        // Arrange
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        // Assert
        assertThat(result, is(FetchReputationUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void fetchReputation_generalError_failureReturned() throws Exception {
        // Arrange
        generalError();
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        // Assert
        assertThat(result, is(FetchReputationUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void fetchReputation_networkError_failureReturned() throws Exception {
        // Arrange
        networkError();
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.getReputationSync();
        // Assert
        assertThat(result, is(FetchReputationUseCaseSync.UseCaseResult.FAILURE));
    }


    // region helper methods
    private void success() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(SUCCESS, REPUTATION_SUCCESS));
    }

    private void generalError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GENERAL_ERROR, REPUTATION_ERROR));
    }

    private void networkError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(NETWORK_ERROR, REPUTATION_ERROR));
    }
    // endregion helper methods

    // region helper classes

    // endregion helper classes

}