package org.example.atool.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "atool.regcheck")
public class RegexProp {
    Map<String,String> regex;
}
