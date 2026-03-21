package org.example.atool.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "atool.lottery")
public class LotteryProp {
    long min = 25;
    long max = 65;
}
