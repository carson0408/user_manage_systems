package com.carson.cachedemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.carson.cachedemo.mapper")
@EnableScheduling
@EnableCaching
public class CachedemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CachedemoApplication.class, args);
    }

}
