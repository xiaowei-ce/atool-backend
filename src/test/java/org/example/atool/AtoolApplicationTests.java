package org.example.atool;

import cn.hutool.core.util.ReUtil;
import org.example.atool.entity.dto.RegisterDTO;
import org.example.atool.properties.RegExpProp;
import org.example.atool.service.UserService;
import org.example.atool.strategy.typeCkeck.Checker;
import org.example.atool.strategy.typeCkeck.CheckerFactory;
import org.example.atool.utils.Throw;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AtoolApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    RegExpProp regExpProp;

    @Test
    void contextLoads() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setAccount("234@123.");
        registerDTO.setCaptcha("000000");
        registerDTO.setPassword("wwdsfdsfdsdsgfdfg1");
        Checker checker = CheckerFactory.get("email");
//        System.out.println(checker.check(registerDTO.getAccount()));

        System.out.println(regExpProp);

//        userService.register("phone",dto);

    }

}
