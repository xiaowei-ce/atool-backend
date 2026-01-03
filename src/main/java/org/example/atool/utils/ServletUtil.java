package org.example.atool.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ServletUtil {

    public static void write(HttpServletResponse response, Object obj){
        write(response , JSONUtil.toJsonStrIncludeNull(obj));
    }

    public static void write(HttpServletResponse response, String str){
        try {
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
