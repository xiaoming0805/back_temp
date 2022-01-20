package com.cennavi.core.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 *通过它Spring容器会自动把上下文环境对象调用ApplicationContextAware接口中的setApplicationContext方法。
 * 我们在ApplicationContextAware的实现类中，就可以通过这个上下文环境对象得到Spring容器中的Bean。
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        if (applicationContext == null){
            return null;
        }
        return (T)applicationContext.getBean(name);
    }

    public static String getProperty(String name) {
        String ss = applicationContext.getEnvironment().getProperty(name);
        try {
            ss = new String(ss.getBytes("iso-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ss;
    }
}
