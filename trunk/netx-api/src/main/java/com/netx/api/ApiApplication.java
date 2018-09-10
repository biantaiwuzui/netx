package com.netx.api;

import com.mysql.cj.x.MysqlxSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {"com.netx.api.config.*"})
//@MapperScan(basePackages = {"com.netx.ucenter.mapper.**"},sqlSessionTemplateRef = "sqlmap")
@ComponentScan(basePackages = {"com.netx.common.**"})
@ComponentScan(basePackages = {"com.netx.fuse.**"})
@ComponentScan(basePackages = {"com.netx.shopping.**"})
@ComponentScan(basePackages = {"com.netx.worth.**"})
@ComponentScan(basePackages = {"com.netx.ucenter.**"})
@ComponentScan(basePackages = {"com.netx.credit.**"})
@ComponentScan(basePackages = {"com.netx.searchengine.**"})
public class ApiApplication {
	public static void main(String[] args) {
//		PasswordEncoder encoder =  new BCryptPasswordEncoder();
//		String password = encoder.encode("123456");
		SpringApplication.run(ApiApplication.class, args);
	}
}
