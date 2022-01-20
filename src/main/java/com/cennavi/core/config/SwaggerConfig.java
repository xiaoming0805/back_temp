package com.cennavi.core.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableSwaggerBootstrapUi;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUi
public class SwaggerConfig {
    @Value("${swagger.title}")
    private String title;
    @Value("${swagger.version}")
    private String version;
    @Value("${swagger.author}")
    private String author;
    @Value("${swagger.email}")
    private String email;
    @Value("${swagger.enable}")
    private boolean enableSwagger;
    @Bean
    public Docket customDocket() {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> parameters = Lists.newArrayList();
        parameterBuilder.name("token").description("token令牌").modelRef(new ModelRef("String"))
                .parameterType("header")
                .required(false).build();
        parameters.add(parameterBuilder.build());
        // 添加全局响应状态码
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(200).message("Http请求成功状态码，Http状态码不用于与业务相关，无论对错均为200，除非出现网络状况，或者服务器无响应").responseModel(new ModelRef("ApiError")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("{\"code\":500,\"message\":\"服务器内部错误\",\"data\":null}").responseModel(new ModelRef("ApiError")).build());
        responseMessageList.add(new ResponseMessageBuilder().code(1000).message("{\"code\":1000,\"message\":\"token缺失或错误\",\"data\":null}").responseModel(new ModelRef("ApiError")).build());
        return new Docket(DocumentationType.SWAGGER_2)
                // 添加全局响应状态码
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .apiInfo(apiInfo())
                .groupName("V1.0.0版本")
                .enable(enableSwagger)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cennavi.modules"))// 扫描controller的范围
                .paths(PathSelectors.any())
                .build().globalOperationParameters(parameters);
    }
    public ApiInfo apiInfo()  {
        try {
            title=new String(title.getBytes("iso-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ApiInfoBuilder()
                .title(title)
                .version(version)
                .contact(new Contact(author, null, email))
                .description(title+"接口文档")
                .build();
    }
}
