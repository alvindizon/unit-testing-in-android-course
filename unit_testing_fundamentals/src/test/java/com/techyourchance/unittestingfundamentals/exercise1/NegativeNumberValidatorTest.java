package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class NegativeNumberValidatorTest {

    NegativeNumberValidator SUT;

    @Before
    public void setup() {
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void negativeTest() {
        boolean result = SUT.isNegative(-1);
        Assert.assertThat(result, is(true));
    }

    @Test
    public void positiveTest() {
        boolean result = SUT.isNegative(4);
        Assert.assertThat(result, is(false));
    }

    @Test
    public void zeroTest() {
        boolean result = SUT.isNegative(0);
        Assert.assertThat(result, is(false));
    }


}