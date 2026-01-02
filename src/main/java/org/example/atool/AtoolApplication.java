package org.example.atool;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ForestScan(basePackages = "org.example.atool.HttpApi")
public class AtoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtoolApplication.class, args);
    }
}
