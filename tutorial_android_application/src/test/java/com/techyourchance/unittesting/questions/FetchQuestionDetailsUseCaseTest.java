package com.techyourchance.unittesting.questions;

import com.techyourchance.unittesting.common.time.TimeProvider;
import com.techyourchance.unittesting.networking.questions.FetchQuestionDetailsEndpoint;
import com.techyourchance.unittesting.networking.questions.QuestionSchema;
import com.techyourchance.unittesting.testdata.QuestionDetailsData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FetchQuestionDetailsUseCaseTest {

    // region constants
    private static final String QUESTION_ID = "question_id";
    // endregion constants

    // region helper fields
    private EndpointMock fetchQuestionDetailsEndpointMock;
    @Mock
    FetchQuestionDetailsUseCase.Listener listener1;

    @Mock
    FetchQuestionDetailsUseCase.Listener listener2;

    @Mock
    TimeProvider timeProviderMock;

    // endregion helper fields
    FetchQuestionDetailsUseCase SUT;

    @Before
    public void setup() throws Exception {
        fetchQuestionDetailsEndpointMock = new EndpointMock();
        SUT = new FetchQuestionDetailsUseCase(fetchQuestionDetailsEndpointMock, timeProviderMock);
    }

    @Test
    public void fetchQuestions_success_listenersNotifiedWithCorrectData() throws Exception {
        // Arrange
        ArgumentCaptor<QuestionDetails> ac = ArgumentCaptor.forClass(QuestionDetails.class);
        success();
        SUT.registerListener(listener1);
        SUT.registerListener(listener2);
        // Act
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID);
        // Assert - that business object/domain QuestionDetails is passed
        verify(listener1).onQuestionDetailsFetched(ac.capture());
        verify(listener2).onQuestionDetailsFetched(ac.capture());
        List<QuestionDetails> acList = ac.getAllValues();
        assertThat(acList.get(0), is(getQuestionDetails()));
        assertThat(acList.get(1), is(getQuestionDetails()));
    }

    @Test
    public void fetchQuestions_failure_listenersNotifiedOfError() throws Exception {
        // Arrange
        failure();
        SUT.registerListener(listener1);
        SUT.registerListener(listener2);
        // Act
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID);
        // Assert - that listener is notified of error
        verify(listener1).onQuestionDetailsFetchFailed();
        verify(listener2).onQuestionDetailsFetchFailed();
    }

    @Test
    public void fetchQuestions_success_newDetailsReturnedAfterTimeout() throws Exception {
        // Arrange
        ArgumentCaptor<QuestionDetails> ac = ArgumentCaptor.forClass(QuestionDetails.class);
        success();
        SUT.registerListener(listener1);
        SUT.registerListener(listener2);
        // Act
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(0l);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(10000l);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID);
        // Assert
        verify(listener1, times(2)).onQuestionDetailsFetched(ac.capture());
        verify(listener2, times(2)).onQuestionDetailsFetched(ac.capture());
        List<QuestionDetails> acList = ac.getAllValues();
        assertThat(acList.get(0), is(getQuestionDetails()));
        assertThat(acList.get(1), not(getQuestionDetails()));
        assertThat(acList.get(2), is(getQuestionDetails()));
        assertThat(acList.get(3), not(getQuestionDetails()));
    }

    @Test
    public void fetchQuestions_success_returnCachedDetailsIfWithinTimeout() throws Exception {
        // Arrange
        ArgumentCaptor<QuestionDetails> ac = ArgumentCaptor.forClass(QuestionDetails.class);
        success();
        SUT.registerListener(listener1);
        SUT.registerListener(listener2);
        // Act
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(0l);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(8000l);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID);
        // Assert
        verify(listener1, times(2)).onQuestionDetailsFetched(ac.capture());
        verify(listener2, times(2)).onQuestionDetailsFetched(ac.capture());
        List<QuestionDetails> acList = ac.getAllValues();
        assertThat(acList.get(0), is(getQuestionDetails()));
        assertThat(acList.get(1), is(getQuestionDetails()));
        assertThat(acList.get(2), is(getQuestionDetails()));
        assertThat(acList.get(3), is(getQuestionDetails()));
    }

    // region helper methods
    private QuestionDetails getQuestionDetails() {
        return new QuestionDetails(QUESTION_ID, "title1", "body1");
    }

    private void success() {
        // no-op
    }

    private void failure() {
        fetchQuestionDetailsEndpointMock.failure = true;
    }
    // endregion helper methods

    // region helper classes
    private class EndpointMock extends FetchQuestionDetailsEndpoint {

        public boolean failure;
        private int calls = 0;

        public EndpointMock() {
            super(null);
        }

        @Override
        public void fetchQuestionDetails(String questionId, Listener listener) {
            calls++;
            if(failure) {
                listener.onQuestionDetailsFetchFailed();
            } else {
                QuestionSchema questionSchema;
                questionSchema = new QuestionSchema("title" + calls, questionId, "body" + calls);
                listener.onQuestionDetailsFetched(questionSchema);
            }
        }
    }
    // endregion helper classes

}