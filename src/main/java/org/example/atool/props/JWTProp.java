package org.example.atool.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Data
@ConfigurationProperties(prefix = "atool.jwt")
@Component
public class JWTProp {
    private byte[] key;
    private Long expire;
    private TimeUnit unit;

    public void setKey(String key) {
        this.key = key.getBytes(StandardCharsets.UTF_8);
    }
}
