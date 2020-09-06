package com.lyl.config;

import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Title: PageHelperConfig
 * @Description: 使用PageHelper分页插件的一个配置类，除了使用配置类外，还可以直接在配置文件中进行配置。
 * @date: 2019年8月24日 下午1:18:01
 */
@Configuration
public class PageHelperConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public PageHelper pageHelper() {
        logger.info("\n MyBatisConfiguration.pageHelper()分页插件成功注册到容器中......");

        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        // 配置mysql数据库的方言
        properties.setProperty("dialect", "mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

}
