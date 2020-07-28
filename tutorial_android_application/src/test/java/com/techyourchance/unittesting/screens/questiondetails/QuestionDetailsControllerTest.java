package com.techyourchance.unittesting.screens.questiondetails;

import com.techyourchance.unittesting.questions.FetchQuestionDetailsUseCase;
import com.techyourchance.unittesting.questions.QuestionDetails;
import com.techyourchance.unittesting.screens.common.screensnavigator.ScreensNavigator;
import com.techyourchance.unittesting.screens.common.toastshelper.ToastsHelper;
import com.techyourchance.unittesting.testdata.QuestionDetailsData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class QuestionDetailsControllerTest {

    // region constants
    public static final QuestionDetails QUESTION_DETAILS = QuestionDetailsData.getQuestionDetails();
    // endregion constants

    // region helper fields
    UseCase fetchQuestionDetailsUseCaseMock;
    @Mock
    ScreensNavigator screensNavigator;
    @Mock
    ToastsHelper toastsHelper;
    @Mock QuestionDetailsViewMvc questionDetailsViewMvcMock;
    // endregion helper fields

    QuestionDetailsController SUT;

    @Before
    public void setup() throws Exception {
        fetchQuestionDetailsUseCaseMock = new UseCase();
        SUT = new QuestionDetailsController(fetchQuestionDetailsUseCaseMock, screensNavigator, toastsHelper);
        SUT.bindView(questionDetailsViewMvcMock);
    }

    // onStart - fetch question details
    @Test
    public void detailsFetched_success_onStartDetailsAreBoundToView() throws Exception {
        // Arrange
        success();
        // Act
        SUT.onStart();
        // Assert
        verify(questionDetailsViewMvcMock).bindQuestion(QUESTION_DETAILS);
    }

    // onStart - register listener for view, usecase
    @Test
    public void detailsFetched_onStartListenersRegistered() throws Exception {
        // Arrange
        // Act
        SUT.onStart();
        // Assert
        verify(questionDetailsViewMvcMock).registerListener(SUT);
        fetchQuestionDetailsUseCaseMock.verifyListenerRegistered(SUT);
    }

    // onStart - display progress indicator
    @Test
    public void detailsFetched_onStartProgressIndicatorDisplayed() throws Exception {
        // Arrange
        // Act
        SUT.onStart();
        // Assert
        verify(questionDetailsViewMvcMock).showProgressIndication();
    }

    // successful fetch - progress indicator hidden on details bind
    @Test
    public void detailsFetched_success_progressIndicatorHidden() throws Exception {
        // Arrange
        success();
        // Act
        SUT.onStart();
        // Assert
        verify(questionDetailsViewMvcMock).hideProgressIndication();
    }

    // failed fetch - hide progress indicator, show error toast
    @Test
    public void detailsFetched_error_hideProgress() throws Exception {
        // Arrange
        failure();
        // Act
        SUT.onStart();
        // Assert
        verify(questionDetailsViewMvcMock).hideProgressIndication();
    }

    // failed fetch -  show error toast
    @Test
    public void detailsFetched_error_showToast() throws Exception {
        // Arrange
        failure();
        // Act
        SUT.onStart();
        // Assert
        verify(toastsHelper).showUseCaseError();
    }

    // onStop - unregister listeners
    @Test
    public void detailsFetched_onStopUnregisterListeners() throws Exception {
        // Arrange
        // Act
        // see https://www.udemy.com/course/professional-android-unit-testing/learn/lecture/15185754#questions/10330098
        // if we don't call onStart, this test will pass
        SUT.onStart();
        SUT.onStop();
        // Assert
        verify(questionDetailsViewMvcMock).unregisterListener(SUT);
        fetchQuestionDetailsUseCaseMock.verifyListenerUnregistered(SUT);
    }
    
    // failed fetch - details not bound
    @Test
    public void detailsFetched_error_detailsNotBoundToView() throws Exception {
        // Arrange
        failure();
        // Act
        SUT.onStart();
        // Assert
        verify(questionDetailsViewMvcMock, never()).bindQuestion(any(QuestionDetails.class));
    }

    // onNavigateUpClicked - navigator called
    @Test
    public void detailsFetched_navigatorUpCalled() throws Exception {
        // Arrange
        // Act
        SUT.onNavigateUpClicked();
        // Assert
        verify(screensNavigator).navigateUp();
    }

    // region helper methods
    private void success() {
        // no-op
    }

    private void failure() {
        fetchQuestionDetailsUseCaseMock.failure = true;
    }
    // endregion helper methods

    // region helper classes
    private class UseCase extends FetchQuestionDetailsUseCase {

        public boolean failure;

        public UseCase() {
            super(null);
        }

        @Override
        public void fetchQuestionDetailsAndNotify(String questionId) {
            for(FetchQuestionDetailsUseCase.Listener listener : getListeners()) {
                if(failure) {
                    listener.onQuestionDetailsFetchFailed();
                } else {
                    listener.onQuestionDetailsFetched(new QuestionDetails(QuestionDetailsData.QUESTION_ID,
                            QuestionDetailsData.TITLE, QuestionDetailsData.BODY));
                }
            }
        }

        public void verifyListenerRegistered(QuestionDetailsController SUT) {
            for(FetchQuestionDetailsUseCase.Listener listener : getListeners()) {
                if(listener == SUT) {
                    return;
                }
                throw new RuntimeException("listener not registered");
            }
        }

        public void verifyListenerUnregistered(QuestionDetailsController SUT) {
            for(FetchQuestionDetailsUseCase.Listener listener : getListeners()) {
                if(listener == SUT) {
                    throw new RuntimeException("listener registered");
                }
            }
        }
    }
    // endregion helper classes

}