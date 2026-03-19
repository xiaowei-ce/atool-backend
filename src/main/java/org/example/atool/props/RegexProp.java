package org.example.atool.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "atool.regex-check")
public class RegexProp {
    Map<String,String> regex;

    public String get(String key){
        return regex.get(key);
    }

    public Collection<String> getAsColl(){
        return regex.values();
    }

    public String[] get(){
        return getAsColl().toArray(new String[0]);
    }
}
