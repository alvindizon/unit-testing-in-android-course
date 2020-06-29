package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsAdjacencyDetector();
    }

    // interval 1 is adjacent to interval 2
    @Test
    public void isAdjacent_interval1IsAdjacentToInterval2_trueReturned() throws Exception {
        boolean result = SUT.isAdjacent(new Interval(-1, 0), new Interval(0, 2));
        assertThat(result, is(true));
    }
    // interval 1 is before interval 2
    @Test
    public void isAdjacent_interval1IsBeforeInterval2_falseReturned() throws Exception {
        boolean result = SUT.isAdjacent(new Interval(-2, 2), new Interval(3, 4));
        assertThat(result, is(false));
    }

    // interval 1 is after interval 2
    @Test
    public void isAdjacent_interval1IsAfterInterval2_falseReturned() throws Exception {
        boolean result = SUT.isAdjacent(new Interval(-2, 2), new Interval(-4, -3));
        assertThat(result, is(false));
    }

    // same intervals
    @Test
    public void isAdjacent_interval1IsEqualToInterval2_falseReturned() throws Exception {
        boolean result = SUT.isAdjacent(new Interval(-2, 2), new Interval(-2, 2));
        assertThat(result, is(false));
    }

    // interval 1 is after and adjacent to interval 2
    @Test
    public void isAdjacent_interval1IsAfterAndAdjacentToInterval2_falseReturned() throws Exception {
        boolean result = SUT.isAdjacent(new Interval(0, 10), new Interval(10, 12));
        assertThat(result, is(false));
    }
}