package com.techyourchance.unittestingfundamentals.exercise1;

/*
 *  Spec rules:
 *  - number > 0: Positive
 *  - number < 0: Negative
 *  - number = 0: Not Positive, Not Negative
 * */

public class NegativeNumberValidator {

    public boolean isNegative(int number) {
        /*
        Example with bug: the bug is that 0 will be reported as negative while it's not
        return number <= 0;*/

        //Fix here
        return number < 0; //number = 0: Not Positive, Not Negative
    }

}
