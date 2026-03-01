package org.example.atool;

import org.example.atool.mapper.PointKeysMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AtoolApplicationTests {

    @Autowired
    private PointKeysMapper pointKeysMapper;

    @Test
    void contextLoads() {
//
//        List<String> txt = new ArrayList<>(200);
//        List<PointKeys> sql = new ArrayList<>(200);
//
//        FileWriter writer = new FileWriter("./key.txt");
//        for (int i = 0; i < 150; i++) {
//            String randomed = RandomUtil.randomString(16);
//            txt.add("激活码："+randomed);
//            PointKeys keys = new PointKeys();
//            keys.setKey(randomed);
//            keys.setPoints(100L);
//            sql.add(keys);
//        }
//        writer.writeLines(txt,true);
//        pointKeysMapper.addBatch(sql);
//    }

//        boolean b = RegexUtil.matchAny("D22Y-WGAZ-KPGG", "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", "^[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}$");
//        System.out.println(b);
    }
}
