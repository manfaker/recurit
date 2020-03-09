package com.shenzhen.recurit.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages ={"com.shenzhen.recurit"} )
@MapperScan(value = "com.shenzhen.recurit.dao")
@EnableScheduling
public class ProduceApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ProduceApplication.class,args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ProduceApplication.class);
    }
}
