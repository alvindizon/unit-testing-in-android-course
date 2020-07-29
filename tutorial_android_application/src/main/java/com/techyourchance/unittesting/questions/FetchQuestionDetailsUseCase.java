package com.techyourchance.unittesting.questions;

import android.util.Log;

import com.techyourchance.unittesting.common.BaseObservable;
import com.techyourchance.unittesting.common.time.TimeProvider;
import com.techyourchance.unittesting.networking.questions.FetchQuestionDetailsEndpoint;
import com.techyourchance.unittesting.networking.questions.QuestionSchema;

import java.util.HashMap;
import java.util.Map;

public class FetchQuestionDetailsUseCase extends BaseObservable<FetchQuestionDetailsUseCase.Listener> {

    public interface Listener {
        void onQuestionDetailsFetched(QuestionDetails questionDetails);
        void onQuestionDetailsFetchFailed();
    }

    private final FetchQuestionDetailsEndpoint mFetchQuestionDetailsEndpoint;
    private final TimeProvider mTimeProvider;
    private static final int CACHE_TIMEOUT_MS = 10000;
    private Map<String, Long> timestampMap = new HashMap<>();
    private Map<String, QuestionDetails> questionDetailsCache = new HashMap<>();

    public FetchQuestionDetailsUseCase(FetchQuestionDetailsEndpoint fetchQuestionDetailsEndpoint,
                                       TimeProvider timeProvider) {
        mFetchQuestionDetailsEndpoint = fetchQuestionDetailsEndpoint;
        mTimeProvider = timeProvider;
    }

    public void fetchQuestionDetailsAndNotify(final String questionId) {
        if(isCachedDataWithinTimeout(questionId)) {
            QuestionDetails cachedEntry = questionDetailsCache.get(questionId);
            if(cachedEntry != null) {
                notifySuccess(cachedEntry);
                return;
            }
        }
        mFetchQuestionDetailsEndpoint.fetchQuestionDetails(questionId, new FetchQuestionDetailsEndpoint.Listener() {
            @Override
            public void onQuestionDetailsFetched(QuestionSchema question) {
                timestampMap.put(questionId, mTimeProvider.getCurrentTimestamp());
                questionDetailsCache.put(questionId, mapResponseToQuestionDetails(question));
                notifySuccess(questionDetailsCache.get(questionId));
            }

            @Override
            public void onQuestionDetailsFetchFailed() {
                notifyFailure();
            }
        });
    }

    private void notifyFailure() {
        for (Listener listener : getListeners()) {
            listener.onQuestionDetailsFetchFailed();
        }
    }

    private void notifySuccess(QuestionDetails questionDetails) {
        for (Listener listener : getListeners()) {
            listener.onQuestionDetailsFetched(questionDetails);
        }
    }

    private boolean isCachedDataWithinTimeout(String questionId) {
        return  timestampMap.get(questionId) != null &&
                mTimeProvider.getCurrentTimestamp() < timestampMap.get(questionId) + CACHE_TIMEOUT_MS;
    }

    private QuestionDetails mapResponseToQuestionDetails(QuestionSchema questionSchema) {
        return new QuestionDetails(
                questionSchema.getId(),
                questionSchema.getTitle(),
                questionSchema.getBody()
        );
    }
}
