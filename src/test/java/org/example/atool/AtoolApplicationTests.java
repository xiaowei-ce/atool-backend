package org.example.atool;

import org.example.atool.entity.dto.PayNotifyDTO;
import org.example.atool.props.EPayProp;
import org.example.atool.utils.SignUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AtoolApplicationTests {

    @Autowired
    private EPayProp ePayProp;

    @Test
    void contextLoads() {


        PayNotifyDTO dto = new PayNotifyDTO();
        dto.setOut_trade_no("202603180016429fa9e22f");
        dto.setPid("1010");
        dto.setType("alipay");
        dto.setName("积分购买");
        dto.setMoney("10");
        dto.setTrade_status("TRADE_SUCCESS");
        dto.setTrade_no("1234567890");

        // 以下字段在签名拼接时会被逻辑剔除
        dto.setSign("abc");
        dto.setSign_type("MD5");

        System.out.println(SignUtil.md5Sign(dto,ePayProp.getKey()));

    }
}
