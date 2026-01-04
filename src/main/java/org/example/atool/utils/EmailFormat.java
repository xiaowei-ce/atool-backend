package org.example.atool.utils;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

public class EmailFormat {
    public static String captchaFmt(String template, Object... params) {
        if (StrUtil.isBlank(template)) {
            return "null";
        } else {
            return !ArrayUtil.isEmpty(params) && !StrUtil.isBlank(template) ? StrFormatter.formatWith(template, "#{captchaCode}", params) : template;
        }
    }
}
