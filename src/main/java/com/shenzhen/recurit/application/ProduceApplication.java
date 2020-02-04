package com.shenzhen.recurit.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages ={"com.shenzhen"} )
@MapperScan(value = "com.shenzhen.recurit.dao")
public class ProduceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProduceApplication.class,args);
    }
}
