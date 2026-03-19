package org.example.atool.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "atool.epay")
public class EPayProp {
    private String key;
    private String pid;
    private String return_url;
    private String notify_url;
}
