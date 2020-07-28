package com.techyourchance.unittesting.testdata;

import com.techyourchance.unittesting.questions.QuestionDetails;

public class QuestionDetailsData {
    public static final String QUESTION_ID = "question_id";
    public static final String TITLE = "title";
    public static final String BODY = "body";

    public static QuestionDetails getQuestionDetails() {
        return new QuestionDetails(QUESTION_ID, TITLE, BODY);
    }
}
