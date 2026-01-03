package org.example.atool.HttpClient;

import com.dtflys.forest.annotation.GetRequest;
import com.dtflys.forest.annotation.Query;

public interface SkyApiClient {
    @GetRequest(url = "https://ovoav.com/api/sky/sgwz/sgd" , dataType = "text")
    String data(@Query("key") String key, @Query("id") String id);
}
