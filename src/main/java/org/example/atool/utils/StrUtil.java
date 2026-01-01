package org.example.atool.utils;

import java.util.Arrays;
import java.util.List;

public class StrUtil {
    public static boolean anyBlank(String... strings){
        List<String> list = Arrays.asList(strings);
        if (list.isEmpty()) {
            return true;
        }

        for (String string : list) {
            if (string.isBlank()) {
                return true;
            }
        }

        return false;
    }

}
