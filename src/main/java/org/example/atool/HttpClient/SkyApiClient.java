package org.example.atool.HttpClient;

import com.dtflys.forest.annotation.GetRequest;
import com.dtflys.forest.annotation.Query;


public interface SkyApiClient {
    @GetRequest(url = "https://ovoav.com/api/sky/sgwz/sgd?key=Y0L4u3qMZvtNI" , dataType = "text")
    String data(@Query("id") String id);
}
