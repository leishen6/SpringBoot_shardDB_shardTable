package com.lyl.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @PACKAGE_NAME: com.lyl.utils
 * @ClassName: SpringContextJobUtil
 * @Description: spring 上下文工具类，用于在spring容器启动后，使用getBean方法获取容器中的实例bean
 * @Date: 2020-06-26 23:46
 **/
@Component // 工具类需要注入到容器中
public class SpringContextJobUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    @SuppressWarnings("static-access" )
    public void setApplicationContext(ApplicationContext contex)
            throws BeansException {
        this.context = contex;
    }

    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

    public static String getMessage(String key){
        return context.getMessage(key, null, Locale.getDefault());
    }

}
