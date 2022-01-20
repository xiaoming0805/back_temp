package com.cennavi.core.config;

import com.cennavi.core.filter.WebContextFilter;
import com.cennavi.core.interceptor.CommonInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by sunpengyan on 2018/12/14.
 */
@Configuration
public class MyWebConfig implements WebMvcConfigurer {

    @Autowired
    private CommonInterceptor commonInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor).addPathPatterns("/**").excludePathPatterns();
    }

    /**
     * 过滤器两种方案 1.过滤器注册配置类：如下即可
     * 2.注解方式配置过滤器：在自定义过滤器上添加注解 @WebFilter(urlPatterns = {"/*"})  在，Application中添加@ServletComponentScan
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();//新建过滤器注册类
        registration.setFilter(new WebContextFilter());// 添加我们写好的过滤器
        registration.addUrlPatterns("/*");// 设置过滤器的URL模式
        return registration;
    }

    /**
     * 添加静态资源访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 跨域
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*");
    }
}
