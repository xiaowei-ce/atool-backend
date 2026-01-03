package org.example.atool.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "atool.api.sky")
public class SkyApiProp {
    private String url;
    private String key;
}
