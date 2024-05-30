package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector intervalsAdjacencyDetector;

    @Before
    public void setUp() {
        intervalsAdjacencyDetector = new IntervalsAdjacencyDetector();
    }

    //Case 1: interval1 = (1, 2), interval2 = (2, 3) -> true
    @Test
    public void testIsAdjacent_whenInterval1AdjacentLeftInterval2() {
        Interval interval1 = new Interval(1, 2);
        Interval interval2 = new Interval(2, 3);
        Assert.assertTrue(intervalsAdjacencyDetector.isAdjacent(interval1, interval2));
    }

    //Case 2: interval1 = (1, 2), interval2 = (3, 4) -> false
    @Test
    public void testIsAdjacent_whenInterval1NotAdjacentLeftInterval2() {
        Interval interval1 = new Interval(1, 2);
        Interval interval2 = new Interval(3, 4);
        Assert.assertFalse(intervalsAdjacencyDetector.isAdjacent(interval1, interval2));
    }

    //Case 3: interval1 = (1, 2), interval2 = (-1, 1) -> true
    @Test
    public void testIsAdjacent_whenInterval1AdjacentRightInterval2() {
        Interval interval1 = new Interval(1, 2);
        Interval interval2 = new Interval(-1, 1);
        Assert.assertTrue(intervalsAdjacencyDetector.isAdjacent(interval1, interval2));
    }

    //Case 4: interval1 = (1, 2), interval2 = (-1, 0) -> false
    @Test
    public void testIsAdjacent_whenInterval1NotAdjacentRightInterval2() {
        Interval interval1 = new Interval(1, 2);
        Interval interval2 = new Interval(-1, 0);
        Assert.assertFalse(intervalsAdjacencyDetector.isAdjacent(interval1, interval2));
    }

    //Case 5: interval1 = (-1, 5), interval2 = (-10, -3) -> false
    @Test
    public void testIsAdjacent_whenInterval1AfterInterval2() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-10, -3);
        Assert.assertFalse(intervalsAdjacencyDetector.isAdjacent(interval1, interval2));
    }

    //Case 6: interval1 = (-2, 2), interval2 = (-2, 2) -> false
    @Test
    public void testIsAdjacent_whenInterval1EqualsInterval2() {
        Interval interval1 = new Interval(-2, 2);
        Interval interval2 = new Interval(-2, 2);
        Assert.assertFalse(intervalsAdjacencyDetector.isAdjacent(interval1, interval2));
    }
}