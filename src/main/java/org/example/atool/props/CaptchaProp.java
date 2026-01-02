package org.example.atool.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@Component
@ConfigurationProperties(prefix = "atool.captcha")
public class CaptchaProp {
    private Integer captchaCodeLen = 6;
    private List<String> types;
    private Long timeout = 60L;
    private TimeUnit unit;
}
