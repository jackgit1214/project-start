package com.project.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelSpecification;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@EnableKnife4j
@Configuration
@EnableOpenApi
public class SwaggerDocumentationConfig {

    private List<RequestParameter> getParameters(){

        RequestParameterBuilder tokenPar = new RequestParameterBuilder();
        List<RequestParameter> globalRequestParameters = new ArrayList<RequestParameter>();
        tokenPar.name("Authorization")
                .description("授权token")
                .required(true)
                .in(ParameterType.HEADER);
        globalRequestParameters.add(tokenPar.build());    //根据每个方法名也知道当前方法在设置什么参数
        return globalRequestParameters;
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.OAS_30)
                .select()
                    //.apis(RequestHandlerSelectors.basePackage("com.project.resources"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(Operation.class)).paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .globalRequestParameters(getParameters())
                .globalResponses(HttpMethod.GET, getGlobalResponseMessage())
                .globalResponses(HttpMethod.POST, getGlobalResponseMessage());
    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("基本管理功能API")
            .description("基本系统管理模块文档，包含用户管理、日志管理、角色权限管理、组织结构管理等")
            .termsOfServiceUrl("")
            .version("1.0.0")
            .contact(new Contact("jack","", "19707910@qq.com"))
            .build();
    }

    /**
     * 封装通用响应信息
     */
    private List<Response> getGlobalResponseMessage() {
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("404").description("未找到资源").build());
        return responseList;
    }
}
