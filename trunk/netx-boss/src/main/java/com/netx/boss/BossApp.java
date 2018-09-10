package com.netx.boss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.netx.boss.**"})
@ComponentScan(basePackages = {"com.netx.common.**"})
@ComponentScan(basePackages = {"com.netx.ucenter.**"})
@ComponentScan(basePackages = {"com.netx.fuse.**"})
@ComponentScan(basePackages = {"com.netx.credit.**"})
@ComponentScan(basePackages = {"com.netx.worth.**"})
@ComponentScan(basePackages = {"com.netx.shopping.**"})
@ComponentScan(basePackages = {"com.netx.searchengine.**"})
public class BossApp {
    public static void main(String[] args) {
        SpringApplication.run(BossApp.class, args);
    }
}
