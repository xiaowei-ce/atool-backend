package org.example.atool.ApiClient;

import com.dtflys.forest.annotation.GetRequest;
import com.dtflys.forest.annotation.Var;
import org.example.atool.entity.dto.TestDTO;

public interface TestApi {

    @GetRequest(url = "https://imgapi.jinghuashang.cn/random?type={type}", dataType = "json")
    TestDTO test(@Var("type") String type);
}
