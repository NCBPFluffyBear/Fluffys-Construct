package io.ncbpfluffybear.fluffysconstruct.utils;

public class DataUtils {

    public static int replaceNull(Integer input, int nullReplace) {
        return input == null ? nullReplace : input;
    }

}
