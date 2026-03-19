package org.example.atool.HttpClient;

import com.dtflys.forest.annotation.GetRequest;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.http.ForestResponse;


public interface SkyApiClient {
    @GetRequest(url = "https://ovoav.com/api/sky/sgwz/sgd?key=Y0L4u3qMZvtNI" , dataType = "text")
    ForestResponse<String> data(@Query("id") String id);

    @GetRequest(url = "https://ovoav.com/api/sky/lbcx/gflb?key=Y0L4u3qMZvtNI&type=json" ,dataType = "text")
    ForestResponse<String> gift(@Query("id") String id);
}
