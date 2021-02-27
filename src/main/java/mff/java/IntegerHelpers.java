package mff.java;

public class IntegerHelpers {
    /**
     * try to get int from @text
     * @param text given text
     * @param defaultValue value to return if parsing is not successful
     * @return integer value or @defaultValue if @text cannot be parsed
     */
    public static int tryGetInt(String text, int defaultValue) {
        try {
            return Integer.parseInt(text);
        }
        catch (NumberFormatException exception) {
            return defaultValue;
        }
    }
}
