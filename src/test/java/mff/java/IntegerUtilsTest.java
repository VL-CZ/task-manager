package mff.java;

import mff.java.utils.IntegerUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegerUtilsTest {

    // test tryGetInt

    /**
     * check valid input
     */
    @Test
    public void tryGetInt_validInteger() {
        int number = IntegerUtils.tryGetInt("5", 0);
        Assertions.assertEquals(5, number);
    }

    /**
     * check that it returns default value on invalid input
     */
    @Test
    public void tryGetInt_invalid() {
        int defaultValue = 10;
        int number = IntegerUtils.tryGetInt("asdfkjvnadf", defaultValue);
        Assertions.assertEquals(defaultValue, number);
    }

    /**
     * another invalid input, check if it returns default value
     */
    @Test
    public void tryGetInt_invalidNumber() {
        int defaultValue = 30;
        int number = IntegerUtils.tryGetInt("5.2dff", defaultValue);
        Assertions.assertEquals(defaultValue, number);
    }
}
