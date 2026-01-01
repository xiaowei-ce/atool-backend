package org.example.atool.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    public static Result ok(String msg, Object data){
        return custom(200,msg,data);
    }

    public static Result err(String msg, Object data){
        return custom(500,msg,data);
    }

    public static Result custom(Integer code, String msg, Object data){
        return new Result(code,msg,data);
    }
}
