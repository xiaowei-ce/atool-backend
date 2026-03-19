package org.example.atool.utils;

import cn.hutool.core.util.ReUtil;

public class RegexUtil extends ReUtil {
    public static boolean matchAny(CharSequence content, String... regex){
        for (String reg : regex) {
            if (isMatch(reg,content)){
                return true;
            }
        }
        return false;
    }
}
