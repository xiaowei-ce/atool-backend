package org.example.atool.utils;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.jspecify.annotations.NonNull;

public class StrFmt {
    public static String fmt(String template, String fmt, @NonNull Object... params) {
        if (StrUtil.isBlank(template)) {
            return "null";
        } else {
            return !ArrayUtil.isEmpty(params) && !StrUtil.isBlank(template) ? StrFormatter.formatWith(template, fmt, params) : template;
        }
    }
}
