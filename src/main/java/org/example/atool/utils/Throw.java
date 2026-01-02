package org.example.atool.utils;

import org.example.atool.Exp.BizException;

public class Throw {

    public static void BizExp(String msg)  {
        throw new BizException(msg);
    }

    public static void RTExp(String msg){
        throw new RuntimeException(msg);
    }

}
