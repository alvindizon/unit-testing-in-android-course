package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

public class IntervalsAdjacencyDetector {

    /**
     * @return true if the intervals are adjacent, but don't overlap
     */
    public boolean isAdjacent(Interval interval1, Interval interval2) {
        // check for the following
        // 1. check if ends of intervals are the same. this will take care of overlaps
        // 2. check if intervals are the same.
        return interval1.getEnd() == interval2.getStart() || interval1.getStart() == interval2.getEnd()
                && !isSameIntervals(interval1, interval2);
    }

    private boolean isSameIntervals(Interval interval1, Interval interval2) {
        return interval1.getStart() == interval2.getStart() && interval1.getEnd() == interval2.getEnd();
    }

}
