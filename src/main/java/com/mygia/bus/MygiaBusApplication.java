package com.mygia.bus;

import com.mygia.bus.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class MygiaBusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MygiaBusApplication.class, args);
    }
}
