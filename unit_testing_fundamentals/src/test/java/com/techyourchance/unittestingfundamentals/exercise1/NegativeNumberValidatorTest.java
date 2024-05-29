package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 *  Spec rules:
 *  - number > 0: Positive
 *  - number < 0: Negative
 *  - number = 0: Not Positive, Not Negative
 * */

public class NegativeNumberValidatorTest {

    NegativeNumberValidator negativeNumberValidator;

    //Khởi tạo mỗi test case với Before
    //
    @Before
    public void setUp() {
        negativeNumberValidator = new NegativeNumberValidator();
    }

    @Test
    public void testNegativeWhenInputNegativeNumber() {
        Assert.assertTrue(negativeNumberValidator.isNegative(-1));
    }

    @Test
    public void testNegativeWhenInputPositiveNumber() {
        Assert.assertFalse(negativeNumberValidator.isNegative(1));
    }

    @Test
    public void testNegativeWhenInputZero() {
        Assert.assertFalse(negativeNumberValidator.isNegative(0));
    }
}