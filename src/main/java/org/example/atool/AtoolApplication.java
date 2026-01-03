package org.example.atool;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ForestScan(basePackages = "org.example.atool.HttpClient")
@MapperScan(basePackages = "org.example.atool.mapper")
public class AtoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtoolApplication.class, args);
    }
}
