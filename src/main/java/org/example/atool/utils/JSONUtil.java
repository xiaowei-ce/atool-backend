package org.example.atool.utils;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;

public class JSONUtil extends cn.hutool.json.JSONUtil {
    private static final JSONConfig config = JSONConfig.create().setIgnoreNullValue(false);

    public static String toJsonStrIncludeNull(Object object){
        return toJsonStr(object, config);
    }

    public static JSONObject createObj(){
        return createObj(config);
    }

}
