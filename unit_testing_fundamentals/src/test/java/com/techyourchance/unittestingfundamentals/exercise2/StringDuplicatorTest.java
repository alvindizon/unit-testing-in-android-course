package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringDuplicatorTest {

    StringDuplicator SUT;

    @Before
    public void setUp() throws Exception {
        SUT =  new StringDuplicator();
    }

    @Test
    public void duplicate_emptyString_emptyStringReturned() {
        String result = SUT.duplicate("");
        assertThat(result, is(""));
    }

    @Test
    public void duplicate_string_duplicateStringReturned() {
        String result = SUT.duplicate("hi");
        assertThat(result, is("hihi"));
    }

    @Test
    public void duplicate_singleChar_twoDuplicateCharsReturned() {
        String result = SUT.duplicate(Character.toString('T'));
        assertThat(result, is("TT"));
    }

}