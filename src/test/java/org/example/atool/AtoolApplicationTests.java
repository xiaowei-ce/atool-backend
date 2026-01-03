package org.example.atool;

import org.example.atool.utils.Converter.ContertersType;
import org.example.atool.utils.Converter.TextToJsonConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AtoolApplicationTests {


    @Test
    void contextLoads() {
//        String data = skyApiClient.data("hjkhjkh", "4HR0-D28R-1GA2");
//        System.out.println(data);
//        System.out.println(JSONUtil.toJsonStrIncludeNull(data));
//        System.out.println(JSONUtil.isTypeJSON("assa"));

//        SkyData bean = JSONUtil.toBean("adasd", SkyData.class);
//        System.out.println(bean);
        System.out.println(TextToJsonConverter.convert("""
                身高解析结果：
                体型值: -0.04459
                身高值: -1.94260
                最高身高: 1.97010
                最矮身高: 13.97010
                当前身高: 13.79790
                当前身高描述: 非常矮身高
                距离最低还差: 0.1722
                光崽是较瘦体型
                
                当前角色服装：
                发型: 樱花发型
                面具: 黑脸面具
                发饰: 未穿戴
                斗篷: 樱花斗篷
                背饰: 长笛
                裤子: 武士裤
                查询耗时: 1.65687 s
                查询时间：15时35分25秒""", ContertersType.JACKSON));
    }
}
