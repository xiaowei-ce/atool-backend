package org.example.atool.utils;

import cn.hutool.json.JSONConfig;

public class JSONUtil extends cn.hutool.json.JSONUtil {
    private static final JSONConfig config = JSONConfig.create().setIgnoreNullValue(false);

    public static String toJsonStrNoIgnoreNull(Object object){
        return toJsonStr(object, config);
    }
}
