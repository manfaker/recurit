package com.shenzhen.recurit.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        //添加header参数
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder ticketPar = new ParameterBuilder();
        ticketPar.name("auth-user")
                .description("user token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .groupName("pc")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.shenzhen.recurit.controller"))// 对所有api进行监控
                .paths(PathSelectors.any())// 对所有路径进行监控
                .build()
                .globalOperationParameters(pars)    ;
    }
    private ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot 集成Swagger2")
                //作者的相关信息
                .contact("")
                //版本号
                .version("2.0")
                //详细描述
                .description("接口文档")
                .build();
    }


}
