package com.cennavi.search.logs.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 审计日志配置
 *
 * @author zlt
 * @date 2020/2/3
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
@Setter
@Getter
@Component
public class AuditLogProperties {
    /**
     * 是否开启审计日志
     */
    @Value("${spring.audit-log.enabled}")
    private Boolean enabled = false;
    /**
     * 日志记录类型(logger/redis/db/es)
     */
    @Value("${spring.audit-log.log-type}")
    private String logType;
}
