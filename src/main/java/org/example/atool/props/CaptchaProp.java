package org.example.atool.props;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@Component
@ConfigurationProperties(prefix = "atool.captcha")
public class CaptchaProp {
    private Long captchaLen = 6L;
    private List<String> types;
    private Long timeout = 60L;
    private TimeUnit unit = TimeUnit.SECONDS;

    private StringBuilder emailTemplate = new StringBuilder();

    public void setEmailTemplate(String location) {
        String noClassPath = location;
        try {
            if(StrUtil.contains(location,"classpath:")){
                noClassPath = StrUtil.removePrefix(location,"classpath:");
            }else if(StrUtil.contains(location,"classpath*:")) {
                noClassPath = StrUtil.removePrefix(location, "classpath*:");
            }
            ClassPathResource resource = new ClassPathResource(noClassPath);
            BufferedReader reader = resource.getReader(StandardCharsets.UTF_8);
            char[] buffer = new char[64];
            int index;
            while ((index = reader.read(buffer)) > 0){
                emailTemplate.append(buffer,0,index);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getEmailTemplate() {
        return emailTemplate.toString();
    }

    public Long getTimeoutAs(TimeUnit as){
        return as.convert(this.timeout,this.unit);
    }
}
