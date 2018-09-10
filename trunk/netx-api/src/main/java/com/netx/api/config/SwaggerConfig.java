package com.netx.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket statApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("专项奖子系统接口文档")
                .apiInfo(statApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.netx.api.controller.stat"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo statApiInfo() {
        return new ApiInfoBuilder().title("专项奖子系统").description("专项奖子系统API服务").termsOfServiceUrl("no terms of service")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket businessApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("网商子系统接口文档")
                .apiInfo(businessApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.netx.api.controller.shoppingmall"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo businessApiInfo() {
        return new ApiInfoBuilder().title("网商子系统").description("网商子系统API服务").termsOfServiceUrl("no terms of service")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket currencyApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("网信子系统接口文档")
                .apiInfo(creditApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.netx.api.controller.credit"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo creditApiInfo() {
        return new ApiInfoBuilder().title("网信子系统").description("网信子系统API服务").termsOfServiceUrl("no terms of service")
                .version("1.0")
                .build();
    }
    
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("网值子系统接口文档")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.netx.api.controller.worth"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("网值模块API").description("注意：<br>" +
                "1：所有的托管费用不能使用网币，也就是只有当真正支付时才可使用网币，凡是涉及到暂时冻结的，以后会返还的费用都不能用网币。<br>" +
                "2：禁止调用定时任务的方法").termsOfServiceUrl("")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户接口文档")
                .apiInfo(userInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.netx.api.controller.ucenter.user"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public Docket commonApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("通用接口文档")
                .apiInfo(userInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.netx.api.controller.ucenter.common"))
                .paths(PathSelectors.any()).build();
    }
    @Bean
    public Docket friendApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("好友接口文档")
                .apiInfo(userInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.netx.api.controller.ucenter.friend"))
                .paths(PathSelectors.any()).build();
    }
    @Bean
    public Docket routerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户合并接口文档")
                .apiInfo(userInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.netx.api.controller.ucenter.router"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo userInfo() {
        return new ApiInfoBuilder().title("ucenter API服务").description("平台描述").termsOfServiceUrl("no terms of service")
                .version("1.0")
                .build();
    }
}
