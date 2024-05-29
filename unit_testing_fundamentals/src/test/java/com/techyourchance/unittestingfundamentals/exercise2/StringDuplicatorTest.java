package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringDuplicatorTest {

    StringDuplicator stringDuplicator;

    @Before
    public void setUp() throws Exception {
        //GIVEN
        stringDuplicator = new StringDuplicator();
    }

    @Test
    public void Test_duplicate_Should_return_true_When_inputEmptyString_outputEmptyString() {
        //WHEN
        String actual = stringDuplicator.duplicate("");
        String expect = "";

        //THEN
        Assert.assertEquals(expect, actual);
    }

    @Test
    public void Test_duplicate_Should_return_false_When_inputEmptyString_outputEmptyString() {
        //WHEN
        String actual = stringDuplicator.duplicate("");
        String expect = "aaaaa";

        //THEN
        Assert.assertNotEquals(expect, actual);
    }

    @Test
    public void Test_duplicate_Should_return_true_When_inputNotEmptyString_outputItStringWithDuplicate() {
        //WHEN
        String actual = stringDuplicator.duplicate("hi");
        String expect = "hihi";

        //THEN
        Assert.assertEquals(expect, actual);
    }

    @Test
    public void Test_duplicate_Should_return_false_When_inputNotEmptyString_outputItStringWithDuplicate() {
        //WHEN
        String actual = stringDuplicator.duplicate("hi");
        String expect = "hihihi";

        //THEN
        Assert.assertNotEquals(expect, actual);
    }

    
}