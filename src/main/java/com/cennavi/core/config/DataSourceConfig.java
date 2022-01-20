package com.cennavi.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by sunpengyan on 2018/12/13.
 * 多数据源配置类
 * 使用说明：在配置文件中添加如下，使用是注入otherJdbcTemplate既可
 * other.spring.datasource.driver-class-name=org.postgresql.Driver
 * other.spring.datasource.url=jdbc:postgresql://localhost:5432/tp_platform_v3
 * other.spring.datasource.username=postgres
 * other.spring.datasource.password=postgres
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "dataSource")
    @Qualifier("dataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "otherDataSource")
    @Qualifier("otherDataSource")
    @ConfigurationProperties(prefix="other.spring.datasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "otherJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("otherDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
