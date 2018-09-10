package com.netx.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {"com.netx.schedule.config.*"})
@ComponentScan(basePackages = {"com.netx.common.**"})
@ComponentScan(basePackages = {"com.netx.utils.**"})
@ComponentScan(basePackages = {"com.netx.searchengine.**"})
@ComponentScan(basePackages = {"com.netx.shopping.**"})
@ComponentScan(basePackages = {"com.netx.worth.**"})
@ComponentScan(basePackages = {"com.netx.ucenter.**"})
@ComponentScan(basePackages = {"com.netx.credit.**"})
@ComponentScan(basePackages = {"com.netx.fuse.**"})
public class ScheduleApplication {
	public static void main(String[] args) {
		SpringApplication.run(ScheduleApplication.class, args);
	}
}
