package org.example.atool.components.alibabaCloud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliBabaCloudConfig {
    @Bean
    public com.aliyun.dypnsapi20170525.Client aliCloudClient(){
        try {
        // 工程代码建议使用更安全的无AK方式，凭据配置方式请参见：https://help.aliyun.com/document_detail/378657.html。
        com.aliyun.credentials.Client credential = new com.aliyun.credentials.Client();
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setCredential(credential);

        // Endpoint 请参考 https://api.aliyun.com/product/Dypnsapi
        config.endpoint = "dypnsapi.aliyuncs.com";

            return new com.aliyun.dypnsapi20170525.Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
